package dev.ahmedmourad.validation.compiler

import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.cli.common.config.KotlinSourceRoot
import org.jetbrains.kotlin.cli.common.config.kotlinSourceRoots
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.com.intellij.openapi.extensions.LoadingOrder
import org.jetbrains.kotlin.com.intellij.openapi.extensions.impl.ExtensionPointImpl
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.extensions.ProjectExtensionDescriptor
import org.jetbrains.kotlin.utils.addToStdlib.cast
import java.io.File

internal data class CompilerContext(
    val messageCollector: MessageCollector,
    val configuration: CompilerConfiguration
) {
    val codeGenDir = createCodeGenDir(configuration)
}

private fun createCodeGenDir(configuration: CompilerConfiguration): File {

    val kotlinSourceRoots: List<KotlinSourceRoot> = configuration.kotlinSourceRoots

    fun kotlinValidationDir(parent: File): File {
        val file = File(parent, "kotlin-validation")
        check(file.exists() || file.mkdirs()) {
            "Could not create source generation directory: $file"
        }
        return file
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

    // This's here for testing purposes
    parentSequence.firstOrNull { it.name == "sources" }?.let {
        return kotlinValidationDir(File(it.parentFile, "build"))
    }

    throw IllegalStateException("Could not create source generation directory: $oneSourceFile")
}

internal fun <T : Any> ProjectExtensionDescriptor<T>.registerExtensionAsFirst(project: Project, extension: T) {
    project.extensionArea
        .getExtensionPoint(extensionPointName)
        .cast<ExtensionPointImpl<T>>()
        .registerExtension(extension, LoadingOrder.LAST)
}
