package dev.ahmedmourad.validation.compiler.generators

import dev.ahmedmourad.validation.compiler.descriptors.ConstraintsDescriptor
import dev.ahmedmourad.validation.compiler.descriptors.ViolationDescriptor

internal class ViolationsGenerator : Generator {

    override fun imports(constraintsDescriptor: ConstraintsDescriptor) = emptyList<String>()

    override fun generate(
        constraintsDescriptor: ConstraintsDescriptor
    ): List<String> {
        val parentName = constraintsDescriptor.violationsParentName
        val violations = constraintsDescriptor.violations.joinToString("\n\t") {
            generateViolation(parentName, it).replace("\n", "\n\t")
        }
        return listOf(generateViolationsParent(parentName, violations))
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
        return if (violation.params.isEmpty()) {
            "object ${violation.name} : $parentName()"
        } else {
            val params = violation.params.joinToString(",\n\t") { param ->
                "val ${param.name}: ${param.typeFqName}"
            }
            "data class ${violation.name}(\n\t$params\n) : $parentName()"
        }
    }
}
