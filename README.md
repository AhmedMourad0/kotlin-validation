# Kotlin-Validation _(Under Development)_
A multiplatform, declarative, flexible and type-safe Kotlin validation framework.

## Target Syntax

```kotlin
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
```

```kotlin
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
```

License
-------

    Copyright (C) 2020 Ahmed Mourad

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 [snapshots]: https://oss.sonatype.org/content/repositories/snapshots/
 