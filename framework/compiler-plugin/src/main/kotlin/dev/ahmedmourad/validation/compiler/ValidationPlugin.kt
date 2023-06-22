package dev.ahmedmourad.validation.compiler

import com.intellij.mock.MockProject
import com.intellij.openapi.project.Project
import dev.ahmedmourad.validation.compiler.codegen.ValidationsCodeGenerator
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.config.KotlinSourceRoot
import org.jetbrains.kotlin.cli.common.config.kotlinSourceRoots
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.com.intellij.openapi.extensions.LoadingOrder
import org.jetbrains.kotlin.com.intellij.openapi.extensions.impl.ExtensionPointImpl
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.extensions.ProjectExtensionDescriptor
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension
import org.jetbrains.kotlin.resolve.jvm.extensions.AnalysisHandlerExtension
import org.jetbrains.kotlin.utils.addToStdlib.cast
import java.io.File

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
