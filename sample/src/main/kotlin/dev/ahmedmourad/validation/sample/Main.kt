package dev.ahmedmourad.validation.sample

import dev.ahmedmourad.validation.core.*

fun main() {

}

data class Password(val v: String) {
    companion object : Constrains<Password> {
        override val constraints = constraints {
            constraint(violation = "TooShort") {
                param("min") { 7 }
                on(Password::v) {
                    maxLength { 15 }
                    minLength(7)
                    //it's not final whether the params will be a lambda or a value, probably will provide both options
                    /* or */
                    validation {
                        length in 7..15
                    }
                }
                /* or */
                script { //script operates on all properties of the data class
                    v.length in 7..15
                }
            }
            constraint(violation = "ContainsNoDigits") {
                //...
            }
        }
    }
}

//Custom validations
fun <T : Any> ValidatorBuilder<T, String>.maxLength(max: () -> Int) = validation {
    length <= max()
}

//generates

sealed class PasswordViolations {
    data class TooShort(val min: Int)
    object ContainsNoDigits
}

//also generates top-level `validate` and other helper functions

/*...*/

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
        override val constraints = constraints {
            constraint(violation = "ShortV") {
                customScript()
                customScriptWithoutInterface()
            }
        }
    }
}
