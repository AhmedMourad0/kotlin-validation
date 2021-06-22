package dev.ahmedmourad.validation.compiler.codegen.validations

import dev.ahmedmourad.validation.compiler.codegen.CodeSectionGenerator
import dev.ahmedmourad.validation.compiler.descriptors.ValidatorDescriptor
import dev.ahmedmourad.validation.compiler.descriptors.MetaDescriptor
import dev.ahmedmourad.validation.compiler.utils.*
import dev.ahmedmourad.validation.compiler.utils.fqNameCase
import dev.ahmedmourad.validation.compiler.utils.fqNameConstraintDescriptor
import dev.ahmedmourad.validation.compiler.utils.fqNameIllegalFun
import dev.ahmedmourad.validation.compiler.utils.fqNameLegalFun
import dev.ahmedmourad.validation.compiler.utils.fqNameValidation

//TODO: only generate copy and factory when annotated
//TODO: inline??
internal class FunctionsGenerator : CodeSectionGenerator {

    override fun imports(validatorDescriptor: ValidatorDescriptor) = setOf(
        fqNameCase.asString(),
        fqNameLegalFun.asString(),
        fqNameIllegalFun.asString(),
        fqNameSwapFun.asString(),
        fqNameOrElseFun.asString(),
        fqNameConstraintDescriptor.asString(),
        fqNameValidation.asString(),
        fqNameConstraintsDescriptor.asString(),
        fqNameIncludedValidator.asString(),
        fqNameInternalValidationApi.asString()
    ) + validatorDescriptor.violations.flatMap { violation ->
        violation.metas.flatMap { meta ->
            meta.includedValidator
                ?.let { listOf(it.isValidFqName, it.validateFqName) }
                .orEmpty()
        }
    }

    override fun generate(
        validatorDescriptor: ValidatorDescriptor
    ): Set<String> {
        return setOfNotNull(
            generateFactory(validatorDescriptor = validatorDescriptor),
            generateIsValid(validatorDescriptor = validatorDescriptor),
            generateValidate(validatorDescriptor = validatorDescriptor),
            generateFindViolatedConstraints(validatorDescriptor = validatorDescriptor),
            generateValidateNestedConstraints(validatorDescriptor = validatorDescriptor),
            generateToViolation(validatorDescriptor = validatorDescriptor)
        )
    }

    private fun generateIsValid(validatorDescriptor: ValidatorDescriptor): String {

        val subjectFqName = validatorDescriptor.subjectFqName

        val validatorFqName = validatorDescriptor.validatorFqName
        val validatorTypeParams = validatorDescriptor.validatorTypeParams
        val validatorTypeParamsAsTypeArgs = validatorDescriptor.validatorTypeParamsAsTypeArgs

        val validationContext = validatorDescriptor.validationContextName
        val validationContextImpl = validatorDescriptor.validationContextImplName

        val validationContextInstantiation = if (validatorDescriptor.isValidationContextImplAnObject) {
            validationContextImpl
        } else {
            "$validationContextImpl()"
        }

        return """
            |fun $validatorTypeParams$validatorFqName.isValid(
            |    createItem: $validationContext$validatorTypeParamsAsTypeArgs.() -> $subjectFqName
            |): Boolean {
            |
            |    val item = lazy {
            |        createItem($validationContextInstantiation)
            |    }
            |
            |    return findViolatedConstraints(item).isNotEmpty()
            |}
        """.trimMargin()
    }

