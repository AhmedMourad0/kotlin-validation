package dev.ahmedmourad.validation.compiler

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class ValidationContextGeneratorTests {

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

    @Test
    fun `constrainedAlias applies an alias to the constrained type`() {
        val successResult = compile(SourceFile.kotlin("Test.kt", """$PACKAGE_AND_IMPORTS
            
            @ConstrainerConfig(constrainedAlias = "DeluxeInt")
            object SomeConstrainer : Constrains<Int> {
                override val constraints by describe {
                    constraint("FirstViolation") { }
                }
            }

            fun main() {
                val context: DeluxeIntValidationContext = object : DeluxeIntValidationContext { }
            }
        """
        ))
        Assert.assertEquals(KotlinCompilation.ExitCode.OK, successResult.exitCode)
    }

    @Test
    fun `The ValidationContext interface inherits the type params of the constrainer`() {

        val failedResult = compile(SourceFile.kotlin("Test.kt", """$PACKAGE_AND_IMPORTS
            
            class SomeConstrainer<T : Any, L : List<T>, M> : Constrains<Int> {
                override val constraints by describe {
                    constraint("FirstViolation") { }
                }
            }

            fun main() {
                val context = object : IntValidationContext { }
            }
        """
        ))
        Assert.assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, failedResult.exitCode)

        val failedResult1 = compile(SourceFile.kotlin("Test.kt", """$PACKAGE_AND_IMPORTS
            
            class SomeConstrainer<T : Any, L : List<T>, M> : Constrains<Int> {
                override val constraints by describe {
                    constraint("FirstViolation") { }
                }
            }

            fun main() {
                val context = object : IntValidationContext<String, String, Int> { }
            }
        """
        ))
        Assert.assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, failedResult1.exitCode)

        val successResult = compile(SourceFile.kotlin("Test.kt", """$PACKAGE_AND_IMPORTS
            
            class SomeConstrainer<T : Any, L : List<T>, M> : Constrains<Int> {
                override val constraints by describe {
                    constraint("FirstViolation") { }
                }
            }

            fun main() {
                val context = object : IntValidationContext<String, List<String>, Int> { }
                val context1 = object : IntValidationContext<String, ArrayList<String>, Int> { }
            }
        """))
        Assert.assertEquals(KotlinCompilation.ExitCode.OK, successResult.exitCode)
    }

    @Test
    fun `The ValidationContext interface inherits the ValidationContext of every included constrainer`() {
        val successResult = compile(SourceFile.kotlin("Test.kt", """$PACKAGE_AND_IMPORTS
            
            object StringConstrainer : Constrains<String> {
                override val constraints by describe {
                    constraint("FirstViolation") { }
                }
            }

            class IntConstrainer<T : Any> : Constrains<Int> {
                override val constraints by describe {
                    constraint("FirstViolation") { }
                }
            }
            
            @ConstrainerConfig(constrainedAlias = "Int1")
            class Int1Constrainer<A : Any, B : List<A>> : Constrains<Int> {
                override val constraints by describe {
                    constraint("FirstViolation") {
                        include("v", { 4 }) { _, _ ->
                            IntConstrainer<A>()
                        }
                    }
                }
            }

            @ConstrainerConfig(constrainedAlias = "Int2")
            class Int2Constrainer<A : Any, A1 : List<A>> : Constrains<Int> {
                override val constraints by describe {
                    constraint("FirstViolation") {
                        include("v", { 4 }) { _, _ ->
                            Int1Constrainer<A, A1>()
                        }
                    }
                }
            }

            @ConstrainerConfig(constrainedAlias = "Int3")
            class Int3Constrainer<T : Any, L : List<T>> : Constrains<Int> {
                override val constraints by describe {
                    constraint("FirstViolation") {
                        include("i", { 4 }) { _, _ ->
                            Int2Constrainer<T, L>()
                        }
                        include("s", { "4" }) { _, _ ->
                            StringConstrainer
                        }
                    }
                }
            }

            object StringValidationContextInstance : StringValidationContext
            object IntValidationContextInstance : IntValidationContext<String>
            object Int1ValidationContextInstance : Int1ValidationContext<String, List<String>>
            object Int2ValidationContextInstance : Int2ValidationContext<String, List<String>>
            object Int3ValidationContextInstance : Int3ValidationContext<String, List<String>>

            fun main() {

                val i1: IntValidationContext<String> = Int1ValidationContextInstance

                val i2: IntValidationContext<String> = Int2ValidationContextInstance
                val i21: Int1ValidationContext<String, List<String>> = Int2ValidationContextInstance

                val i3: IntValidationContext<String> = Int3ValidationContextInstance
                val i31: Int1ValidationContext<String, List<String>> = Int3ValidationContextInstance
                val i32: Int2ValidationContext<String, List<String>> = Int3ValidationContextInstance
                val i33: StringValidationContext = Int3ValidationContextInstance
            }
        """
        ))
        Assert.assertEquals(KotlinCompilation.ExitCode.OK, successResult.exitCode)
    }

    private fun compile(vararg sourceFiles: SourceFile): KotlinCompilation.Result {
        return prepareCompilation(temporaryFolder, *sourceFiles).compile()
    }
}
