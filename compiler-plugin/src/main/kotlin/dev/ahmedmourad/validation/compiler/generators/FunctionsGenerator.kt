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
import org.jetbrains.kotlin.codegen.kotlinType
import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.bindingContextUtil.getAbbreviatedTypeOrType
import org.jetbrains.kotlin.types.KotlinType

private data class Param(val name: String, val type: KotlinType)

internal class FunctionsGenerator(
    private val verifier: DslVerifier,
    private val bindingContext: BindingContext
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

        val constrainedParams = verifier.verifyConstrainedClassHasPrimaryConstructor(
            constrainedClass,
            constraintsDescriptor.constrainedTypePsi
        )?.valueParameters ?.map {
            Param(it.name.asString(), it.type)
        } ?: return null
        
        val constrainerParams = (constraintsDescriptor.constrainerClassOrObject as? KtClass)?.primaryConstructor
            ?.valueParameters
            ?.mapNotNull { param ->
                param.typeReference
                    ?.typeElement
                    ?.getAbbreviatedTypeOrType(bindingContext)
                    ?.let { Param(param.nameAsSafeName.asString(), it) }
            }.orEmpty()

        return listOfNotNull(
            generateIsValid(constraintsDescriptor, constrainedParams, constrainerParams),
            generateValidate(constraintsDescriptor, constrainedParams, constrainerParams),
            generateFindViolatedConstraints(constraintsDescriptor, constrainedParams, constrainerParams),
            generateToViolation(constraintsDescriptor),
            generateCreate(constraintsDescriptor, constrainerParams)
        )
    }

    private fun generateIsValid(
        constraintsDescriptor: ConstraintsDescriptor,
        constrainedParams: List<Param>,
        constrainerParams: List<Param>
    ): String {

        val typeArgs = constraintsDescriptor.constrainerClassOrObject
            .typeParameters
            .map { it.nameAsSafeName.asString() }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(", ")
            ?.let { "<$it>" }
            .orEmpty()

        val typeParams = constraintsDescriptor.constrainerClassOrObject
            .typeParameters
            .map { it.deepFqName(bindingContext) }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(separator = ", ", prefix = "<", postfix = ">")
            ?.plus(" ")
            .orEmpty()

        val constrainedFqName = constraintsDescriptor.constrainedType
            .getJetTypeFqName(false)
            .plus(typeArgs)

        val constrainerFqName = constraintsDescriptor.constrainerClassOrObject
            .fqName
            ?.asString()
            ?.plus(typeArgs)

        val params = (constrainedParams + constrainerParams).joinToString(",\n\t") { param ->
            param.name + ": " + param.type.deepFqName()
        }

        val itemArgs = constrainedParams.joinToString(",\n") { param ->
            param.name
        }

        val findViolatedConstraintsArgs = (constrainedParams + constrainerParams).joinToString(",\n") { param ->
            param.name
        }

        return """
            |fun $typeParams$constrainerFqName.isValid(
            |    $params
            |): Boolean {
            |
            |    val item = lazy {
            |        $constrainedFqName(
            |            $itemArgs
            |        )
            |    }
            |
            |    return findViolatedConstraints(
            |        item,
            |        ${findViolatedConstraintsArgs.replace("\n", "\n\t\t")}
            |    ).isNotEmpty()
            |}
        """.trimMargin()
    }

    private fun generateValidate(
        constraintsDescriptor: ConstraintsDescriptor,
        constrainedParams: List<Param>,
        constrainerParams: List<Param>
    ): String {

        val typeArgs = constraintsDescriptor.constrainerClassOrObject
            .typeParameters
            .map { it.nameAsSafeName.asString() }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(", ")
            ?.let { "<$it>" }
            .orEmpty()

        val typeParams = constraintsDescriptor.constrainerClassOrObject
            .typeParameters
            .map { it.deepFqName(bindingContext) }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(separator = ", ", prefix = "<", postfix = ">")
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

        val params = (constrainedParams + constrainerParams).joinToString(",\n\t") { param ->
            param.name + ": " + param.type.deepFqName()
        }

        val itemArgs = constrainedParams.joinToString(",\n\t\t\t") { param ->
            param.name
        }

        val findViolatedConstraintsArgs = (constrainedParams + constrainerParams).joinToString(",\n\t\t") { param ->
            param.name
        }

        return """
            |fun $typeParams$constrainerFqName.validate(
            |    $params
            |): Case<List<$violationsParent>, $constrainedFqName> {
            |
            |    val item = lazy {
            |        $constrainedFqName(
            |            $itemArgs
            |        )
            |    }
            |    
            |    return findViolatedConstraints(
            |        item,
            |        $findViolatedConstraintsArgs
            |    ).map { it.toViolation(item) }
            |        .takeIf(List<$violationsParent>::isNotEmpty)
            |        ?.illegal() ?: item.value.legal()
            |}
        """.trimMargin()
    }

    private fun generateFindViolatedConstraints(
        constraintsDescriptor: ConstraintsDescriptor,
        constrainedParams: List<Param>,
        constrainerParams: List<Param>
    ): String? {

        val typeArgs = constraintsDescriptor.constrainerClassOrObject
            .typeParameters
            .map { it.nameAsSafeName.asString() }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(", ")
            ?.let { "<$it>" }
            .orEmpty()

        val typeParams = constraintsDescriptor.constrainerClassOrObject
            .typeParameters
            .map { it.deepFqName(bindingContext) }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(separator = ", ", prefix = "<", postfix = ">")
            ?.plus(" ")
            .orEmpty()

        val constrainedFqName = constraintsDescriptor.constrainedType
            .getJetTypeFqName(false)
            .plus(typeArgs)

        val constrainerFqName = constraintsDescriptor.constrainerClassOrObject
            .fqName
            ?.asString()
            ?.plus(typeArgs)

        val params = (constrainedParams + constrainerParams).joinToString(",\n\t") { param ->
            param.name + ": " + param.type.deepFqName()
        }

        val validationCases = constrainedParams.joinToString("\n\n") { param ->
            """
                |"${param.name}" -> { validation ->
                |    (validation as Validation<${param.type.deepFqName()}>).validate(${param.name})
                |}
            """.trimMargin()
        }

        val constraints = when (constraintsDescriptor.constrainerClassOrObject) {

            is KtObjectDeclaration -> "this.constraints"

            is KtClass -> {
                val createArgs = constrainerParams.joinToString(
                    separator = ", ",
                    transform = Param::name
                )
                "this.create($createArgs)"
            }

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
            .typeParameters
            .map { it.deepFqName(bindingContext) }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(separator = ", ", prefix = "<", postfix = ">")
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
                    "this.params[$index].get(item.value) as ${param.type.deepFqName()}"
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
        constraintsDescriptor: ConstraintsDescriptor,
        constrainerParams: List<Param>
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
            .typeParameters
            .map { it.deepFqName(bindingContext) }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(separator = ", ", prefix = "<", postfix = ">")
            ?.plus(" ")
            .orEmpty()

        val constrainedFqName = constraintsDescriptor.constrainedType
            .getJetTypeFqName(false)
            .plus(typeArgs)

        val constrainerFqName = constraintsDescriptor.constrainerClassOrObject
            .fqName
            ?.asString()
            ?.plus(typeArgs)

        val params = constrainerParams.takeIf(
            List<Param>::isNotEmpty
        )?.joinToString(separator = ",\n\t", prefix = "\n\t", postfix = "\n") { param ->
                param.name + ": " + param.type.deepFqName()
        }.orEmpty()

        val constrainerArgs = constrainerParams.joinToString(
            separator = ", ",
            transform = Param::name
        )

        return """
            |private fun ${typeParams}$constrainerFqName.create($params): ConstraintsDescriptor<$constrainedFqName> {
            |    return $constrainerFqName($constrainerArgs).constraints
            |}
        """.trimMargin()
    }
}