    private fun generateValidate(validatorDescriptor: ValidatorDescriptor): String {

        val subjectFqName = validatorDescriptor.subjectFqName

        val validatorFqName = validatorDescriptor.validatorFqName
        val validatorTypeParams = validatorDescriptor.validatorTypeParams
        val validatorTypeParamsAsTypeArgs = validatorDescriptor.validatorTypeParamsAsTypeArgs

        val violationsParent = validatorDescriptor.violationsParentName
        val validationContext = validatorDescriptor.validationContextName
        val validationContextImpl = validatorDescriptor.validationContextImplName

        val validationContextInstantiation = if (validatorDescriptor.isValidationContextImplAnObject) {
            validationContextImpl
        } else {
            "$validationContextImpl()"
        }

        return """
            |fun $validatorTypeParams$validatorFqName.validate(
            |    createItem: $validationContext$validatorTypeParamsAsTypeArgs.() -> $subjectFqName
            |): Case<Set<$violationsParent>, $subjectFqName> {
            |
            |    val item = lazy {
            |        createItem($validationContextInstantiation)
            |    }
            |    
            |    return findViolatedConstraints(item).map { it.toViolation$validatorTypeParamsAsTypeArgs(item) }
            |        .toSet()
            |        .takeIf(Set<$violationsParent>::isNotEmpty)
            |        ?.illegal() ?: item.value.legal()
            |}
        """.trimMargin()
    }

    //TODO: generate a factory for each constructor (be careful with their visibility)
    //TODO: only generate factories for @MustBeValid annotated classes that live in the same module
    private fun generateFactory(validatorDescriptor: ValidatorDescriptor): String? {

        val subjectParams = validatorDescriptor.subjectParams ?: return null
        val subjectFqName = validatorDescriptor.subjectFqName
        val subjectSimpleName = validatorDescriptor.subjectType.simpleName()

        val validatorTypeParams = validatorDescriptor.validatorTypeParams
        val validatorTypeParamsAsTypeArgs = validatorDescriptor.validatorTypeParamsAsTypeArgs

        val validationContext = validatorDescriptor.validationContextName

        val params = subjectParams.joinToString(",\n\t") { param ->
            param.name.asString() + ": " + param.type.deepFqName()
        }

        val itemArgs = subjectParams.joinToString(",\n\t\t") { param ->
            param.name.asString()
        }

        return """
            |fun $validatorTypeParams$validationContext$validatorTypeParamsAsTypeArgs.$subjectSimpleName(
            |    $params
            |): $subjectFqName {
            |    return $subjectFqName(
            |        $itemArgs
            |    )
            |}
        """.trimMargin()
    }

    private fun generateCopy(validatorDescriptor: ValidatorDescriptor): String? {
        TODO("copy")
    }

    private fun generateFindViolatedConstraints(validatorDescriptor: ValidatorDescriptor): String {

        val subjectFqName = validatorDescriptor.subjectFqName
        val validatorFqName = validatorDescriptor.validatorFqName
        val validatorTypeParams = validatorDescriptor.validatorTypeParams
        val validatorTypeParamsAsTypeArgs = validatorDescriptor.validatorTypeParamsAsTypeArgs

        return """
            |private fun $validatorTypeParams$validatorFqName.findViolatedConstraints(
            |    item: kotlin.Lazy<$subjectFqName>
            |): Set<ConstraintDescriptor<$subjectFqName>> {
            |    return this.constraints.filterNot { constraint ->
            |        constraint.validations.all { validation ->
            |            validation.validate(item.value)
            |        } && constraint.validateNestedConstraints$validatorTypeParamsAsTypeArgs(item)
            |    }.toSet()
            |}
        """.trimMargin()
    }

