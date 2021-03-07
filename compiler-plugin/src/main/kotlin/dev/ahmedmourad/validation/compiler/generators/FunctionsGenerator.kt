package dev.ahmedmourad.validation.compiler.generators

import dev.ahmedmourad.validation.compiler.descriptors.ConstraintsDescriptor
import dev.ahmedmourad.validation.compiler.descriptors.Param
import dev.ahmedmourad.validation.compiler.utils.*
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_CASE
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_CONSTRAINT
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_ILLEGAL_FUN
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_LEGAL_FUN
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_VALIDATION
import dev.ahmedmourad.validation.compiler.verifier.DslVerifier
import org.jetbrains.kotlin.resolve.BindingContext

private data class SimpleParam(val name: String, val typeFqName: String)

internal class FunctionsGenerator(
    private val verifier: DslVerifier,
    private val bindingContext: BindingContext
) : Generator {

    override fun imports(constraintsDescriptor: ConstraintsDescriptor) = listOf(
        FQ_NAME_CASE,
        FQ_NAME_LEGAL_FUN,
        FQ_NAME_ILLEGAL_FUN,
        FQ_NAME_SWAP_FUN,
        FQ_NAME_OR_ELSE_FUN,
        FQ_NAME_CONSTRAINT,
        FQ_NAME_VALIDATION,
        FQ_NAME_CONSTRAINTS_DESCRIPTOR,
        FQ_NAME_INCLUDED_CONSTRAINTS
    ) + constraintsDescriptor.violations.flatMap { violation ->
        violation.params.flatMap { param ->
            param.includedConstraint
                ?.let { listOf(it.isValidFqName, it.validateFqName) }
                .orEmpty()
        }
    }

    override fun generate(
        constraintsDescriptor: ConstraintsDescriptor
    ): List<String> {

        val constrainedClass = constraintsDescriptor.constrainedClass ?: return emptyList()

        val constrainedParams = verifier.verifyConstrainedClassHasPrimaryConstructor(
            constrainedClass,
            constraintsDescriptor.constrainedTypePsi
        )?.valueParameters?.map {
            SimpleParam(it.name.asString(), it.type.deepFqName())
        } ?: return emptyList()

        val constrainedFqName = constraintsDescriptor.constrainedFqName
        val constrainerFqName = constraintsDescriptor.constrainerFqName
        val constrainerTypeParams = constraintsDescriptor.constrainerTypeParams

        return listOfNotNull(
            generateFactory(
                constraintsDescriptor = constraintsDescriptor,
                constrainedFqName = constrainedFqName,
                constrainedParams = constrainedParams
            ), generateIsValid(
                constraintsDescriptor = constraintsDescriptor,
                constrainedFqName = constrainedFqName,
                constrainerFqName = constrainerFqName,
                constrainerTypeParams = constrainerTypeParams
            ), generateValidate(
                constraintsDescriptor = constraintsDescriptor,
                constrainedFqName = constrainedFqName,
                constrainerFqName = constrainerFqName,
                constrainerTypeParams = constrainerTypeParams
            ), generateFindViolatedConstraints(
                constrainedFqName = constrainedFqName,
                constrainerFqName = constrainerFqName,
                constrainerTypeParams = constrainerTypeParams
            ), generateValidateNestedConstraints(
                constraintsDescriptor = constraintsDescriptor,
                constrainedFqName = constrainedFqName
            ), generateToViolation(
                constraintsDescriptor = constraintsDescriptor,
                constrainedFqName = constrainedFqName
            )
        )
    }

    private fun generateIsValid(
        constraintsDescriptor: ConstraintsDescriptor,
        constrainedFqName: String,
        constrainerFqName: String?,
        constrainerTypeParams: String
    ): String {

        val validationContext = constraintsDescriptor.validationContextName
        val validationContextImpl = constraintsDescriptor.validationContextImplName

        val validationContextInstantiation = if (constraintsDescriptor.isValidationContextImplAClass) {
            "$validationContextImpl()"
        } else {
            validationContextImpl
        }

        return """
            |fun $constrainerTypeParams$constrainerFqName.isValid(
            |    createItem: $validationContext${constraintsDescriptor.constrainerTypeParamsAsTypeArgs}.() -> $constrainedFqName
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

    private fun generateValidate(
        constraintsDescriptor: ConstraintsDescriptor,
        constrainedFqName: String,
        constrainerFqName: String?,
        constrainerTypeParams: String
    ): String {

        val violationsParent = constraintsDescriptor.violationsParentName
        val validationContext = constraintsDescriptor.validationContextName
        val validationContextImpl = constraintsDescriptor.validationContextImplName

        val validationContextInstantiation = if (constraintsDescriptor.isValidationContextImplAClass) {
            "$validationContextImpl()"
        } else {
            validationContextImpl
        }

        return """
            |fun $constrainerTypeParams$constrainerFqName.validate(
            |    createItem: $validationContext${constraintsDescriptor.constrainerTypeParamsAsTypeArgs}.() -> $constrainedFqName
            |): Case<List<$violationsParent>, $constrainedFqName> {
            |
            |    val item = lazy {
            |        createItem($validationContextInstantiation)
            |    }
            |    
            |    return findViolatedConstraints(item).map { it.toViolation(item) }
            |        .takeIf(List<$violationsParent>::isNotEmpty)
            |        ?.illegal() ?: item.value.legal()
            |}
        """.trimMargin()
    }

    private fun generateFactory(
        constraintsDescriptor: ConstraintsDescriptor,
        constrainedFqName: String,
        constrainedParams: List<SimpleParam>
    ): String {

        val params = constrainedParams.joinToString(",\n\t") { param ->
            param.name + ": " + param.typeFqName
        }

        val itemArgs = constrainedParams.joinToString(",\n\t\t") { param ->
            param.name
        }

        return """
            |fun ${constraintsDescriptor.constrainerTypeParams.takeIf { it.isNotBlank() }?.plus(" ").orEmpty()}${constraintsDescriptor.validationContextName}${constraintsDescriptor.constrainerTypeParamsAsTypeArgs}.${constraintsDescriptor.constrainedType.simpleName()}(
            |    $params
            |): $constrainedFqName {
            |    return $constrainedFqName(
            |        $itemArgs
            |    )
            |}
        """.trimMargin()
    }

    private fun generateFindViolatedConstraints(
        constrainedFqName: String,
        constrainerFqName: String?,
        constrainerTypeParams: String
    ): String {
        return """
            |private fun $constrainerTypeParams$constrainerFqName.findViolatedConstraints(
            |    item: kotlin.Lazy<$constrainedFqName>
            |): List<Constraint<$constrainedFqName>> {
            |    return this.constraints.filterNot { constraint ->
            |        constraint.validations.all { validation ->
            |            validation.validate(item.value)
            |        } && constraint.validateNestedConstraints(item)
            |    }
            |}
        """.trimMargin()
    }

    private fun generateValidateNestedConstraints(
        constraintsDescriptor: ConstraintsDescriptor,
        constrainedFqName: String
    ): String {

        val violationCases = constraintsDescriptor.violations.map { violation ->

            val instantiation = if (violation.inclusionParams.isEmpty()) {
                "true"
            } else {

                val inclusionParamsVars = violation.inclusionParams.mapIndexed { index, param ->
                    """
                    |val ${param.name}Descriptor = this.includedConstraints[$index] as $FQ_NAME_INCLUDED_CONSTRAINTS<$constrainedFqName, ${param.includedConstraint!!.constrainedType.deepFqName()}, ${param.includedConstraint.constrainerType.deepFqName()}>
                    |val ${param.name} = ${param.name}Descriptor.isValid(item.value) {
                    |    this.isValid { it }
                    |}
                    """.trimMargin()
                }.joinToString(separator = "\n\n")

                """
                |
                |$inclusionParamsVars
                |
                |${violation.inclusionParams.joinToString(" && ") { it.name }}
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
            |private fun ${constraintsDescriptor.constrainerTypeParamsForConstrained.takeIf(String::isNotBlank)?.let{ "$it " }.orEmpty()}Constraint<$constrainedFqName>.validateNestedConstraints(
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
        constraintsDescriptor: ConstraintsDescriptor,
        constrainedFqName: String
    ): String {

        val violationsParent = constraintsDescriptor.violationsParentName

        val violationCases = constraintsDescriptor.violations.map { violation ->

            val instantiation = if (violation.params.isEmpty()) {
                "$violationsParent.${violation.name}"
            } else {

                val regularParams = violation.regularParams
                    .mapIndexed { index, param ->
                        "${param.name} = this.params[$index].get(item.value) as ${param.typeFqName}"
                    }

                val inclusionParamsList = violation.inclusionParams
                    .takeIf(List<Param>::isNotEmpty)

                val inclusionParamsVars = inclusionParamsList?.mapIndexed { index, param ->
                    """
                    |val ${param.name}Descriptor = this.includedConstraints[$index] as $FQ_NAME_INCLUDED_CONSTRAINTS<$constrainedFqName, ${param.includedConstraint!!.constrainedType.deepFqName()}, ${param.includedConstraint.constrainerType.deepFqName()}>
                    |val ${param.name} = ${param.name}Descriptor.findViolations(item.value) {
                    |    this.validate { it }
                    |}
                    """.trimMargin()
                }?.joinToString(separator = "\n\n", prefix = "\n", postfix = "\n").orEmpty()

                val inclusionParams = inclusionParamsList?.map { param ->
                    "${param.name} = ${param.name}"
                }.orEmpty()

                """
                |$inclusionParamsVars
                |$violationsParent.${violation.name}(
                |    ${(regularParams + inclusionParams).joinToString(",\n\t")}
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
            |private fun ${constraintsDescriptor.constrainerTypeParamsForConstrained.takeIf(String::isNotBlank)?.let{ "$it " }.orEmpty()}Constraint<$constrainedFqName>.toViolation(
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
