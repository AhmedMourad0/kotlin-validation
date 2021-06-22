package dev.ahmedmourad.validation.compiler.codegen.validations

import dev.ahmedmourad.validation.compiler.codegen.CodeSectionGenerator
import dev.ahmedmourad.validation.compiler.descriptors.ValidatorDescriptor
import dev.ahmedmourad.validation.compiler.utils.SUFFIX_VALIDATION_CONTEXT
import org.jetbrains.kotlin.types.TypeProjection

internal class ValidationContextGenerator : CodeSectionGenerator {

    override fun imports(validatorDescriptor: ValidatorDescriptor) = emptySet<String>()

    override fun generate(
        validatorDescriptor: ValidatorDescriptor
    ): Set<String> {
        return setOf(
            generateValidationContext(validatorDescriptor),
            generateValidationContextImpl(validatorDescriptor)
        )
    }

    private fun generateValidationContext(
        validatorDescriptor: ValidatorDescriptor
    ): String {

        val supertypes = validatorDescriptor.violations.flatMap {
            it.metas
        }.mapNotNull {
            it.includedValidator
        }.map { includedValidator ->

            val includedValidatorTypeArgs = includedValidator.validatorType
                .arguments
                .map(TypeProjection::toString)
                .takeIf(List<String>::isNotEmpty)
                ?.joinToString(separator = ", ", prefix = "<", postfix = ">")
                .orEmpty()

            val includedSubjectSimpleName = includedValidator.subjectAliasOrSimpleName

            val validationsFileFqName = includedValidator.validationsFileFqName

            "$validationsFileFqName.$includedSubjectSimpleName$SUFFIX_VALIDATION_CONTEXT$includedValidatorTypeArgs"
        }.distinct()
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(separator = ", ", prefix = " : ")
            .orEmpty()

        val validationContextName = validatorDescriptor.validationContextName
        val validatorTypeParams = validatorDescriptor.validatorTypeParams.let {
            if (supertypes.isNotBlank()) {
                it.trim()
            } else {
                it
            }
        }

        return "interface $validationContextName$validatorTypeParams$supertypes"
    }

    private fun generateValidationContextImpl(validatorDescriptor: ValidatorDescriptor): String {

        val validationContextName = validatorDescriptor.validationContextName
        val validationContextImplName = validatorDescriptor.validationContextImplName

        val validatorTypeParams = validatorDescriptor.validatorTypeParams
        val validatorTypeParamsAsTypeArgs = validatorDescriptor.validatorTypeParamsAsTypeArgs

        return if (validatorDescriptor.isValidationContextImplAnObject) {
            "private object $validationContextImplName : $validationContextName"
        } else {
            "private class $validationContextImplName$validatorTypeParams: $validationContextName$validatorTypeParamsAsTypeArgs"
        }
    }
}