    private fun generateValidateNestedConstraints(validatorDescriptor: ValidatorDescriptor): String {

        val subjectFqName = validatorDescriptor.subjectFqName

        val validatorTypeParams = validatorDescriptor.validatorTypeParams

        val violationCases = validatorDescriptor.violations.map { violation ->

            val instantiation = if (violation.inclusionMetas.isEmpty()) {
                "true"
            } else {

                val inclusionParamsVals = violation.inclusionMetas.mapIndexed { index, param ->

                    val includedSubjectFqName = param.includedValidator!!.subjectFqName
                    val includedValidatorFqName = param.includedValidator.validatorFqName

                    val includedValidatorTypeArgs =
                        "<$subjectFqName, $includedSubjectFqName, $includedValidatorFqName>"
                    val valName = "${param.name}Descriptor"

                    """
                    |val $valName = this.includedValidator[$index] as ${fqNameIncludedValidator.asString()}$includedValidatorTypeArgs
                    |val ${param.name} = $valName.isValid(item.value) {
                    |    this.isValid { it }
                    |}
                    """.trimMargin()
                }.joinToString(separator = "\n\n")

                """
                |
                |$inclusionParamsVals
                |
                |${violation.inclusionMetas.joinToString(" && ") { it.name }}
                """.trimMargin()
            }

            """
            |"${violation.name}" -> {
            |    ${instantiation.replace("\n", "\n\t")}
            |}
            """.trimMargin()
        }

        return """
            |@Suppress("UNCHECKED_CAST")
            |@OptIn(InternalValidationApi::class)
            |private fun ${validatorTypeParams}ConstraintDescriptor<$subjectFqName>.validateNestedConstraints(
            |    item: kotlin.Lazy<$subjectFqName>
            |): Boolean {
            |    return when (this.violation) {
            |    
            |        ${violationCases.joinToString("\n\n").replace("\n", "\n\t\t")}
            |        
            |        else -> {
            |            throw kotlin.IllegalArgumentException(
            |                "`${"$"}{this.violation}` is not a recognizable violation"
            |            )
            |        }
            |    }
            |}
        """.trimMargin()
    }

    private fun generateToViolation(
        validatorDescriptor: ValidatorDescriptor
    ): String {

        val subjectFqName = validatorDescriptor.subjectFqName

        val validatorTypeParams = validatorDescriptor.validatorTypeParams

        val violationsParent = validatorDescriptor.violationsParentName

        val violationCases = validatorDescriptor.violations.map { violation ->

            val instantiation = if (violation.metas.isEmpty()) {
                "$violationsParent.${violation.name}"
            } else {

                val regularArgs = violation.regularMetas.mapIndexed { index, arg ->
                    "${arg.name} = this.metadata[$index].get(item.value) as ${arg.typeFqName}"
                }

                val inclusionArgsList = violation.inclusionMetas
                    .takeIf(List<MetaDescriptor>::isNotEmpty)

                val inclusionArgsVals = inclusionArgsList?.mapIndexed { index, arg ->

                    val includedSubjectFqName = arg.includedValidator!!.subjectFqName
                    val includedValidatorFqName = arg.includedValidator.validatorFqName

                    val includedValidatorTypeArgs =
                        "<$subjectFqName, $includedSubjectFqName, $includedValidatorFqName>"

                    val valName = "${arg.name}Descriptor"

                    """
                    |val $valName = this.includedValidator[$index] as ${fqNameIncludedValidator.asString()}$includedValidatorTypeArgs
                    |val ${arg.name} = $valName.findViolations(item.value) {
                    |    this.validate { it }
                    |}
                    """.trimMargin()
                }?.joinToString(separator = "\n\n", prefix = "\n", postfix = "\n").orEmpty()

                val inclusionArgs = inclusionArgsList?.map { arg ->
                    "${arg.name} = ${arg.name}"
                }.orEmpty()

                """
                |$inclusionArgsVals
                |$violationsParent.${violation.name}(
                |    ${(regularArgs + inclusionArgs).joinToString(",\n\t")}
                |)
                """.trimMargin()
            }

            """
            |"${violation.name}" -> {
            |    ${instantiation.replace("\n", "\n\t")}
            |}
            """.trimMargin()
        }

        return """
            |@Suppress("UNCHECKED_CAST")
            |@OptIn(InternalValidationApi::class)
            |private fun ${validatorTypeParams}ConstraintDescriptor<$subjectFqName>.toViolation(
            |    item: kotlin.Lazy<$subjectFqName>
            |): $violationsParent {
            |    return when (this.violation) {
            |    
            |        ${violationCases.joinToString("\n\n").replace("\n", "\n\t\t")}
            |        
            |        else -> {
            |            throw kotlin.IllegalArgumentException(
            |                "`${"$"}{this.violation}` is not a recognizable violation"
            |            )
            |        }
            |    }
            |}
        """.trimMargin()
    }
}
