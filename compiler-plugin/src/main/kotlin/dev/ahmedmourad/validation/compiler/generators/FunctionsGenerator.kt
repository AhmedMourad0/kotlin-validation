package dev.ahmedmourad.validation.compiler.generators

import dev.ahmedmourad.validation.compiler.descriptors.ConstraintsDescriptor
import dev.ahmedmourad.validation.compiler.utils.*
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_CASE
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_CONSTRAINT
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_ILLEGAL_FUN
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_LEGAL_FUN
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_VALIDATION
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_VALIDATOR
import dev.ahmedmourad.validation.compiler.verifier.DslVerifier
import org.jetbrains.kotlin.descriptors.ValueParameterDescriptor
import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.psi.KtClass

internal class FunctionsGenerator(
    private val verifier: DslVerifier
) {

    internal val imports: List<String> = listOf(
        FQ_NAME_CASE,
        FQ_NAME_LEGAL_FUN,
        FQ_NAME_ILLEGAL_FUN,
        FQ_NAME_CONSTRAINT,
        FQ_NAME_VALIDATION,
        FQ_NAME_VALIDATOR,
        FQ_NAME_CONSTRAINTS_DESCRIPTOR
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

        return listOfNotNull(
            generateIsValid(constraintsDescriptor, primaryConstructor.valueParameters),
            generateValidate(constraintsDescriptor, primaryConstructor.valueParameters),
            generateFindViolatedConstraints(constraintsDescriptor, primaryConstructor.valueParameters),
            generateToViolation(constraintsDescriptor),
            generateCreate(constraintsDescriptor)
        )
    }

    private fun generateIsValid(
        constraintsDescriptor: ConstraintsDescriptor,
        constrainedParams: List<ValueParameterDescriptor>
    ): String {

        val typeArgs = constraintsDescriptor.constrainerClassOrObject
            .typeParameters
            .map { it.nameAsSafeName.asString() }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(", ")
            ?.let { "<$it>" }
            .orEmpty()

        val typeParams = constraintsDescriptor.constrainerClassOrObject
            .typeParameterList
            ?.text
            ?.plus(" ")
            .orEmpty()

        val constrainedFqName = constraintsDescriptor.constrainedType
            .getJetTypeFqName(false)
            .plus(typeArgs)

        val constrainerFqName = constraintsDescriptor.constrainerClassOrObject
            .fqName
            ?.asString()
            ?.plus(typeArgs)

        val params = constrainedParams.joinToString(",\n\t") { param ->
            param.name.asString() + ": " + param.type.getJetTypeFqName(true)
        }

        val arguments = constrainedParams.joinToString(",\n") { param ->
            param.name.asString()
        }

        return """
            |fun $typeParams$constrainerFqName.isValid(
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

        val typeArgs = constraintsDescriptor.constrainerClassOrObject
            .typeParameters
            .map { it.nameAsSafeName.asString() }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(", ")
            ?.let { "<$it>" }
            .orEmpty()

        val typeParams = constraintsDescriptor.constrainerClassOrObject
            .typeParameterList
            ?.text
            ?.plus(" ")
            .orEmpty()

        val constrainedFqName = constraintsDescriptor.constrainedType
            .getJetTypeFqName(false)
            .plus(typeArgs)

        val constrainerFqName = constraintsDescriptor.constrainerClassOrObject
            .fqName
            ?.asString()
            ?.plus(typeArgs)

        val violationsParent = constraintsDescriptor.violationsParentName

        val params = constrainedParams.joinToString(",\n\t") { param ->
            param.name.asString() + ": " + param.type.getJetTypeFqName(true)
        }

        val arguments = constrainedParams.joinToString(",\n\t\t\t") { param ->
            param.name.asString()
        }

        return """
            |fun $typeParams$constrainerFqName.validate(
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
    ): String? {

        val typeArgs = constraintsDescriptor.constrainerClassOrObject
            .typeParameters
            .map { it.nameAsSafeName.asString() }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(", ")
            ?.let { "<$it>" }
            .orEmpty()

        val typeParams = constraintsDescriptor.constrainerClassOrObject
            .typeParameterList
            ?.text
            ?.plus(" ")
            .orEmpty()

        val constrainedFqName = constraintsDescriptor.constrainedType
            .getJetTypeFqName(false)
            .plus(typeArgs)

        val constrainerFqName = constraintsDescriptor.constrainerClassOrObject
            .fqName
            ?.asString()
            ?.plus(typeArgs)

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

        val constraints = when (constraintsDescriptor.constrainerClassOrObject) {
            is KtObjectDeclaration -> "this.constraints"
            is KtClass -> "this.create()"
            else -> return verifier.reportError(
                "Only objects and regular classes with companion objects can implement Constrains",
                constraintsDescriptor.constrainerClassOrObject
            )
        }

        return """
            |private fun $typeParams$constrainerFqName.findViolatedConstraints(
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
            |    return $constraints.filterNot { constraint ->
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

        val typeArgs = constraintsDescriptor.constrainerClassOrObject
            .typeParameters
            .map { it.nameAsSafeName.asString() }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(", ")
            ?.let { "<$it>" }
            .orEmpty()

        val typeParams = constraintsDescriptor.constrainerClassOrObject
            .typeParameterList
            ?.text
            ?.plus(" ")
            .orEmpty()

        val constrainedFqName = constraintsDescriptor.constrainedType
            .getJetTypeFqName(false)
            .plus(typeArgs)

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
            |private fun ${typeParams}Constraint<$constrainedFqName>.toViolation(
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

    //TODO: pass extra parameters through constructors
    private fun generateCreate(
        constraintsDescriptor: ConstraintsDescriptor
    ): String? {

        if (constraintsDescriptor.constrainerClassOrObject is KtObjectDeclaration) {
            return null
        }

        val typeArgs = constraintsDescriptor.constrainerClassOrObject
            .typeParameters
            .map { it.nameAsSafeName.asString() }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(", ")
            ?.let { "<$it>" }
            .orEmpty()

        val typeParams = constraintsDescriptor.constrainerClassOrObject
            .typeParameterList
            ?.text
            ?.plus(" ")
            .orEmpty()

        val constrainedFqName = constraintsDescriptor.constrainedType
            .getJetTypeFqName(false)
            .plus(typeArgs)

        val constrainerFqName = constraintsDescriptor.constrainerClassOrObject
            .fqName
            ?.asString()
            ?.plus(typeArgs)

        return """
            |private fun ${typeParams}$constrainerFqName.create(): ConstraintsDescriptor<$constrainedFqName> {
            |    return $constrainerFqName().constraints
            |}
        """.trimMargin()
    }
}
