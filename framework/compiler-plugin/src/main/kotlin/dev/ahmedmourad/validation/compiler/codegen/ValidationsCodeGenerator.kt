package dev.ahmedmourad.validation.compiler.codegen

import dev.ahmedmourad.validation.compiler.analysers.ConstraintsAnalyser
import dev.ahmedmourad.validation.compiler.codegen.validations.FunctionsGenerator
import dev.ahmedmourad.validation.compiler.codegen.validations.ValidationContextGenerator
import dev.ahmedmourad.validation.compiler.codegen.validations.ViolationsGenerator
import dev.ahmedmourad.validation.compiler.dsl.DslValidator
import dev.ahmedmourad.validation.compiler.files.FileBuilder
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.BindingContext
import java.io.File

//TODO: every one should follow the visibility of the least of the constrainer and constrained (public or internal only)
internal class ValidationsCodeGenerator(
    private val bindingContext: BindingContext,
    private val dslValidator: DslValidator
) : CodeGenerator {
    override fun generate(
        codeGenDir: File,
        module: ModuleDescriptor,
        projectFiles: Collection<KtFile>
    ) {
        val fileBuilder = FileBuilder(dslValidator)
        val violationsGenerator = ViolationsGenerator()
        val functionsGenerator = FunctionsGenerator()
        val validationContextGenerator = ValidationContextGenerator()
        ConstraintsAnalyser(bindingContext, dslValidator).analyse(projectFiles).forEach { descriptor ->
            fileBuilder.createFile(
                codeGenDir,
                descriptor,
                violationsGenerator,
                functionsGenerator,
                validationContextGenerator
            )
        }
    }
}
