package dev.ahmedmourad.validation.compiler.descriptors

import dev.ahmedmourad.validation.compiler.utils.*
import dev.ahmedmourad.validation.compiler.utils.SUFFIX_VALIDATION_CONTEXT_IMPL
import dev.ahmedmourad.validation.compiler.utils.SUFFIX_VALIDATION_CONTEXT
import dev.ahmedmourad.validation.compiler.utils.SUFFIX_VIOLATIONS_SUPER_CLASS
import dev.ahmedmourad.validation.compiler.utils.simpleName
import dev.ahmedmourad.validation.compiler.dsl.DslValidator
import org.jetbrains.kotlin.descriptors.ConstructorDescriptor
import org.jetbrains.kotlin.descriptors.ValueParameterDescriptor
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.lazy.descriptors.LazyClassDescriptor
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.utils.addToStdlib.cast
import org.jetbrains.kotlin.utils.addToStdlib.safeAs
import java.io.File

internal class ConstraintsDescriptor(
    bindingContext: BindingContext,
    dslValidator: DslValidator,
    val constrainedType: KotlinType,
    val constrainerClassOrObject: KtClassOrObject,
    val violations: List<ViolationDescriptor>
) {

    val packageName by lazy { constrainerClassOrObject.containingFile.cast<KtFile>().packageFqName.asString() }
    val packageAsPath by lazy { packageName.replace('.', File.separatorChar) }

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
            .fqNameSafe
            ?.asString()
            ?.plus(constrainedTypeArgs)
    }

    private val constrainedAlias by lazy {

        val (annotation, entry) = constrainerClassOrObject.annotationEntries
            .mapNotNull { annotationEntry ->
                bindingContext.get(BindingContext.ANNOTATION, annotationEntry).takeIf {
                    it?.fqName == fqNameConstrainerConfig
                } to annotationEntry
            }.firstOrNull() ?: null to null

            annotation?.allValueArguments
            ?.get(paramConstrainedAlias)
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

    val violationsParentName by lazy { constrainedAliasOrSimpleName!! + SUFFIX_VIOLATIONS_SUPER_CLASS }

    val validationContextName by lazy { constrainedAliasOrSimpleName!! + SUFFIX_VALIDATION_CONTEXT }
    val validationContextImplName by lazy { validationContextName + SUFFIX_VALIDATION_CONTEXT_IMPL }

    val isValidationContextImplAnObject by lazy { constrainerTypeParams.isBlank() }
}
