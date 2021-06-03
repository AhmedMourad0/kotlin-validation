package dev.ahmedmourad.validation.compiler.codegen.validations

import dev.ahmedmourad.validation.compiler.codegen.CodeSectionGenerator
import dev.ahmedmourad.validation.compiler.descriptors.ConstraintsDescriptor
import dev.ahmedmourad.validation.compiler.descriptors.ViolationDescriptor

internal class ViolationsGenerator : CodeSectionGenerator {

    override fun imports(constraintsDescriptor: ConstraintsDescriptor) = emptySet<String>()

    override fun generate(
        constraintsDescriptor: ConstraintsDescriptor
    ): Set<String> {
        val parentName = constraintsDescriptor.violationsParentName
        val violations = constraintsDescriptor.violations.joinToString("\n\t") {
            generateViolation(parentName, it).replace("\n", "\n\t")
        }
        return setOf(generateViolationsParent(parentName, violations))
    }

    private fun generateViolationsParent(parentName: String, violations: String): String {
        return """
        |sealed class $parentName {
        |    $violations
        |}
        """.trimMargin()
    }

    private fun generateViolation(
        parentName: String,
        violation: ViolationDescriptor
    ): String {
        return if (violation.metas.isEmpty()) {
            "object ${violation.name} : $parentName()"
        } else {
            val metas = violation.metas.joinToString(",\n\t") { meta ->
                "val ${meta.name}: ${meta.typeFqName}"
            }
            "data class ${violation.name}(\n\t$metas\n) : $parentName()"
        }
    }
}
