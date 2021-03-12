package dev.ahmedmourad.validation.compiler.descriptors

import dev.ahmedmourad.validation.compiler.utils.*
import dev.ahmedmourad.validation.compiler.utils.VALIDATION_CONTEXT_IMPL_SUFFIX
import dev.ahmedmourad.validation.compiler.utils.VALIDATION_CONTEXT_SUFFIX
import dev.ahmedmourad.validation.compiler.utils.VIOLATIONS_SUPER_CLASS_SUFFIX
import dev.ahmedmourad.validation.compiler.utils.simpleName
import dev.ahmedmourad.validation.compiler.verifier.ValidationVerifier
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.lazy.descriptors.LazyClassDescriptor
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.utils.addToStdlib.cast
import org.jetbrains.kotlin.utils.addToStdlib.safeAs

internal data class ConstraintsDescriptor constructor(
    private val bindingContext: BindingContext,
    private val verifier: ValidationVerifier,
    val constrainedType: KotlinType,
    val constrainedTypePsi: PsiElement,
    val constrainerClassOrObject: KtClassOrObject,
    val violations: List<ViolationDescriptor>
) {

    val packageName by lazy { constrainerClassOrObject.containingFile.cast<KtFile>().packageFqName.asString() }
    val packageAsPath by lazy { packageName.replace('.', '/') }

    val constrainedClass by lazy {
        constrainedType.constructor
            .declarationDescriptor
            ?.safeAs<LazyClassDescriptor>()
    }

    val constrainedTypeArgs by lazy {
        constrainedType.arguments
            .map { it.toString() }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(separator = ", ", prefix = "<", postfix = ">")
            .orEmpty()
    }

    val constrainedParams by lazy {
        constrainedClass?.let {
            verifier.verifyConstrainedClassHasPrimaryConstructor(
                it,
                constrainedTypePsi
            )?.valueParameters
        }
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

    val isValidationContextImplAnObject by lazy { constrainerTypeParams.isBlank() }
}
