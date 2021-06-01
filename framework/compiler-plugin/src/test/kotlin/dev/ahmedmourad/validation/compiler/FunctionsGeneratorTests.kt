package dev.ahmedmourad.validation.compiler

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class FunctionsGeneratorTests {

    @Rule
    @JvmField
    var temporaryFolder: TemporaryFolder = TemporaryFolder()

    @Test
    fun `A ValidationContext interface is generated for each constrainer`() {
        val successResult = compile(SourceFile.kotlin("Test.kt", """$PACKAGE_AND_IMPORTS
            
            object SomeConstrainer : Constrains<Int> {
                override val constraints by describe {
                    constraint("FirstViolation") { }
                }
            }

            fun main() {
                val context: IntValidationContext = object : IntValidationContext { }
            }
        """))
        Assert.assertEquals(KotlinCompilation.ExitCode.OK, successResult.exitCode)
    }

    private fun compile(vararg sourceFiles: SourceFile): KotlinCompilation.Result {
        return prepareCompilation(temporaryFolder, *sourceFiles).compile()
    }
}
