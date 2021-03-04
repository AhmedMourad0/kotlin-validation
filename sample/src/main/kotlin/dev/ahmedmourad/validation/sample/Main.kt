package dev.ahmedmourad.validation.sample

import dev.ahmedmourad.validation.core.*
import dev.ahmedmourad.validation.core.validations.*

//import dev.ahmedmourad.validation.sample.validations.*

fun main() {

}

data class Rand<T : List<*>>(val v: List<T>?)
data class Nested(val x: String, val y: String?) {
    companion object : Constrains<Nested> {
        override val constraints by describe {
            constraint("LALALA") {

            }
        }
    }
}

interface X
class Y : X

data class Model(val v: String, val n: Nested?, val l1: Array<Nested>, val l2: List<Nested?>, val x: X) {
    companion object : Constrains<Model> {
        override val constraints by describe {
            constraint(violation = "TooShort") {
                param("min") { 7 }
                param("max") { Rand(emptyList()) }
                param("len") { 56.567 }
                include("nestedViolations", Model::n) {
                    Nested.Companion
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
                        it.x.length == it.y?.length
                    }
                }
                on(Model::x) {
                    enforceAll {

                    }
                    enforceAtLeastOne {

                    }
                    enforceNone {

                    }
                }
                on(Model::x) {
                    ifIs<Y> {

                    }
                    mustBeA<Y> {

                    }
                }
                on(Model::l1) {
                    all {
                        on(Nested::y) ifExists {

                        }
                        on(Nested::x) {

                        }
                    }
                    any {
                        //...
                    }
                    none {
                        //...
                    }
                }
                on(Model::l2) {
                    all {
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
                    validation { it.length in 7..15 }
                }
                /* or */
                validation { //validations can also operates on all properties of the data class
                    it.v.length in 7..15
                }
            }
            constraint(violation = "ContainsNoDigits") {
                //...
            }
        }
    }
}

//Custom validations
fun <T : List<*>> Validator<List<T>>.customValidation() = validation {
    it.size > 5
}

//Dealing with type parameters or extra parameters
class RandConstrainer<T : List<*>, M>(val x: String, val m: T, val c: List<T>, d: M) : Constrains<Rand<T>> {
    override val constraints by describe {
        constraint("ad") {
            on(Rand<T>::v) ifExists {
                customValidation()
            }
        }
    }
}

//Custom scripts
fun ConstraintBuilder<VSauce>.customScript() = validation {
    it.v.length > 44
}

data class VSauce(val v: String) {
    companion object : Constrains<VSauce> {
        override val constraints by describe {
            constraint(violation = "ShortV") {
                customScript()
            }
        }
    }
}
