package dev.ahmedmourad.validation.compiler.generators

import dev.ahmedmourad.validation.compiler.descriptors.ConstraintsDescriptor
import dev.ahmedmourad.validation.compiler.descriptors.Violation
import dev.ahmedmourad.validation.compiler.utils.fqNameTotal

internal class ViolationsGenerator {

    internal val imports: List<String> = emptyList()

    internal fun generate(
        constraintsDescriptor: ConstraintsDescriptor
    ): String {
        val parentName = constraintsDescriptor.violationsParentName
        val violations = constraintsDescriptor.violations.joinToString("\n\t") {
            generateViolation(parentName, it).replace("\n", "\n\t")
        }
        return """
        |sealed class $parentName {
        |    $violations
        |}
        """.trimMargin()
    }

    private fun generateViolation(
        parentName: String,
        violation: Violation
    ): String {
        return if (violation.params.isEmpty()) {
            "object ${violation.name} : $parentName()"
        } else {
            val params = violation.params.joinToString(",\n\t") { param ->
                "val ${param.name}: ${param.type.fqNameTotal()}"
            }
            "data class ${violation.name}(\n\t$params\n) : $parentName()"
        }
    }
}
