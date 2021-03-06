package dev.ahmedmourad.validation.compiler.generators

import dev.ahmedmourad.validation.compiler.descriptors.ConstraintsDescriptor
import dev.ahmedmourad.validation.compiler.utils.VALIDATION_CONTEXT_SUFFIX
import dev.ahmedmourad.validation.compiler.utils.simpleName
import org.jetbrains.kotlin.resolve.BindingContext

internal class ValidationContextGenerator(
    private val bindingContext: BindingContext
) : Generator {

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
        }.distinctBy {
            it.validationsFileFqName
        }.map { includedConstraint ->
            includedConstraint.validationsFileFqName +
                    "." +
                    includedConstraint.constrainedType.simpleName() + VALIDATION_CONTEXT_SUFFIX +
                    includedConstraint.constrainerType
                .arguments
                .map { it.toString() }
                .takeIf(List<String>::isNotEmpty)
                ?.joinToString(separator = ", ", prefix = "<", postfix = ">")
                .orEmpty()
        }.takeIf(List<String>::isNotEmpty)?.joinToString(separator = ", ", prefix = " : ").orEmpty()

        return "interface ${constraintsDescriptor.validationContextName}${constraintsDescriptor.constrainerTypeParams}$supertypes"
    }

    private fun generateValidationContextImpl(
        constraintsDescriptor: ConstraintsDescriptor
    ): String {
        return "private class ${constraintsDescriptor.validationContextImplName}${constraintsDescriptor.constrainerTypeParams}: ${constraintsDescriptor.validationContextName}${constraintsDescriptor.constrainerTypeParamsAsTypeArgs}"
    }
}
