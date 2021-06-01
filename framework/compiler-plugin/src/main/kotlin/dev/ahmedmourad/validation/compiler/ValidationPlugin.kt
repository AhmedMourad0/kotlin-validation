package dev.ahmedmourad.validation.compiler

import dev.ahmedmourad.validation.compiler.codegen.ValidationsCodeGenerator
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.config.KotlinSourceRoot
import org.jetbrains.kotlin.cli.common.config.kotlinSourceRoots
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.com.intellij.mock.MockProject
import org.jetbrains.kotlin.com.intellij.openapi.extensions.LoadingOrder
import org.jetbrains.kotlin.com.intellij.openapi.extensions.impl.ExtensionPointImpl
import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.extensions.ProjectExtensionDescriptor
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension
import org.jetbrains.kotlin.resolve.jvm.extensions.AnalysisHandlerExtension
import org.jetbrains.kotlin.utils.addToStdlib.cast
import java.io.File
import java.lang.IllegalStateException

internal data class CompilerContext(
    val messageCollector: MessageCollector,
    val configuration: CompilerConfiguration
) {
    val codeGenDir = createCodeGenDir(configuration)
}

class ValidationPlugin : ComponentRegistrar {
    override fun registerProjectComponents(
        project: MockProject,
        configuration: CompilerConfiguration
    ) {

        val messageCollector = configuration.get(
            CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY,
            MessageCollector.NONE
        )

        val ctx = CompilerContext(
            messageCollector,
            configuration
        )

        val generators = listOf(CodeGeneratorFactory(::ValidationsCodeGenerator))

        SyntheticResolveExtension.registerExtensionAsFirst(
            project,
            ValidationSyntheticResolveExtension(messageCollector)
        )

        AnalysisHandlerExtension.registerExtensionAsFirst(
            project,
            ValidationAnalysisHandlerExtension(ctx, generators)
        )
    }
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

private fun <T : Any> ProjectExtensionDescriptor<T>.registerExtensionAsFirst(project: Project, extension: T) {
    project.extensionArea
        .getExtensionPoint(extensionPointName)
        .cast<ExtensionPointImpl<T>>()
        .registerExtension(extension, LoadingOrder.LAST, project)
}
