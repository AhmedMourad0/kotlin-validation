package dev.ahmedmourad.validation.compiler.codegen.validations

import dev.ahmedmourad.validation.compiler.codegen.CodeSectionGenerator
import dev.ahmedmourad.validation.compiler.descriptors.ConstraintsDescriptor
import dev.ahmedmourad.validation.compiler.descriptors.MetaDescriptor
import dev.ahmedmourad.validation.compiler.utils.*
import dev.ahmedmourad.validation.compiler.utils.fqNameCase
import dev.ahmedmourad.validation.compiler.utils.fqNameConstraint
import dev.ahmedmourad.validation.compiler.utils.fqNameIllegalFun
import dev.ahmedmourad.validation.compiler.utils.fqNameLegalFun
import dev.ahmedmourad.validation.compiler.utils.fqNameValidation

//TODO: only generate copy and factory when annotated
//TODO: inline??
internal class FunctionsGenerator : CodeSectionGenerator {

    override fun imports(constraintsDescriptor: ConstraintsDescriptor) = setOf(
        fqNameCase.asString(),
        fqNameLegalFun.asString(),
        fqNameIllegalFun.asString(),
        fqNameSwapFun.asString(),
        fqNameOrElseFun.asString(),
        fqNameConstraint.asString(),
        fqNameValidation.asString(),
        fqNameConstraintsDescriptor.asString(),
        fqNameIncludedConstraints.asString(),
        fqNameInternalValidationApi.asString()
    ) + constraintsDescriptor.violations.flatMap { violation ->
        violation.metas.flatMap { meta ->
            meta.includedConstraint
                ?.let { listOf(it.isValidFqName, it.validateFqName) }
                .orEmpty()
        }
    }

    override fun generate(
        constraintsDescriptor: ConstraintsDescriptor
    ): Set<String> {
        return setOfNotNull(
            generateFactory(constraintsDescriptor = constraintsDescriptor),
            generateIsValid(constraintsDescriptor = constraintsDescriptor),
            generateValidate(constraintsDescriptor = constraintsDescriptor),
            generateFindViolatedConstraints(constraintsDescriptor = constraintsDescriptor),
            generateValidateNestedConstraints(constraintsDescriptor = constraintsDescriptor),
            generateToViolation(constraintsDescriptor = constraintsDescriptor)
        )
    }

