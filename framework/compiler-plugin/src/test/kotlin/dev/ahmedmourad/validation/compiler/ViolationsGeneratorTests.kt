package dev.ahmedmourad.validation.compiler

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class ViolationsGeneratorTests {

    @Rule
    @JvmField
    var temporaryFolder: TemporaryFolder = TemporaryFolder()

    @Test
    fun `A violations sealed class should be created for the constrainer with a child for each constraint`() {
        val successResult = compile(SourceFile.kotlin("Test.kt", """$PACKAGE_AND_IMPORTS
            
            object SomeConstrainer : Constrains<Int> {
                override val constraints by describe {
                    constraint("FirstViolation") { }
                    constraint("SecondViolation") { }
                    constraint("ThirdViolation") { }
                }
            }

            fun main() {

                val first: IntViolation = IntViolation.FirstViolation
                val second: IntViolation = IntViolation.SecondViolation

                val isItReallySealed = when (val third: IntViolation = IntViolation.ThirdViolation) {
                    IntViolation.FirstViolation -> 1
                    IntViolation.SecondViolation -> 2
                    IntViolation.ThirdViolation -> 3
                }
            }
        """))
        Assert.assertEquals(KotlinCompilation.ExitCode.OK, successResult.exitCode)
    }

    @Test
    fun `An object is created when there are no metas and a class is created when there are metas`() {
        val successResult = compile(SourceFile.kotlin("Test.kt", """$PACKAGE_AND_IMPORTS
            
            object SomeConstrainer : Constrains<Int> {
                override val constraints by describe {
                    constraint("FirstViolation") {
                        meta("v") { 4 }
                     }
                    constraint("SecondViolation") { }
                }
            }

            fun main() {

                val first: IntViolation = IntViolation.FirstViolation(44)
                
                val hello = when (val second: IntViolation = IntViolation.SecondViolation) {
                    is IntViolation.FirstViolation -> 1
                    IntViolation.SecondViolation -> 2
                }
            }
        """))
        Assert.assertEquals(KotlinCompilation.ExitCode.OK, successResult.exitCode)
    }

    @Test
    fun `constrainedAlias applies an alias to the constrained type`() {
        val successResult = compile(SourceFile.kotlin("Test.kt", """$PACKAGE_AND_IMPORTS
            
            object SomeConstrainer : Constrains<Int> {
                override val constraints by describe {
                    constraint("FirstViolation") { }
                }
            }

            @ConstrainerConfig(constrainedAlias = "DeluxeInt")
            object AnotherConstrainer : Constrains<Int> {
                override val constraints by describe {
                    constraint("SecondViolation") { }
                }
            }

            fun main() {

                val first = when (val f: IntViolation = IntViolation.FirstViolation) {
                    IntViolation.FirstViolation -> 1
                }

                val second = when (val s: DeluxeIntViolation = DeluxeIntViolation.SecondViolation) {
                    DeluxeIntViolation.SecondViolation -> 2
                }
            }
        """
        ))
        Assert.assertEquals(KotlinCompilation.ExitCode.OK, successResult.exitCode)
    }

    @Test
    fun `meta adds a property to the violation of the corresponding constraint`() {
        val successResult = compile(SourceFile.kotlin("Test.kt", """$PACKAGE_AND_IMPORTS
            
            object SomeConstrainer : Constrains<Int> {
                override val constraints by describe {
                    constraint("FirstViolation") {
                        meta("value") { 5 }
                        meta("another") { 4 to "Hi" }
                        meta("another1") { emptyList<String>() }
                     }
                }
            }

            object AnotherConstrainer : Constrains<String> {
                override val constraints by describe {
                    constraint("SecondViolation") {
                        meta("value") { 5 }
                     }
                }
            }

            fun main() {

                val first: IntViolation = IntViolation.FirstViolation(
                    value = 5,
                    another = 3 to "Hello",
                    another1 = emptyList<String>()
                )
                val second: StringViolation = StringViolation.SecondViolation(
                    value = 44
                )

                val f = when (first) {
                    is IntViolation.FirstViolation -> 1
                }

                val s = when (second) {
                    is StringViolation.SecondViolation -> 2
                }
            }
        """
        ))
        Assert.assertEquals(KotlinCompilation.ExitCode.OK, successResult.exitCode)
    }

    @Test
    fun `include adds a list of the violations of the included constrainer as a property of the violation of the corresponding constraint`() {
        val successResult = compile(SourceFile.kotlin("Test.kt", """$PACKAGE_AND_IMPORTS
            
            object IntConstrainer : Constrains<Int> {
                override val constraints by describe {
                    constraint("FirstViolation") { }
                }
            }

            @ConstrainerConfig(constrainedAlias = "LongInt")
            object LongIntConstrainer : Constrains<Int> {
                override val constraints by describe {
                    constraint("FirstViolation") { }
                }
            }

            object AnotherConstrainer : Constrains<String> {
                override val constraints by describe {
                    constraint("SecondViolation") {
                        meta("value") { 5 }
                        include("value1", { 4 }) { _, _ ->
                            IntConstrainer
                        }
                        include("value2", { 54 }) { _, _ ->
                            LongIntConstrainer
                        }
                    }
                }
            }

            fun main() {
                val first: StringViolation = StringViolation.SecondViolation(
                    value = 5,
                    value1 = emptyList<IntViolation>(),
                    value2 = emptyList<LongIntViolation>()
                )
            }
        """
        ))
        Assert.assertEquals(KotlinCompilation.ExitCode.OK, successResult.exitCode)
    }

    private fun compile(vararg sourceFiles: SourceFile): KotlinCompilation.Result {
        return prepareCompilation(temporaryFolder, *sourceFiles).compile()
    }
}
