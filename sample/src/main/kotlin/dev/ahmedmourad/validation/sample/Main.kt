package dev.ahmedmourad.validation.sample

import dev.ahmedmourad.validation.core.*
import dev.ahmedmourad.validation.core.validations.*

//import dev.ahmedmourad.validation.sample.validations.*

fun main() {
//    Model.validate {
//        Model(
//            "", null, emptyArray(), emptyList(), object : X {}
//        ).copy()
//    }
}

@ValidatorConfig(subjectAlias = "LongInt")
object IntValidator : Validator<Int> {
    override val constraints by describe {
        constraint(violation = "TooShort") { }
    }
}

@ValidatorConfig(subjectAlias = "SomeInt")
object SomeValidator : Validator<Int> {
    override val constraints by describe {
        constraint("Something") {
            meta("name") { "Ahmed" }
//            meta("name1") { "Ahmed" }
            meta("country") { "Egypt" }
            include("ageViolations") { 22 to IntValidator }
            include("heightViolations") { 185 to IntValidator }
        }
        constraint("AnotherThing") {
            meta("name") { "Ahmed" }
            meta("country") { "Egypt" }
            include("ageViolations") { 22 to IntValidator }
            include("heightViolations") { 185 to IntValidator }
        }
    }
}

data class Rand<T : List<*>>(val v: List<T>?)
data class Nested(val x: String, val y: String?) {
    companion object : Validator<Nested> {
        override val constraints by describe {
            constraint("LALALA") {

            }
        }
    }
}

interface X

@MustBeValid
data class Model internal constructor(
    val v: String,
    val n: Nested?,
    val l1: Array<Nested>,
    val l2: List<Nested?>,
    val x: X
) {
    internal constructor(n: Nested?, l1: Array<Nested>, l2: List<Nested?>, x: X) : this("v", n, l1, l2, x)
    companion object : Validator<Model> {
        override val constraints by describe {
            constraint(violation = "TooShort") {
                meta<Number>("min") { 7 }
                meta("max") { Rand(emptyList()) }
                meta("len") { 56.567 }
                include("nestedViolations") {
                    Model::n to Nested.Companion
                }

                on(Model::n) ifExists {
                    on(Nested::x) {
                        maxLength { 14 }
                        minLength(7)
                    }
                    on(Nested::y) ifExists {
                        maxLength { 14 }
                        minLength(7)
                        startsWithChar { '3' }
                        isEqualTo("")
                        startsWith { "" }
                    }
                    validation {
                        subject.x.length == subject.y?.length
                    }
                }
                on(Model::x) {
                    allOf {

                    }
                    anyOf {

                    }
                    noneOf {

                    }
                }
                on(Model::l1) {
                    forAll {
                        on(Nested::y) ifExists {

                        }
                        on(Nested::x) {

                        }
                    }
                    forAny {
                        //...
                    }
                    forNone {
                        //...
                    }
                }
                on(Model::l2) {
                    forAll {
                        ifExists {
                            on(Nested::y) ifExists {

                            }
                            on(Nested::x) {

                            }
                        }
                    }
                }
                on(Model::v) {
                    on(String::length) {
                        min(7)
                        max(14)
                    }
                    maxLength { it.toInt() }
                    minLength(7)
                    validation { subject.length in 7..15 }
                }
                /* or */
                validation { //validations can also operates on all properties of the data class
                    subject.v.length in 7..15
                }
            }
            constraint(violation = "ContainsNoDigits") {
                //...
            }
        }
    }
}

//Custom validations
fun <T : List<*>> Constraint<List<T>>.customValidation() = validation {
    subject.size > 5
}

//Dealing with type parameters or extra parameters
class RandValidator<T : List<*>, M>(val x: String, val m: T, val c: List<T>, d: M) : Validator<Rand<T>> {
    override val constraints by describe {
        constraint("ad") {
            val name = evaluate { subject.v }
//            meta<M>("m") { throw RuntimeException() }
//            meta<T>("t") { throw RuntimeException() }
            on(Rand<T>::v) ifExists {
                customValidation()
            }
        }
    }
}

//Custom scripts
fun ConstraintBuilder<VSauce>.customScript() = validation {
    subject.v.length > 44
}

data class VSauce(val v: String) {
    companion object : Validator<VSauce> {
        override val constraints by describe {
            constraint(violation = "ShortV") {
                customScript()
            }
        }
    }
}