    private fun generateIsValid(constraintsDescriptor: ConstraintsDescriptor): String {

        val constrainedFqName = constraintsDescriptor.constrainedFqName

        val constrainerFqName = constraintsDescriptor.constrainerFqName
        val constrainerTypeParams = constraintsDescriptor.constrainerTypeParams
        val constrainerTypeParamsAsTypeArgs = constraintsDescriptor.constrainerTypeParamsAsTypeArgs

        val validationContext = constraintsDescriptor.validationContextName
        val validationContextImpl = constraintsDescriptor.validationContextImplName

        val validationContextInstantiation = if (constraintsDescriptor.isValidationContextImplAnObject) {
            validationContextImpl
        } else {
            "$validationContextImpl()"
        }

        return """
            |fun $constrainerTypeParams$constrainerFqName.isValid(
            |    createItem: $validationContext$constrainerTypeParamsAsTypeArgs.() -> $constrainedFqName
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

    private fun generateValidate(constraintsDescriptor: ConstraintsDescriptor): String {

        val constrainedFqName = constraintsDescriptor.constrainedFqName

        val constrainerFqName = constraintsDescriptor.constrainerFqName
        val constrainerTypeParams = constraintsDescriptor.constrainerTypeParams
        val constrainerTypeParamsAsTypeArgs = constraintsDescriptor.constrainerTypeParamsAsTypeArgs

        val violationsParent = constraintsDescriptor.violationsParentName
        val validationContext = constraintsDescriptor.validationContextName
        val validationContextImpl = constraintsDescriptor.validationContextImplName

        val validationContextInstantiation = if (constraintsDescriptor.isValidationContextImplAnObject) {
            validationContextImpl
        } else {
            "$validationContextImpl()"
        }

        return """
            |fun $constrainerTypeParams$constrainerFqName.validate(
            |    createItem: $validationContext$constrainerTypeParamsAsTypeArgs.() -> $constrainedFqName
            |): Case<Set<$violationsParent>, $constrainedFqName> {
            |
            |    val item = lazy {
            |        createItem($validationContextInstantiation)
            |    }
            |    
            |    return findViolatedConstraints(item).map { it.toViolation$constrainerTypeParamsAsTypeArgs(item) }
            |        .toSet()
            |        .takeIf(Set<$violationsParent>::isNotEmpty)
            |        ?.illegal() ?: item.value.legal()
            |}
        """.trimMargin()
    }

    //TODO: generate a factory for each constructor (be careful with their visibility)
    //TODO: only generate factories for @MustBeValid annotated classes that live in the same module
    private fun generateFactory(constraintsDescriptor: ConstraintsDescriptor): String? {

        val constrainedParams = constraintsDescriptor.constrainedParams ?: return null
        val constrainedFqName = constraintsDescriptor.constrainedFqName
        val constrainedSimpleName = constraintsDescriptor.constrainedType.simpleName()

        val constrainerTypeParams = constraintsDescriptor.constrainerTypeParams
        val constrainerTypeParamsAsTypeArgs = constraintsDescriptor.constrainerTypeParamsAsTypeArgs

        val validationContext = constraintsDescriptor.validationContextName

        val params = constrainedParams.joinToString(",\n\t") { param ->
            param.name.asString() + ": " + param.type.deepFqName()
        }

        val itemArgs = constrainedParams.joinToString(",\n\t\t") { param ->
            param.name.asString()
        }

        return """
            |fun $constrainerTypeParams$validationContext$constrainerTypeParamsAsTypeArgs.$constrainedSimpleName(
            |    $params
            |): $constrainedFqName {
            |    return $constrainedFqName(
            |        $itemArgs
            |    )
            |}
        """.trimMargin()
    }

    private fun generateCopy(constraintsDescriptor: ConstraintsDescriptor): String? {
        TODO("copy")
    }

    private fun generateFindViolatedConstraints(constraintsDescriptor: ConstraintsDescriptor): String {

        val constrainedFqName = constraintsDescriptor.constrainedFqName
        val constrainerFqName = constraintsDescriptor.constrainerFqName
        val constrainerTypeParams = constraintsDescriptor.constrainerTypeParams
        val constrainerTypeParamsAsTypeArgs = constraintsDescriptor.constrainerTypeParamsAsTypeArgs

        return """
            |private fun $constrainerTypeParams$constrainerFqName.findViolatedConstraints(
            |    item: kotlin.Lazy<$constrainedFqName>
            |): Set<Constraint<$constrainedFqName>> {
            |    return this.constraints.filterNot { constraint ->
            |        constraint.validations.all { validation ->
            |            validation.validate(item.value)
            |        } && constraint.validateNestedConstraints$constrainerTypeParamsAsTypeArgs(item)
            |    }.toSet()
            |}
        """.trimMargin()
    }

    private fun generateValidateNestedConstraints(constraintsDescriptor: ConstraintsDescriptor): String {

        val constrainedFqName = constraintsDescriptor.constrainedFqName

        val constrainerTypeParams = constraintsDescriptor.constrainerTypeParams

        val violationCases = constraintsDescriptor.violations.map { violation ->

            val instantiation = if (violation.inclusionMetas.isEmpty()) {
                "true"
            } else {

                val inclusionParamsVals = violation.inclusionMetas.mapIndexed { index, param ->

                    val includedConstrainedFqName = param.includedConstraint!!.constrainedFqName
                    val includedConstrainerFqName = param.includedConstraint.constrainerFqName

                    val includedConstraintTypeArgs =
                        "<$constrainedFqName, $includedConstrainedFqName, $includedConstrainerFqName>"
                    val valName = "${param.name}Descriptor"

                    """
                    |val $valName = this.includedConstraints[$index] as ${fqNameIncludedConstraints.asString()}$includedConstraintTypeArgs
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
            |private fun ${constrainerTypeParams}Constraint<$constrainedFqName>.validateNestedConstraints(
            |    item: kotlin.Lazy<$constrainedFqName>
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
        constraintsDescriptor: ConstraintsDescriptor
    ): String {

        val constrainedFqName = constraintsDescriptor.constrainedFqName

        val constrainerTypeParams = constraintsDescriptor.constrainerTypeParams

        val violationsParent = constraintsDescriptor.violationsParentName

        val violationCases = constraintsDescriptor.violations.map { violation ->

            val instantiation = if (violation.metas.isEmpty()) {
                "$violationsParent.${violation.name}"
            } else {

                val regularArgs = violation.regularMetas.mapIndexed { index, arg ->
                    "${arg.name} = this.metadata[$index].get(item.value) as ${arg.typeFqName}"
                }

                val inclusionArgsList = violation.inclusionMetas
                    .takeIf(List<MetaDescriptor>::isNotEmpty)

                val inclusionArgsVals = inclusionArgsList?.mapIndexed { index, arg ->

                    val includedConstrainedFqName = arg.includedConstraint!!.constrainedFqName
                    val includedConstrainerFqName = arg.includedConstraint.constrainerFqName

                    val includedConstraintTypeArgs =
                        "<$constrainedFqName, $includedConstrainedFqName, $includedConstrainerFqName>"

                    val valName = "${arg.name}Descriptor"

                    """
                    |val $valName = this.includedConstraints[$index] as ${fqNameIncludedConstraints.asString()}$includedConstraintTypeArgs
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
            |private fun ${constrainerTypeParams}Constraint<$constrainedFqName>.toViolation(
            |    item: kotlin.Lazy<$constrainedFqName>
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
