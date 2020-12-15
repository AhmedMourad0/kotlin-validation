package dev.ahmedmourad.validation.compiler.generators

import arrow.meta.phases.CompilerContext
import dev.ahmedmourad.validation.compiler.utils.*
import dev.ahmedmourad.validation.compiler.descriptors.ViolationsDescriptor
import org.jetbrains.kotlin.cli.common.config.KotlinSourceRoot
import org.jetbrains.kotlin.cli.common.config.kotlinSourceRoots
import java.io.File

internal class FileManager(private val cc: CompilerContext) {

    private val sourceGenFolder by lazy { createSourceGenFolder() }

    internal val generatedFiles: List<File>
        get() = sourceGenFolder?.let(::listOf) ?: emptyList()

    internal fun createFile(
        violationsDescriptor: ViolationsDescriptor
    ) {

        val fileName = violationsDescriptor.constrainedSimpleName + OUTPUT_FILE_NAME_SUFFIX
        val directory = File(sourceGenFolder, violationsDescriptor.packageAsPath + "/$OUTPUT_FOLDER")
        val file = File(directory, "$fileName.kt")

        if (!file.parentFile.exists() && !file.parentFile.mkdirs()) {
            cc.reportError("Could not generate package directory: $file", null)
            return
        }

        file.writeText(
            """
        |package ${violationsDescriptor.packageName}.$OUTPUT_FOLDER
        |
        |${violationsDescriptor.text}
        |
        """.trimMargin()
        )
    }

    private fun createSourceGenFolder(): File? {

        val kotlinSourceRoots: List<KotlinSourceRoot> = cc.configuration?.kotlinSourceRoots ?: return null

        fun kotlinValidationDir(parent: File): File? {
            val file = File(parent, "kotlin-validation")
            return if (!file.exists() && !file.mkdirs()) {
                cc.reportError("Could not create source generation directory: $file", null)
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

        // If the src dir is not part of the input (incremental build), look for the build dir
        // directly.
        parentSequence.firstOrNull { it.name == "build" }?.let {
            return kotlinValidationDir(it)
        }

        return cc.reportError(
            "Could not create source generation directory: $oneSourceFile",
            null
        )
    }
}
