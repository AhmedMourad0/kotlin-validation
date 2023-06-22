package dev.ahmedmourad.validation.compiler

import com.intellij.openapi.project.Project
import dev.ahmedmourad.validation.compiler.codegen.CodeGenerator
import dev.ahmedmourad.validation.compiler.dsl.DslValidator
import org.jetbrains.kotlin.analyzer.AnalysisResult
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.container.ComponentProvider
import org.jetbrains.kotlin.context.ProjectContext
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.BindingTrace
import org.jetbrains.kotlin.resolve.extensions.AnalysisHandlerExtension

internal fun interface CodeGeneratorFactory {
    fun create(bindingContext: BindingContext, dslValidator: DslValidator): CodeGenerator
}

internal class ValidationAnalysisHandlerExtension(
    private val ctx: CompilerContext,
    private val codeGeneratorsFactories: List<CodeGeneratorFactory>
) : AnalysisHandlerExtension {

    private var didRecompile = false

    override fun doAnalysis(
        project: Project,
        module: ModuleDescriptor,
        projectContext: ProjectContext,
        files: Collection<KtFile>,
        bindingTrace: BindingTrace,
        componentProvider: ComponentProvider
    ): AnalysisResult? {


//        val resolveSession = componentProvider.get<ResolveSession>()
//
//        resolveSession.

        // Tell the compiler that we have something to do in the analysisCompleted() method if
        // necessary.
        return null
        return if (!didRecompile) AnalysisResult.EMPTY else null
    }

    override fun analysisCompleted(
        project: Project,
        module: ModuleDescriptor,
        bindingTrace: BindingTrace,
        files: Collection<KtFile>
    ): AnalysisResult? {

//        ctx.messageCollector.report(
//            CompilerMessageSeverity.ERROR,
//            files.size.toString(),
//            null
//        )

        if (didRecompile) {
            return super.analysisCompleted(project, module, bindingTrace, files)
        }
        didRecompile = true

        ctx.codeGenDir.listFiles()?.forEach {
            check(it.deleteRecursively()) {
                "Could not clean file: $it"
            }
        }

        val dslValidator = DslValidator(bindingTrace.bindingContext, ctx.messageCollector)

        codeGeneratorsFactories.map {
            it.create(bindingTrace.bindingContext, dslValidator)
        }.forEach { codeGenerator ->
            codeGenerator.generate(ctx.codeGenDir, module, files)
        }

        return AnalysisResult.RetryWithAdditionalRoots(
            bindingContext = bindingTrace.bindingContext,
            moduleDescriptor = module,
            additionalJavaRoots = emptyList(),
            additionalKotlinRoots = listOf(ctx.codeGenDir),
            addToEnvironment = true
        )
    }
}
