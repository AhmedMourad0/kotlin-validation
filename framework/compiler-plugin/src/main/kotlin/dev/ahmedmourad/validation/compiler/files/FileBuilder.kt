package dev.ahmedmourad.validation.compiler.files

import dev.ahmedmourad.validation.compiler.codegen.CodeSectionGenerator
import dev.ahmedmourad.validation.compiler.descriptors.ValidatorDescriptor
import dev.ahmedmourad.validation.compiler.dsl.DslValidator
import dev.ahmedmourad.validation.compiler.utils.OUTPUT_FOLDER
import dev.ahmedmourad.validation.compiler.utils.SUFFIX_OUTPUT_FILE_NAME
import org.jetbrains.kotlin.name.parentOrNull
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.utils.addToStdlib.safeAs
import java.io.File

internal class FileBuilder(private val dslValidator: DslValidator) {
    fun createFile(
        codeGenDir: File,
        validatorDescriptor: ValidatorDescriptor,
        vararg generators: CodeSectionGenerator
    ) {

        val fileName = validatorDescriptor.validatorClassOrObject.let { classOrObject ->
            if (classOrObject.safeAs<KtObjectDeclaration>()?.isCompanion() == true) {
                classOrObject.fqName?.parentOrNull()?.shortName()?.asString()?.plus("Companion")
            } else {
                classOrObject.fqName?.shortName()?.asString()
            }
        }?.plus(SUFFIX_OUTPUT_FILE_NAME) ?: dslValidator.reportError(
            "Unable to find validator name",
            validatorDescriptor.validatorClassOrObject
        )

        val content = """
        |package ${validatorDescriptor.packageName}.$OUTPUT_FOLDER
        |
        |${generators.flatMap { it.imports(validatorDescriptor) }.distinct().joinToString("\n") { "import $it" }}
        |
        |${generators.flatMap { it.generate(validatorDescriptor) }.joinToString("\n\n")}
        |
        """.trimMargin()

        val directory = File(codeGenDir, "${validatorDescriptor.packageAsPath}${File.separatorChar}$OUTPUT_FOLDER")
        val file = File(directory, "$fileName.kt")

        check(file.parentFile.exists() || file.parentFile.mkdirs()) {
            "Could not generate package directory: ${file.parentFile}"
        }

        file.writeText(content)
    }
}
