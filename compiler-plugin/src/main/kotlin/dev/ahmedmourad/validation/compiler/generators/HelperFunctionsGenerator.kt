package dev.ahmedmourad.validation.compiler.generators

import dev.ahmedmourad.validation.compiler.descriptors.ConstraintsDescriptor
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_CASE
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_LEGAL_FUN
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_ILLEGAL_FUN
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_CONSTRAINT
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_VALIDATION
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_VALIDATOR
import dev.ahmedmourad.validation.compiler.verifier.DslVerifier
import org.jetbrains.kotlin.descriptors.ValueParameterDescriptor
import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName

internal class HelperFunctionsGenerator(
    private val verifier: DslVerifier
) {

    internal val imports: List<String> = listOf(
        FQ_NAME_CASE,
        FQ_NAME_LEGAL_FUN,
        FQ_NAME_ILLEGAL_FUN,
        FQ_NAME_CONSTRAINT,
        FQ_NAME_VALIDATION,
        FQ_NAME_VALIDATOR
    )

    internal fun generate(
        constraintsDescriptor: ConstraintsDescriptor
    ): List<String>? {

        val constrainedClass = verifier.verifyConstrainedClassType(
            constraintsDescriptor.constrainedType,
            constraintsDescriptor.constrainedTypePsi
        ) ?: return null

        val primaryConstructor = verifier.verifyConstrainedClassHasPrimaryConstructor(
            constrainedClass,
            constraintsDescriptor.constrainedTypePsi
        ) ?: return null

        return listOf(
            generateIsValid(constraintsDescriptor, primaryConstructor.valueParameters),
            generateValidate(constraintsDescriptor, primaryConstructor.valueParameters),
            generateFindViolatedConstraints(constraintsDescriptor, primaryConstructor.valueParameters),
            generateToViolation(constraintsDescriptor)
        )
    }

    private fun generateIsValid(
        constraintsDescriptor: ConstraintsDescriptor,
        constrainedParams: List<ValueParameterDescriptor>
    ): String {

        val constrainedFqName = constraintsDescriptor.constrainedType.getJetTypeFqName(true)
        val constrainerFqName = constraintsDescriptor.constrainerObject.fqName?.asString()

        val params = constrainedParams.joinToString(",\n\t") { param ->
            param.name.asString() + ": " + param.type.getJetTypeFqName(true)
        }

        val arguments = constrainedParams.joinToString(",\n") { param ->
            param.name.asString()
        }

        return """
            |fun $constrainerFqName.isValid(
            |    $params
            |): Boolean {
            |    return findViolatedConstraints(
            |        lazy {
            |            $constrainedFqName(
            |                ${arguments.replace("\n", "\n\t\t\t\t")}
            |            )
            |        }, ${arguments.replace("\n", "\n\t\t")}
            |    ).isNotEmpty()
            |}
        """.trimMargin()
    }

    private fun generateValidate(
        constraintsDescriptor: ConstraintsDescriptor,
        constrainedParams: List<ValueParameterDescriptor>
    ): String {

        val constrainedFqName = constraintsDescriptor.constrainedType.getJetTypeFqName(true)
        val constrainerFqName = constraintsDescriptor.constrainerObject.fqName?.asString()
        val violationsParent = constraintsDescriptor.violationsParentName

        val params = constrainedParams.joinToString(",\n\t") { param ->
            param.name.asString() + ": " + param.type.getJetTypeFqName(true)
        }

        val arguments = constrainedParams.joinToString(",\n\t\t\t") { param ->
            param.name.asString()
        }

        return """
            |fun $constrainerFqName.validate(
            |    $params
            |): Case<List<$violationsParent>, $constrainedFqName> {
            |    val item = lazy {
            |        $constrainedFqName(
            |            $arguments
            |        )
            |    }
            |    return findViolatedConstraints(item, v)
            |        .map { it.toViolation(item) }
            |        .takeIf(List<$violationsParent>::isNotEmpty)
            |        ?.illegal() ?: item.value.legal()
            |}
        """.trimMargin()
    }

    private fun generateFindViolatedConstraints(
        constraintsDescriptor: ConstraintsDescriptor,
        constrainedParams: List<ValueParameterDescriptor>
    ): String {

        val constrainedFqName = constraintsDescriptor.constrainedType.getJetTypeFqName(true)
        val constrainerFqName = constraintsDescriptor.constrainerObject.fqName?.asString()

        val params = constrainedParams.joinToString(",\n\t") { param ->
            param.name.asString() + ": " + param.type.getJetTypeFqName(true)
        }

        val validationCases = constrainedParams.joinToString("\n\n") { param ->
            """
                |"${param.name.asString()}" -> { validation ->
                |    (validation as Validation<${param.type.getJetTypeFqName(true)}>).validate(${param.name.asString()})
                |}
            """.trimMargin()
        }

        return """
            |private fun $constrainerFqName.findViolatedConstraints(
            |    item: kotlin.Lazy<$constrainedFqName>,
            |    $params
            |): List<Constraint<$constrainedFqName>> {
            |
            |    fun Validator<$constrainedFqName, *>.preparedValidation(): (Validation<*>) -> kotlin.Boolean {
            |        return when (val propertyName = this.property.name) {
            |            
            |            ${validationCases.replace("\n", "\n\t\t\t")}
            |            
            |            else -> {
            |               throw kotlin.IllegalArgumentException(
            |                   "`${"$"}propertyName` is not a property of this class"
            |               )
            |            }
            |        }
            |    }
            |
            |    return this.constraints.filterNot { constraint ->
            |        constraint.validations.all { validation ->
            |            validation.validate(item.value)
            |        } && constraint.validators.all { validator ->
            |            val preparedValidation = validator.preparedValidation()
            |            validator.validations.all { validation ->
            |                preparedValidation.invoke(validation)
            |            }
            |        }
            |    }
            |}
        """.trimMargin()
    }

    private fun generateToViolation(
        constraintsDescriptor: ConstraintsDescriptor
    ): String {

        val constrainedFqName = constraintsDescriptor.constrainedType.getJetTypeFqName(true)
        val violationsParent = constraintsDescriptor.violationsParentName

        val violationCases = constraintsDescriptor.violations.map { violation ->

            val instantiation = if (violation.params.isEmpty()) {
                "$violationsParent.${violation.name}"
            } else {
                val params = violation.params.mapIndexed { index, param ->
                    "this.params[$index].get(item.value) as ${param.type.getJetTypeFqName(true)}"
                }.joinToString(",\n\t")
                """
                |$violationsParent.${violation.name}(
                |    $params
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
            |private fun Constraint<$constrainedFqName>.toViolation(
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
