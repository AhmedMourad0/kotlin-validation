package dev.ahmedmourad.validation.sample

import dev.ahmedmourad.validation.core.*
//import dev.ahmedmourad.validation.sample.validations.*

fun main() {

}


data class Rand<T : List<*>>(val v: List<T>?)
data class Nested(val x: String, val y: String?)
data class Password(val v: String, val n: Nested?) {
    companion object : Constrains<Password> {
        override val constraints by describe {
            constraint(violation = "TooShort") {
                param("min") { 7 }
                param("max") { Rand(emptyList()) }
                param("len") { 56.567 }
                on(Password::n) ifExists {
                    on(Nested::x) {
                        maxLength { 7 }
                        minLength(7)
                    }
                    on(Nested::y) ifExists {
                        maxLength { 7 }
                        minLength(7)
                    }
                    validation {
                        this.x.length == this.y?.length
                    }
                }
                on(Password::v) ifExists {
                    maxLength { it.toInt() }
                    minLength(7)
                    //it's not final whether the params will be a lambda or a value, probably will provide both options
                    /* or */
                    validation {
                        length in 7..15
                    }
                }
                on(Password::v) {
                    on(String::length) {
                        min(7)
                        max(14)
                    }
                }
                /* or */
                script { //script operates on all properties of the data class
                    v.length in 7..15
                }
            }
            val x = "aaa"
            constraint(violation = "ContainsNoDigits") {
                //...
            }
//            constraint(violation = "ContainsNoDigits$z") {
//                //...
//            }
//            constraint(violation = "ContainsNoDigits + " /*+ this@Companion.*/) {
//                //...
//            }
//            constraint(violation = "ContainsNoDigits + " + "x") {
//                //...
//            }
        }
    }
}

//Custom validations
fun ValidatorBuilder<Int>.min(min: Int) = validation {
    this >= min
}
//Custom validations
fun ValidatorBuilder<Int>.max(max: Int) = validation {
    this <= max
}



class RandConstrainer<T : List<*>>(val x: String, val m: T, val c: List<T>, d: Int) : Constrains<Rand<T>> {
    override val constraints by describe {
        constraint("ad") {

        }
    }
    companion object
}

//Custom validations
fun ValidatorBuilder<String>.maxLength(max: (String) -> Int) = validation {
    length <= max(this)
}

interface HasV {
    val v: String
}

//Custom scripts
//an interface is not mandatory, but it makes sense
fun <T : HasV> ConstraintBuilder<T>.customScript() = script {
    v.length > 44
}

//Or for a specific class
fun ConstraintBuilder<VSauce>.customScriptWithoutInterface() = script {
    v.length > 44
}

data class VSauce(override val v: String) : HasV {
    companion object : Constrains<VSauce> {
        override val constraints by describe {
            constraint(violation = "ShortV") {
                customScript()
                customScriptWithoutInterface()
            }
        }
    }
}
