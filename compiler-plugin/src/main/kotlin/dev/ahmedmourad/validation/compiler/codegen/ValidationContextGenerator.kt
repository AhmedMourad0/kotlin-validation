package dev.ahmedmourad.validation.compiler.codegen

import dev.ahmedmourad.validation.compiler.descriptors.ConstraintsDescriptor
import dev.ahmedmourad.validation.compiler.utils.VALIDATION_CONTEXT_SUFFIX
import org.jetbrains.kotlin.types.TypeProjection

internal class ValidationContextGenerator : Generator {

    override fun imports(constraintsDescriptor: ConstraintsDescriptor) = emptyList<String>()

    override fun generate(
        constraintsDescriptor: ConstraintsDescriptor
    ): List<String> {
        return listOf(
            generateValidationContext(constraintsDescriptor),
            generateValidationContextImpl(constraintsDescriptor)
        )
    }

    private fun generateValidationContext(
        constraintsDescriptor: ConstraintsDescriptor
    ): String {

        val supertypes = constraintsDescriptor.violations.flatMap {
            it.params
        }.mapNotNull {
            it.includedConstraint
        }.map { includedConstraint ->

            val includedConstrainerTypeArgs = includedConstraint.constrainerType
                .arguments
                .map(TypeProjection::toString)
                .takeIf(List<String>::isNotEmpty)
                ?.joinToString(separator = ", ", prefix = "<", postfix = ">")
                .orEmpty()

            val includedConstrainedSimpleName = includedConstraint.constrainedAliasOrSimpleName

            val validationsFileFqName = includedConstraint.validationsFileFqName

            "$validationsFileFqName.$includedConstrainedSimpleName$VALIDATION_CONTEXT_SUFFIX$includedConstrainerTypeArgs"
        }.distinct()
            .takeIf(List<String>::isNotEmpty)
            ?.joinToString(separator = ", ", prefix = " : ")
            .orEmpty()

        val validationContextName = constraintsDescriptor.validationContextName
        val constrainerTypeParams = constraintsDescriptor.constrainerTypeParams.let {
            if (supertypes.isNotBlank()) {
                it.trim()
            } else {
                it
            }
        }

        return "interface $validationContextName$constrainerTypeParams$supertypes"
    }

    private fun generateValidationContextImpl(constraintsDescriptor: ConstraintsDescriptor): String {

        val validationContextName = constraintsDescriptor.validationContextName
        val validationContextImplName = constraintsDescriptor.validationContextImplName

        val constrainerTypeParams = constraintsDescriptor.constrainerTypeParams
        val constrainerTypeParamsAsTypeArgs = constraintsDescriptor.constrainerTypeParamsAsTypeArgs

        return if (constraintsDescriptor.isValidationContextImplAnObject) {
            "private object $validationContextImplName : $validationContextName"
        } else {
            "private class $validationContextImplName$constrainerTypeParams: $validationContextName$constrainerTypeParamsAsTypeArgs"
        }
    }
}
