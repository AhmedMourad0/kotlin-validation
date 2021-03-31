package dev.ahmedmourad.validation.compiler.descriptors

import dev.ahmedmourad.validation.compiler.utils.*
import dev.ahmedmourad.validation.compiler.utils.VALIDATION_CONTEXT_IMPL_SUFFIX
import dev.ahmedmourad.validation.compiler.utils.VALIDATION_CONTEXT_SUFFIX
import dev.ahmedmourad.validation.compiler.utils.VIOLATIONS_SUPER_CLASS_SUFFIX
import dev.ahmedmourad.validation.compiler.utils.simpleName
import dev.ahmedmourad.validation.compiler.dsl.DslValidator
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.descriptors.ConstructorDescriptor
import org.jetbrains.kotlin.descriptors.ValueParameterDescriptor
import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.lazy.descriptors.LazyClassDescriptor
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.utils.addToStdlib.cast
import org.jetbrains.kotlin.utils.addToStdlib.safeAs

internal class ConstraintsDescriptor constructor(
    bindingContext: BindingContext,
    dslValidator: DslValidator,
    val constrainedType: KotlinType,
    constrainedTypePsi: PsiElement,
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

    private val constrainedTypeArgs by lazy {
        constrainedType.arguments
            .map { it.toString() }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(separator = ", ", prefix = "<", postfix = ">")
            .orEmpty()
    }

    val constrainedParams: List<ValueParameterDescriptor>? by lazy {
        constrainedClass?.constructors
            ?.firstOrNull(ConstructorDescriptor::isPrimary)
            ?.valueParameters
    }

    val constrainedFqName by lazy {
        constrainedType
            .getJetTypeFqName(false)
            .plus(constrainedTypeArgs)
    }

    private val constrainedAlias by lazy {

        val (annotation, entry) = constrainerClassOrObject.annotationEntries
            .mapNotNull { annotationEntry ->
                bindingContext.get(BindingContext.ANNOTATION, annotationEntry).takeIf {
                    it?.fqName == FqName(FQ_NAME_CONSTRAINED_ALIAS_ANNOTATION)
                } to annotationEntry
            }.firstOrNull() ?: null to null

            annotation?.allValueArguments
            ?.get(Name.identifier(CONSTRAINED_ALIAS_PARAM_CONSTRAINER_CONFIG_ANNOTATION))
            ?.value
            ?.safeAs<String>()
            ?.let { dslValidator.verifyConstrainedAlias(it, entry) }
    }

    private val constrainedAliasOrSimpleName by lazy {
        constrainedAlias ?: constrainedType.simpleName()
    }

    val constrainerTypeParamsAsTypeArgs by lazy {
        constrainerClassOrObject
            .typeParameters
            .map { it.nameAsSafeName.asString() }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(separator = ", ", prefix = "<", postfix = ">")
            .orEmpty()
    }

    val constrainerTypeParams by lazy {
        constrainerClassOrObject
            .typeParameters
            .map { it.deepFqName(bindingContext) }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(separator = ", ", prefix = "<", postfix = ">")
            ?.plus(" ")
            .orEmpty()
    }

    val constrainerFqName by lazy {
        constrainerClassOrObject
            .fqName
            ?.asString()
            ?.plus(constrainerTypeParamsAsTypeArgs)
    }

    val violationsParentName by lazy { constrainedAliasOrSimpleName!! + VIOLATIONS_SUPER_CLASS_SUFFIX }

    val validationContextName by lazy { constrainedAliasOrSimpleName!! + VALIDATION_CONTEXT_SUFFIX }
    val validationContextImplName by lazy { validationContextName + VALIDATION_CONTEXT_IMPL_SUFFIX }

    val isValidationContextImplAnObject by lazy { constrainerTypeParams.isBlank() }
}
