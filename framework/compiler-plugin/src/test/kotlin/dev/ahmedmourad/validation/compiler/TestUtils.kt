package dev.ahmedmourad.validation.compiler

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import dev.ahmedmourad.validation.compiler.utils.OUTPUT_FOLDER
import org.intellij.lang.annotations.Language
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.JvmTarget
import org.junit.rules.TemporaryFolder

@Language("kotlin")
const val PACKAGE_AND_IMPORTS = """
    package dev.ahmedmourad.validation.compiler
    import dev.ahmedmourad.validation.core.*
    import dev.ahmedmourad.validation.core.validations.*
    import dev.ahmedmourad.validation.compiler.$OUTPUT_FOLDER.*
"""

@Language("kotlin")
const val MINIMAL_INT_VALIDATOR = """
    object IntValidator : Validator<Int> {
        override val constraints by describe {
            constraint(violation = "TooShort") { }
        }
    }
"""

fun prepareCompilation(
    temporaryFolder: TemporaryFolder,
    vararg sourceFiles: SourceFile
): KotlinCompilation {
    return KotlinCompilation().apply {
        workingDir = temporaryFolder.root
//        workingDir = File("E://test//dev//ahmedmourad//validation//compiler")
        kotlincArguments = listOf("-Xopt-in=kotlin.RequiresOptIn")
        compilerPlugins = listOf<ComponentRegistrar>(ValidationPlugin())
        inheritClassPath = true
        sources = sourceFiles.asList()
        verbose = false
        jvmTarget = JvmTarget.JVM_1_8.description
    }
}
