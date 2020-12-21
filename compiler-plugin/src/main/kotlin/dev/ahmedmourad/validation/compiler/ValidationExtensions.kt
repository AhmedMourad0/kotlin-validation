package dev.ahmedmourad.validation.compiler

import arrow.meta.CliPlugin
import arrow.meta.Meta
import arrow.meta.internal.Noop
import arrow.meta.invoke
import arrow.meta.phases.analysis.AnalysisHandler
import arrow.meta.quotes.*
import dev.ahmedmourad.validation.compiler.files.FileManager
import dev.ahmedmourad.validation.compiler.analysers.ConstraintsAnalyser
import dev.ahmedmourad.validation.compiler.generators.HelperFunctionsGenerator
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
        val helperFunctionsGenerator = HelperFunctionsGenerator(verifier)

        ConstraintsAnalyser(
            bindingTrace.bindingContext,
            verifier
        ).analyse().forEach { constraintsDescriptor ->
            val violationsText = violationsGenerator.generate(constraintsDescriptor)
            val functionsText = helperFunctionsGenerator.generate(constraintsDescriptor).orEmpty()
            fileManager.createFile(
                constraintsDescriptor = constraintsDescriptor,
                imports = violationsGenerator.imports + helperFunctionsGenerator.imports,
                violationsText = violationsText,
                functionsText = functionsText
            )
        }

        AnalysisResult.RetryWithAdditionalRoots(
            bindingTrace.bindingContext, module, emptyList(), fileManager.generatedFiles
        )
    }
)
