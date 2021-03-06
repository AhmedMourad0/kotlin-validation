package dev.ahmedmourad.validation.compiler

import arrow.meta.CliPlugin
import arrow.meta.Meta
import arrow.meta.internal.Noop
import arrow.meta.invoke
import arrow.meta.phases.analysis.AnalysisHandler
import arrow.meta.quotes.*
import dev.ahmedmourad.validation.compiler.files.FileManager
import dev.ahmedmourad.validation.compiler.analysers.ConstraintsAnalyser
import dev.ahmedmourad.validation.compiler.generators.FunctionsGenerator
import dev.ahmedmourad.validation.compiler.generators.ValidationContextGenerator
import dev.ahmedmourad.validation.compiler.generators.ViolationsGenerator
import dev.ahmedmourad.validation.compiler.verifier.DslVerifier
import org.jetbrains.kotlin.analyzer.AnalysisResult
import org.jetbrains.kotlin.psi.*
import java.util.*

internal val Meta.kotlinValidation: CliPlugin
    get() = "KotlinValidation" x@{
        meta(validationsAnalysisHandler())
    }

internal fun Meta.validationsAnalysisHandler(): AnalysisHandler = analysis(
    doAnalysis = Noop.nullable7<AnalysisResult>(),
    analysisCompleted = { _, module, bindingTrace, _ ->

        val verifier = DslVerifier(this, bindingTrace.bindingContext)
        val fileManager = FileManager(this, verifier)
        val violationsGenerator = ViolationsGenerator()
        val functionsGenerator = FunctionsGenerator(verifier, bindingTrace.bindingContext)
        val validationContextGenerator = ValidationContextGenerator(bindingTrace.bindingContext)

        ConstraintsAnalyser(
            bindingTrace.bindingContext,
            verifier
        ).analyse().forEach { constraintsDescriptor ->
            fileManager.createFile(
                constraintsDescriptor,
                violationsGenerator,
                functionsGenerator,
                validationContextGenerator
            )
        }

        AnalysisResult.RetryWithAdditionalRoots(
            bindingTrace.bindingContext, module, emptyList(), fileManager.generatedFiles
        )
    }
)
