package dev.ahmedmourad.validation.compiler.files

import arrow.meta.phases.CompilerContext
import dev.ahmedmourad.validation.compiler.utils.*
import dev.ahmedmourad.validation.compiler.descriptors.ConstraintsDescriptor
import dev.ahmedmourad.validation.compiler.generators.Generator
import dev.ahmedmourad.validation.compiler.dsl.DslValidator
import org.jetbrains.kotlin.cli.common.config.KotlinSourceRoot
import org.jetbrains.kotlin.cli.common.config.kotlinSourceRoots
import java.io.File

//TODO: there's probably a better approach to this
internal class FileManager(
    private val cc: CompilerContext,
    private val dslValidator: DslValidator
) {

    private val sourceGenFolder by lazy { createSourceGenFolder() }

    internal val generatedFiles: List<File>
        get() = sourceGenFolder?.let(::listOf).orEmpty()

    internal fun createFile(
        constraintsDescriptor: ConstraintsDescriptor,
        vararg generators: Generator
    ) {

        val fileName = constraintsDescriptor.constrainerClassOrObject
            .let { classOrObject ->
                if (classOrObject.safeAs<KtObjectDeclaration>()?.isCompanion() == true) {
                    classOrObject.fqName?.parentOrNull()?.shortName()?.asString()?.plus("Companion")
                } else {
                    classOrObject.fqName?.shortName()?.asString()
                }
            }?.plus(OUTPUT_FILE_NAME_SUFFIX)
            ?: dslValidator.reportError("Unable to find constrainer name", constraintsDescriptor.constrainerClassOrObject)

        val directory = File(sourceGenFolder, constraintsDescriptor.packageAsPath + "/$OUTPUT_FOLDER")
        val file = File(directory, "$fileName.kt")

        if (!file.parentFile.exists() && !file.parentFile.mkdirs()) {
            dslValidator.reportError("Could not generate package directory: $file", null)
            return
        }

        file.writeText("""
        |package ${constraintsDescriptor.packageName}.$OUTPUT_FOLDER
        |
        |${generators.flatMap { it.imports(constraintsDescriptor) }.distinct().joinToString("\n") { "import $it" }}
        |
        |${generators.flatMap { it.generate(constraintsDescriptor) }.joinToString("\n\n")}
        |
        """.trimMargin()
        )
    }

    private fun createSourceGenFolder(): File? {

        val kotlinSourceRoots: List<KotlinSourceRoot> = cc.configuration?.kotlinSourceRoots ?: return null

        fun kotlinValidationDir(parent: File): File? {
            val file = File(parent, "kotlin-validation")
            return if (!file.exists() && !file.mkdirs()) {
                dslValidator.reportError("Could not create source generation directory: $file", null)
            } else {
                file
            }
        }

        val oneSourceFile = File(kotlinSourceRoots.first().path)
        val parentSequence = generateSequence(oneSourceFile) { it.parentFile }

        // Try to find the src dir.
        parentSequence.firstOrNull { it.name == "src" }?.let {
            return kotlinValidationDir(File(it.parentFile, "build"))
        }

        // If the src dir is not part of the input (incremental build), look for the build dir directly.
        parentSequence.firstOrNull { it.name == "build" }?.let {
            return kotlinValidationDir(it)
        }

        return verifier.reportError(
            "Could not create source generation directory: $oneSourceFile",
            null
        )
    }
}
