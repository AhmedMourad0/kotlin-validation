package dev.ahmedmourad.validation.compiler

import arrow.meta.CliPlugin
import arrow.meta.Meta
import arrow.meta.internal.Noop
import arrow.meta.invoke
import arrow.meta.phases.CompilerContext
import arrow.meta.phases.ExtensionPhase
import arrow.meta.phases.analysis.AnalysisHandler
import arrow.meta.phases.analysis.isAnnotatedWith
import arrow.meta.phases.resolve.synthetics.SyntheticResolver
import arrow.meta.quotes.*
import arrow.meta.quotes.classorobject.allConstructors
import dev.ahmedmourad.validation.compiler.files.FileManager
import dev.ahmedmourad.validation.compiler.analysers.ConstraintsAnalyser
import dev.ahmedmourad.validation.compiler.generators.FunctionsGenerator
import dev.ahmedmourad.validation.compiler.generators.ValidationContextGenerator
import dev.ahmedmourad.validation.compiler.generators.ViolationsGenerator
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_MUST_BE_VALID_ANNOTATION
import dev.ahmedmourad.validation.compiler.dsl.DslValidator
import org.jetbrains.kotlin.analyzer.AnalysisResult
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageUtil
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor
import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.BindingContext
import java.util.*

internal val Meta.kotlinValidation: CliPlugin
    get() = "KotlinValidation" x@{
        meta(
            validationsAnalysisHandler(),
            validationsSyntheticResolver(),
            validationsClassDeclarationQuote(this)
        )
    }

internal fun Meta.validationsAnalysisHandler(): AnalysisHandler = analysis(
    doAnalysis = Noop.nullable7<AnalysisResult>(),
    analysisCompleted = { _, module, bindingTrace, _ ->

        //TODO: every one should follow the visibility of the least of the constrainer and constrained (public or internal only)
        val dslValidator = DslValidator(this, bindingTrace.bindingContext)
        val fileManager = FileManager(this, dslValidator)
        val violationsGenerator = ViolationsGenerator()
        val functionsGenerator = FunctionsGenerator()
        val validationContextGenerator = ValidationContextGenerator()

        ConstraintsAnalyser(
            bindingTrace.bindingContext,
            dslValidator
        ).analyse().forEach { constraintsDescriptor ->
            fileManager.createFile(
                constraintsDescriptor,
                violationsGenerator,
                functionsGenerator,
                validationContextGenerator
            )
        }

        AnalysisResult.RetryWithAdditionalRoots(
            bindingTrace.bindingContext,
            module,
            emptyList(),
            fileManager.generatedFiles
        )
    }
)

internal fun Meta.validationsSyntheticResolver(): SyntheticResolver = syntheticResolver(
    generateSyntheticMethods = { thisDescriptor: ClassDescriptor,
                                 name: Name,
                                 bindingContext: BindingContext,
                                 fromSupertypes: List<SimpleFunctionDescriptor>,
                                 result: MutableCollection<SimpleFunctionDescriptor> ->
        if (name.asString() == "copy" &&
            thisDescriptor.annotations.hasAnnotation(FqName(FQ_NAME_MUST_BE_VALID_ANNOTATION))
        ) {
            result.clear()
        }
    }
)

//TODO: also enforce no vars
//TODO: this's just poc, it's terrible, it will be changed when typed quotes are released
internal fun Meta.validationsClassDeclarationQuote(cc: CompilerContext): ExtensionPhase = classDeclaration(cc, {
    this.isAnnotatedWith("@MustBeValid".toRegex())
}) {
    this.allConstructors.value.forEach {
        if (!it.hasModifier(KtTokens.INTERNAL_KEYWORD)) {
            cc.messageCollector?.report(
                CompilerMessageSeverity.ERROR,
                "All constructors of @MustBeValid annotated classes must be internal",
                MessageUtil.psiElementToMessageLocation(it)
            )
        }
    }
    Transform.empty
}
