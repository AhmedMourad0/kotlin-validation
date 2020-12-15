package dev.ahmedmourad.validation.compiler

import arrow.meta.CliPlugin
import arrow.meta.Meta
import arrow.meta.internal.Noop
import arrow.meta.invoke
import arrow.meta.phases.analysis.AnalysisHandler
import arrow.meta.quotes.*
import dev.ahmedmourad.validation.compiler.generators.FileManager
import dev.ahmedmourad.validation.compiler.generators.ViolationsGenerator
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

        val fileManager = FileManager(this)

        ViolationsGenerator(
            this,
            bindingTrace.bindingContext
        ).generateAllViolations().forEach { violationDescriptor ->
            fileManager.createFile(violationDescriptor)
        }

        AnalysisResult.RetryWithAdditionalRoots(
            bindingTrace.bindingContext, module, emptyList(), fileManager.generatedFiles
        )
    }
)
