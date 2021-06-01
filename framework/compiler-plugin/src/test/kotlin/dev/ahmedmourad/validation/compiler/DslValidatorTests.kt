package dev.ahmedmourad.validation.compiler

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.SourceFile.Companion.kotlin
import org.intellij.lang.annotations.Language
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class DslValidatorTests {

    @Rule
    @JvmField
    var temporaryFolder: TemporaryFolder = TemporaryFolder()

    @Test
    fun `@MustBeValid annotated classes can only be instantiated inside their own ValidationContext`() {

        @Language("kotlin")
        val fileHeaderText = """$PACKAGE_AND_IMPORTS
            @MustBeValid
            class Model internal constructor(val n: Int) {
                internal constructor() : this(5)
                companion object : Constrains<Model> {
                    override val constraints by describe {
                        constraint("TooLow") {
                            on(Model::n) {
                                max(7)
                            }
                        }
                    }
                }
            }
        """

        val failedResult = compile(
            kotlin("FailedTest.kt", """
                $fileHeaderText
    
                fun main() {
                    Model(1)
                }
            """)
        )
        assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, failedResult.exitCode)
        assertTrue(failedResult.messages.contains("Only validated instances of Model can be created"))

        val successResult = compile(
            kotlin("Test.kt", """
                $fileHeaderText
    
                fun main() {
                    Model.validate {
                        Model(1)
                    }
                    Model.isValid {
                        Model(1)
                    }
                }
            """)
        )
        assertEquals(KotlinCompilation.ExitCode.OK, successResult.exitCode)
    }

    @Test
    fun `param can only be called inside the constraint block`() {

        val failedResult = compile(
            kotlin("FailedTest.kt", """$PACKAGE_AND_IMPORTS
                fun <T : Any> ConstraintBuilder<T>.something() {
                    param("illegal") { 4 }
                }
            """)
        )
        assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, failedResult.exitCode)
        assertTrue(failedResult.messages.contains(
                "`param` can only be called directly inside a `constraint` block"
        ))

        val successResult = compile(
            kotlin("Test.kt", """$PACKAGE_AND_IMPORTS
                class Model(val n: Int) {
                    companion object : Constrains<Model> {
                        override val constraints by describe {
                            constraint("Something") {
                                param("legal") { 4 }
                            }
                        }
                    }
                }
            """)
        )
        assertEquals(KotlinCompilation.ExitCode.OK, successResult.exitCode)
    }

    @Test
    fun `include can only be called inside the constraint block`() {

        val failedResult = compile(
            kotlin("FailedTest.kt", """$PACKAGE_AND_IMPORTS
                $MINIMAL_INT_CONSTRAINER
                data class Model(val n: Int)
                fun ConstraintBuilder<Model>.something() {
                    include("legalInclude", Model::n) { _, _ -> IntConstrainer }
                }
            """)
        )
        assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, failedResult.exitCode)
        assertTrue(failedResult.messages.contains(
                "`include` can only be called directly inside a `constraint` block"
        ))

        val successResult = compile(
            kotlin("Test.kt", """$PACKAGE_AND_IMPORTS
                $MINIMAL_INT_CONSTRAINER
                class Model(val n: Int) {
                    companion object : Constrains<Model> {
                        override val constraints by describe {
                            constraint("TooLow") {
                                include("legal", Model::n) { _, _ -> IntConstrainer }
                            }
                        }
                    }
                }
            """)
        )
        assertEquals(KotlinCompilation.ExitCode.OK, successResult.exitCode)
    }

    @Test
    fun `constraint can only be called inside the describe block`() {

        val failedResult = compile(
            kotlin("FailedTest.kt", """$PACKAGE_AND_IMPORTS
                fun <T> ConstraintsBuilder<T>.something() {
                    constraint("Something") { }
                }
            """)
        )
        assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, failedResult.exitCode)
        assertTrue(failedResult.messages.contains(
                "`constraint` can only be called directly inside a `describe` block"
        ))

        val successResult = compile(
            kotlin("Test.kt", """$PACKAGE_AND_IMPORTS
                object SomeConstrainer : Constrains<Int> {
                    override val constraints by describe {
                        constraint("Something") { }
                    }
                }
            """)
        )
        assertEquals(KotlinCompilation.ExitCode.OK, successResult.exitCode)
    }

    @Test
    fun `violation names inside a constrainer must be unique`() {

        val failedResult = compile(
            kotlin("FailedTest.kt", """$PACKAGE_AND_IMPORTS
                object SomeConstrainer : Constrains<Int> {
                    override val constraints by describe {
                        constraint("Something") { }
                        constraint("Something") { }
                    }
                }
            """)
        )
        assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, failedResult.exitCode)
        assertTrue(failedResult.messages.contains("Duplicate violation: Something"))

        val successResult = compile(
            kotlin("Test.kt", """$PACKAGE_AND_IMPORTS
                @ConstrainerConfig(constrainedAlias = "SomeInt")
                object SomeConstrainer : Constrains<Int> {
                    override val constraints by describe {
                        constraint("Something") { }
                    }
                }
                @ConstrainerConfig(constrainedAlias = "AnotherInt")
                object AnotherConstrainer : Constrains<Int> {
                    override val constraints by describe {
                        constraint("Something") { }
                    }
                }
            """
            )
        )
        assertEquals(KotlinCompilation.ExitCode.OK, successResult.exitCode)
    }

    @Test
    fun `param names inside a constraint must be unique`() {

        val failedResult = compile(
            kotlin("FailedTest.kt", """$PACKAGE_AND_IMPORTS
                $MINIMAL_INT_CONSTRAINER
                @ConstrainerConfig(constrainedAlias = "SomeInt")
                class SomeConstrainer : Constrains<Int> {
                    override val constraints by describe {
                        constraint("Something") {

                            param("name") { "Ahmed" }
                            param("name") { "Not Ahmed" }

                            param("aname") { "Ahmed" }
                            param("aname") { 5 }

                            include("bname", { 22 }) { _, _ -> IntConstrainer }
                            include("bname", { 4 }) { _, _ -> IntConstrainer }

                            param("cname") { "Ahmed" }
                            include("cname", { 4 }) { _, _ -> IntConstrainer }

                            param("dname") { "Ahmed" }
                        }
                    }
                }
            """
            )
        )
        println("nnn"+failedResult.messages+"nnn")
        assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, failedResult.exitCode)
        assertTrue(failedResult.messages.contains("Duplicate violation param: name"))
        assertTrue(failedResult.messages.contains("Duplicate violation param: aname"))
        assertTrue(failedResult.messages.contains("Duplicate violation param: bname"))
        assertTrue(failedResult.messages.contains("Duplicate violation param: cname"))
        assertFalse(failedResult.messages.contains("Duplicate violation param: dname"))

        val successResult = compile(
            kotlin("Test.kt", """$PACKAGE_AND_IMPORTS
                $MINIMAL_INT_CONSTRAINER
                @ConstrainerConfig(constrainedAlias = "SomeInt")
                object SomeConstrainer : Constrains<Int> {
                    override val constraints by describe {
                        constraint("Something") {
                            param("name") { "Ahmed" }
                            param("country") { "Egypt" }
                            include("ageViolations", { 22 }) { _, _ -> IntConstrainer }
                            include("heightViolations", { 185 }) { _, _ -> IntConstrainer }
                        }
                        constraint("AnotherThing") {
                            param("name") { "Ahmed" }
                            param("country") { "Egypt" }
                            include("ageViolations", { 22 }) { _, _ -> IntConstrainer }
                            include("heightViolations", { 185 }) { _, _ -> IntConstrainer }
                        }
                    }
                }
            """
            )
        )
        assertEquals(KotlinCompilation.ExitCode.OK, successResult.exitCode)
    }

    @Test
    fun `compilation fails for all invalid violation name identifiers`() {

        fun case(index: Int, violation: String): String {
            return """
                object SomeConstrainer$index : Constrains<Int> {
                    override val constraints by describe {
                        val text = "Hello"
                        constraint($violation) { }
                    }
                }
            """
        }

        val illegalIdentifierResult = compile(
            kotlin("FailingTest.kt", listOf(
                "\"1Something\"",
                "\"Some thing\"",
                "\\\$Something\"",
                "\"Some\\\$thing\"",
                "\"@Something\"",
                "java.util.UUID.randomUUID().toString()"
            ).mapIndexed { index, s ->
                case(index, s)
            }.joinToString("\n", prefix = "$PACKAGE_AND_IMPORTS\n"))
        )

        assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, illegalIdentifierResult.exitCode)
        assertTrue(illegalIdentifierResult.messages.contains("Illegal class identifier"))
        assertFalse(illegalIdentifierResult.messages.contains("Violation name must be a String literal"))

        val nonStringLiteralResult = compile(
            kotlin("Test.kt", """$PACKAGE_AND_IMPORTS
                ${case(99, "text")}
            """.trimIndent())
        )
        assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, nonStringLiteralResult.exitCode)
        assertTrue(nonStringLiteralResult.messages.contains("Violation name must be a String literal"))
        assertFalse(nonStringLiteralResult.messages.contains("Illegal class identifier"))
    }

    @Test
    fun `compilation passes for all valid violation name identifiers`() {
        val successResult = compile(
            kotlin("Test.kt", """$PACKAGE_AND_IMPORTS
                object SomeConstrainer : Constrains<Int> {
                    override val constraints by describe {
                        constraint("Something") { }
                        constraint("anotherThing") { }
                        constraint("OneMore" + "Thing") { }
                        constraint(${"\"\"\""}JustOneMoreThing${"\"\"\""}) { }
                        constraint("A" + 55.toString()) { }
                        constraint("B" + (55 + 1).toString()) { }
                    }
                }
            """)
        )
        assertEquals(KotlinCompilation.ExitCode.OK, successResult.exitCode)
    }

    @Test
    fun `compilation fails for all invalid param name identifiers`() {

        fun case(index: Int, param: String): String {
            return """
                object SomeConstrainer$index : Constrains<Int> {
                    override val constraints by describe {
                        val text = "Hello"
                        constraint("SomeConstraint") {
                            param($param) { 4 }
                        }
                    }
                }
            """
        }

        val illegalIdentifierResult = compile(
            kotlin("FailingTest.kt", listOf(
                "\"1Something\"",
                "\"Some thing\"",
                "\\\$Something\"",
                "\"Some\\\$thing\"",
                "\"@Something\"",
                "java.util.UUID.randomUUID().toString()"
            ).mapIndexed { index, s ->
                case(index, s)
            }.joinToString("\n", prefix = "$PACKAGE_AND_IMPORTS\n"))
        )

        assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, illegalIdentifierResult.exitCode)
        assertTrue(illegalIdentifierResult.messages.contains("Illegal property identifier"))
        assertFalse(illegalIdentifierResult.messages.contains("Param name must be a String literal"))

        val nonStringLiteralResult = compile(
            kotlin("Test.kt", """$PACKAGE_AND_IMPORTS
                ${case(99, "text")}
            """.trimIndent())
        )
        assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, nonStringLiteralResult.exitCode)
        assertTrue(nonStringLiteralResult.messages.contains("Param name must be a String literal"))
        assertFalse(nonStringLiteralResult.messages.contains("Illegal property identifier"))
    }

    @Test
    fun `compilation passes for all valid param name identifiers`() {
        val successResult = compile(
            kotlin("Test.kt", """$PACKAGE_AND_IMPORTS
                object SomeConstrainer : Constrains<Int> {
                    override val constraints by describe {
                        constraint("SomeConstraint") {
                            param("Something") { 4 }
                            param("anotherThing") { 4 }
                            param("OneMore" + "Thing") { 4 }
                            param(${"\"\"\""}JustOneMoreThing${"\"\"\""}) { 4 }
                            param("A" + 55.toString()) { 4 }
                            param("B" + (55 + 1).toString()) { 4 }
                        }
                    }
                }
            """)
        )
        assertEquals(KotlinCompilation.ExitCode.OK, successResult.exitCode)
    }

    @Test
    fun `compilation fails for all invalid constrained alias identifiers`() {

        fun case(index: Int, alias: String): String {
            return """
                @ConstrainerConfig(constrainedAlias = $alias)
                object SomeConstrainer$index : Constrains<Int> {
                    override val constraints by describe {
                        constraint("SomeConstraint") { }
                    }
                }
            """
        }

        val illegalIdentifierResult = compile(
            kotlin("Test.kt", listOf(
                "\"1Something\"",
                "\"Some thing\"",
                "\\\$Something\"",
                "\"Some\\\$thing\"",
                "\"@Something\"",
                "java.util.UUID.randomUUID().toString()"
            ).mapIndexed { index, s ->
                case(index, s)
            }.joinToString("\n", prefix = "$PACKAGE_AND_IMPORTS\n"))
        )
        assertEquals(KotlinCompilation.ExitCode.COMPILATION_ERROR, illegalIdentifierResult.exitCode)
        assertTrue(illegalIdentifierResult.messages.contains("Illegal class identifier"))
    }

    @Test
    fun `compilation passes for all valid constrained alias identifiers`() {
        
        fun case(index: Int, alias: String): String {
            return """
                @ConstrainerConfig(constrainedAlias = $alias)
                object SomeConstrainer$index : Constrains<Int> {
                    override val constraints by describe {
                        constraint("SomeConstraint") { }
                    }
                }
            """
        }

        val legalIdentifierResult = compile(
            kotlin("Test.kt", listOf(
                "\"Something\"",
                "\"anotherThing\"",
                "\"OneMore\" + \"Thing\"",
                "\"\"\"JustOneMoreThing\"\"\"",
                "\"A\" + 55.toString()",
                "\"B\" + (55 + 1).toString()"
            ).mapIndexed { index, s ->
                case(index, s)
            }.joinToString("\n", prefix = "$PACKAGE_AND_IMPORTS\n"))
        )
        assertEquals(KotlinCompilation.ExitCode.OK, legalIdentifierResult.exitCode)
    }

    private fun compile(vararg sourceFiles: SourceFile): KotlinCompilation.Result {
        return prepareCompilation(temporaryFolder, *sourceFiles).compile()
    }
}
