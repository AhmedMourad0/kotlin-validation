package dev.ahmedmourad.validation.compiler.generators

import dev.ahmedmourad.validation.compiler.descriptors.ConstraintsDescriptor
import dev.ahmedmourad.validation.compiler.descriptors.ParamDescriptor
import dev.ahmedmourad.validation.compiler.utils.*
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_CASE
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_CONSTRAINT
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_ILLEGAL_FUN
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_LEGAL_FUN
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_VALIDATION

//TODO: only generate copy and factory when annotated
//TODO: inline??
internal class FunctionsGenerator : Generator {

    override fun imports(constraintsDescriptor: ConstraintsDescriptor) = listOf(
        FQ_NAME_CASE,
        FQ_NAME_LEGAL_FUN,
        FQ_NAME_ILLEGAL_FUN,
        FQ_NAME_SWAP_FUN,
        FQ_NAME_OR_ELSE_FUN,
        FQ_NAME_CONSTRAINT,
        FQ_NAME_VALIDATION,
        FQ_NAME_CONSTRAINTS_DESCRIPTOR,
        FQ_NAME_INCLUDED_CONSTRAINTS,
        FQ_NAME_INTERNAL_VALIDATION_API_ANNOTATION
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
        return listOfNotNull(
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
            |): Case<List<$violationsParent>, $constrainedFqName> {
            |
            |    val item = lazy {
            |        createItem($validationContextInstantiation)
            |    }
            |    
            |    return findViolatedConstraints(item).map { it.toViolation$constrainerTypeParamsAsTypeArgs(item) }
            |        .takeIf(List<$violationsParent>::isNotEmpty)
            |        ?.illegal() ?: item.value.legal()
            |}
        """.trimMargin()
    }

    //TODO: generate a factory for each constructor (be careful with their visibility)
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
            |): List<Constraint<$constrainedFqName>> {
            |    return this.constraints.filterNot { constraint ->
            |        constraint.validations.all { validation ->
            |            validation.validate(item.value)
            |        } && constraint.validateNestedConstraints$constrainerTypeParamsAsTypeArgs(item)
            |    }
            |}
        """.trimMargin()
    }

    private fun generateValidateNestedConstraints(constraintsDescriptor: ConstraintsDescriptor): String {

        val constrainedFqName = constraintsDescriptor.constrainedFqName

        val constrainerTypeParams = constraintsDescriptor.constrainerTypeParams

        val violationCases = constraintsDescriptor.violations.map { violation ->

            val instantiation = if (violation.inclusionParams.isEmpty()) {
                "true"
            } else {

                val inclusionParamsVals = violation.inclusionParams.mapIndexed { index, param ->

                    val includedConstrainedFqName = param.includedConstraint!!.constrainedFqName
                    val includedConstrainerFqName = param.includedConstraint.constrainerFqName

                    val includedConstraintTypeArgs =
                        "<$constrainedFqName, $includedConstrainedFqName, $includedConstrainerFqName>"
                    val valName = "${param.name}Descriptor"

                    """
                    |val $valName = this.includedConstraints[$index] as $FQ_NAME_INCLUDED_CONSTRAINTS$includedConstraintTypeArgs
                    |val ${param.name} = $valName.isValid(item.value) {
                    |    this.isValid { it }
                    |}
                    """.trimMargin()
                }.joinToString(separator = "\n\n")

                """
                |
                |$inclusionParamsVals
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
            |@UseExperimental(InternalValidationApi::class)
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

    private fun generateToViolation(constraintsDescriptor: ConstraintsDescriptor): String {

        val constrainedFqName = constraintsDescriptor.constrainedFqName

        val constrainerTypeParams = constraintsDescriptor.constrainerTypeParams

        val violationsParent = constraintsDescriptor.violationsParentName

        val violationCases = constraintsDescriptor.violations.map { violation ->

            val instantiation = if (violation.params.isEmpty()) {
                "$violationsParent.${violation.name}"
            } else {

                val regularParams = violation.regularParams.mapIndexed { index, param ->
                    "${param.name} = this.params[$index].get(item.value) as ${param.typeFqName}"
                }

                val inclusionParamsList = violation.inclusionParams
                    .takeIf(List<ParamDescriptor>::isNotEmpty)

                val inclusionParamsVals = inclusionParamsList?.mapIndexed { index, param ->

                    val includedConstrainedFqName = param.includedConstraint!!.constrainedFqName
                    val includedConstrainerFqName = param.includedConstraint.constrainerFqName

                    val includedConstraintTypeArgs =
                        "<$constrainedFqName, $includedConstrainedFqName, $includedConstrainerFqName>"

                    val valName = "${param.name}Descriptor"

                    """
                    |val $valName = this.includedConstraints[$index] as $FQ_NAME_INCLUDED_CONSTRAINTS$includedConstraintTypeArgs
                    |val ${param.name} = $valName.findViolations(item.value) {
                    |    this.validate { it }
                    |}
                    """.trimMargin()
                }?.joinToString(separator = "\n\n", prefix = "\n", postfix = "\n").orEmpty()

                val inclusionParams = inclusionParamsList?.map { param ->
                    "${param.name} = ${param.name}"
                }.orEmpty()

                """
                |$inclusionParamsVals
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
            |@UseExperimental(InternalValidationApi::class)
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
