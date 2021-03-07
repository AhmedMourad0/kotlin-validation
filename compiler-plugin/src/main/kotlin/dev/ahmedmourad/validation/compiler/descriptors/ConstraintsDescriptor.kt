package dev.ahmedmourad.validation.compiler.descriptors

import dev.ahmedmourad.validation.compiler.utils.*
import dev.ahmedmourad.validation.compiler.utils.VALIDATION_CONTEXT_IMPL_SUFFIX
import dev.ahmedmourad.validation.compiler.utils.VALIDATION_CONTEXT_SUFFIX
import dev.ahmedmourad.validation.compiler.utils.VIOLATIONS_SUPER_CLASS_SUFFIX
import dev.ahmedmourad.validation.compiler.utils.simpleName
import dev.ahmedmourad.validation.compiler.verifier.DslVerifier
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.TypeProjection
import org.jetbrains.kotlin.types.typeUtil.isTypeParameter

internal data class ConstraintsDescriptor constructor(
    private val bindingContext: BindingContext,
    private val verifier: DslVerifier,
    val constrainedType: KotlinType,
    val constrainedTypePsi: PsiElement,
    val constrainerClassOrObject: KtClassOrObject,
    val violations: List<Violation>
) {

    val packageName by lazy { (constrainerClassOrObject.containingFile as KtFile).packageFqName.asString() }
    val packageAsPath by lazy { packageName.replace('.', '/') }

    val constrainedClass by lazy {
        verifier.verifyConstrainedClassType(
            constrainedType,
            constrainedTypePsi
        )
    }

    val constrainedTypeArgs by lazy {
        constrainedType.arguments
            .map { it.toString() }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(separator = ", ", prefix = "<", postfix = ">")
            .orEmpty()
    }

    val constrainedFqName by lazy {
        constrainedType
            .getJetTypeFqName(false)
            .plus(constrainedTypeArgs)
    }

    val constrainerTypeParamsAsTypeArgs by lazy {
        constrainerClassOrObject
            .typeParameters
            .map { it.nameAsSafeName.asString() }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(separator = ", ", prefix = "<", postfix = ">")
            .orEmpty()
    }

    val constrainerTypeParamsForConstrained by lazy {

        val typeParams = constrainedType
            .arguments
            .deepFlatMap { it.type.arguments }
            .filter { it.type.isTypeParameter() }
            .map(TypeProjection::toString)
            .distinct()

        constrainerClassOrObject
            .typeParameters
            .filter { it.nameAsSafeName.asString() in typeParams }
            .map { it.deepFqName(bindingContext) }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(separator = ", ", prefix = "<", postfix = ">")
            .orEmpty()
    }

    val constrainerTypeParamsForConstrainedAsTypeArgs by lazy {

        val typeParams = constrainedType
            .arguments
            .deepFlatMap { it.type.arguments }
            .filter { it.type.isTypeParameter() }
            .map(TypeProjection::toString)
            .distinct()

        constrainerClassOrObject
            .typeParameters
            .map { it.nameAsSafeName.asString() }
            .filter { it in typeParams }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(separator = ", ", prefix = "<", postfix = ">")
            .orEmpty()
    }

    val constrainerTypeParams = constrainerClassOrObject
        .typeParameters
        .map { it.deepFqName(bindingContext) }
        .takeIf(List<String>::isNotEmpty)
        ?.joinToString(separator = ", ", prefix = "<", postfix = ">")
        ?.plus(" ")
        .orEmpty()

    val constrainerFqName by lazy {
        constrainerClassOrObject
            .fqName
            ?.asString()
            ?.plus(constrainerTypeParamsAsTypeArgs)
    }

    val violationsParentName by lazy { constrainedType.simpleName()!! + VIOLATIONS_SUPER_CLASS_SUFFIX }

    val validationContextName by lazy { constrainedType.simpleName()!! + VALIDATION_CONTEXT_SUFFIX }
    val validationContextImplName by lazy { validationContextName + VALIDATION_CONTEXT_IMPL_SUFFIX }
    val isValidationContextImplAClass by lazy { constrainerTypeParams.isNotBlank() }
}

internal data class Violation(
    val name: String,
    val nameExpression: KtExpression,
    val constrainedType: KotlinType,
    val constrainedTypePsi: PsiElement,
    val constrainerClassOrObject: KtClassOrObject,
    val params: List<Param>
) {
    val regularParams by lazy { params.filter { it.includedConstraint == null } }
    val inclusionParams by lazy { params.filter { it.includedConstraint != null } }
}

internal data class Param(
    val name: String,
    val nameExpression: KtExpression,
    val typeFqName: String,
    val includedConstraint: IncludedConstraint?
)

internal data class IncludedConstraint(
    val validationsFileFqName: String,
    val constrainedType: KotlinType,
    val constrainerType: KotlinType
) {
    val validateFqName by lazy { "$validationsFileFqName.validate" }
    val isValidFqName by lazy { "$validationsFileFqName.isValid" }
}
