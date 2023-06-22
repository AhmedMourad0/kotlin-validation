package dev.ahmedmourad.validation.compiler.descriptors

import dev.ahmedmourad.validation.compiler.utils.*
import dev.ahmedmourad.validation.compiler.utils.SUFFIX_VALIDATION_CONTEXT_IMPL
import dev.ahmedmourad.validation.compiler.utils.SUFFIX_VALIDATION_CONTEXT
import dev.ahmedmourad.validation.compiler.utils.SUFFIX_VIOLATIONS_SUPER_CLASS
import dev.ahmedmourad.validation.compiler.utils.simpleName
import dev.ahmedmourad.validation.compiler.dsl.DslValidator
import org.jetbrains.kotlin.descriptors.ConstructorDescriptor
import org.jetbrains.kotlin.descriptors.ValueParameterDescriptor
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.lazy.descriptors.LazyClassDescriptor
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.utils.addToStdlib.cast
import org.jetbrains.kotlin.utils.addToStdlib.safeAs
import java.io.File

internal class ValidatorDescriptor(
    bindingContext: BindingContext,
    dslValidator: DslValidator,
    val subjectType: KotlinType,
    val validatorClassOrObject: KtClassOrObject,
    val violations: List<ViolationDescriptor>
) {

    val packageName by lazy { validatorClassOrObject.containingFile.cast<KtFile>().packageFqName.asString() }
    val packageAsPath by lazy { packageName.replace('.', File.separatorChar) }

    val subjectClass by lazy {
        subjectType.constructor
            .declarationDescriptor
            ?.safeAs<LazyClassDescriptor>()
    }

    private val subjectTypeArgs by lazy {
        subjectType.arguments
            .map { it.toString() }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(separator = ", ", prefix = "<", postfix = ">")
            .orEmpty()
    }

    val subjectFqName by lazy {
        subjectType
            .fqNameSafe
            ?.asString()
            ?.plus(subjectTypeArgs)
    }

    private val subjectAlias by lazy {

        val (annotation, entry) = validatorClassOrObject.annotationEntries.firstNotNullOfOrNull { annotationEntry ->
            bindingContext.get(BindingContext.ANNOTATION, annotationEntry).takeIf {
                it?.fqName == fqNameValidatorConfig
            } to annotationEntry
        } ?: (null to null)

            annotation?.allValueArguments
            ?.get(paramSubjectAlias)
            ?.value
            ?.safeAs<String>()
            ?.let { dslValidator.verifySubjectAlias(it, entry) }
    }

    private val subjectAliasOrSimpleName by lazy {
        subjectAlias ?: subjectType.simpleName()
    }

    val validatorTypeParamsAsTypeArgs by lazy {
        validatorClassOrObject
            .typeParameters
            .map { it.nameAsSafeName.asString() }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(separator = ", ", prefix = "<", postfix = ">")
            .orEmpty()
    }

    val validatorTypeParams by lazy {
        validatorClassOrObject
            .typeParameters
            .map { it.deepFqName(bindingContext) }
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(separator = ", ", prefix = "<", postfix = ">")
            ?.plus(" ")
            .orEmpty()
    }

    val validatorFqName by lazy {
        validatorClassOrObject
            .fqName
            ?.asString()
            ?.plus(validatorTypeParamsAsTypeArgs)
    }

    val violationsParentName by lazy { subjectAliasOrSimpleName!! + SUFFIX_VIOLATIONS_SUPER_CLASS }

    val validationContextName by lazy { subjectAliasOrSimpleName!! + SUFFIX_VALIDATION_CONTEXT }
    val validationContextImplName by lazy { validationContextName + SUFFIX_VALIDATION_CONTEXT_IMPL }

    val isValidationContextImplAnObject by lazy { validatorTypeParams.isBlank() }
}
