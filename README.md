# Kotlin-Validation
#### *(Under Development)*
A multiplatform, declarative, flexible and type-safe Kotlin validation framework.

## Motivation
Kotlin is a fantastic language, however, it becomes quite challenging when it comes to creating robust domain models that follow strict domain rules, it turns into pure boilerplate when you decide to compose those models.

Other available validation libraries rely on string matching which is a no-go if you seek type safety.

## Terminology
- **Validator:** The class containing the constraints
- **Constraint:** Represents a domain rule of a type, can be a combination of validations and validators
- **Violation:** The product of failing to satisfy a constraint
- **Validation:** A single type-specific check, such as `maxLength` or `isNotBlank`
- **Validation Scope:** Defines what subject the validations are applied to
- **Subject:** The item that the validations are applied to, starts as the main item passed to the validator but changes when the validation scope is changed

## Implementation
### Quick Validators
```kotlin
val nameValidator = validator<String> {
    isNotBlank()
    lengthIn(5..20)
}
nameValidator.matchesAll("123456") //true
nameValidator.matchesAll("1234") //false
nameValidator.matchesAll("") //false
```
### Type Validators
#### Constraints
You can define the domain rules for any type by implementing the `Validator` interface:
```kotlin
object PasswordValidator : Validator<String> {
    override val constraints by describe {
        constraint("TooShort") {
            minLength(7)
        }
        constraint("NoSymbols") {
            listOf('@', '!', '^').forEach { char ->
                doesNotContainChar(char)
            }
        }
    }
}
```
Each domain rule for a type is defined by a `constraint` and for each constraint the compiler plugin generates a violation entry in a sealed hierarchy as follows:
```kotlin
//AUTO GENERATED BY THE PLUGIN
sealed interface StringViolation {
    object TooShort : StringViolation
    object NoSymbols : StringViolation
}
```
*The validator doesn't need to be an object, it can also be a class and can have value and type parameters.*
#### Aliases
You can define an alias for the validation type using the `ValidatorConfig` annotation as follows:
```kotlin
@ValidatorConfig(subjectAlias = "Password")
object PasswordValidator : Validator<String> {
    //...
}
```
So the generated violations would look like this:
```kotlin
//AUTO GENERATED BY THE PLUGIN
sealed interface PasswordViolation {
    //...
}
```
#### Metadata
You can also provide metadata about the violation in the form of properties as follows:
```kotlin
object PasswordValidator : Validator<String> {
    override val constraints by describe {
        constraint("TooShort") {
            meta("min") { 7 }
            meta("actual") { subject.length } //subject refers to the instance being validated
            minLength(7)
        }
        //...
    }
}
```
And the generated violations would look like this:
```kotlin
//AUTO GENERATED BY THE PLUGIN
sealed interface PasswordViolation {
    data class TooShort(
        val min: Int,
        val actual: Int
    ) : PasswordViolation
    //...
}
```
### Logical Validators
To perform logical operations on the validation you can use the following:
#### allOf
Performs the `AND` operation on the validations:
```kotlin
object NameValidator : Validator<String> {
    override val constraints by describe {
        constraints("InvalidLength") {
            //length must be between 7 and 14
            allOf {
                minLength(7)
                maxLength(14)
            }
        }
    }
}
```
#### anyOf
Performs the `OR` operation on the validations:
```kotlin
object NameValidator : Validator<String> {
    override val constraints by describe {
        constraints("InvalidLength") {
            //length must either 7 or 14
            anyOf {
                lengthEqualTo(7)
                lengthEqualTo(14)
            }
        }
    }
}
```
#### noneOf
Performs the `NOT` operation on the validations:
```kotlin
object NameValidator : Validator<String> {
    override val constraints by describe {
        constraints("InvalidLength") {
            //length can't be 7, 10, or 14
            noneOf {
                lengthEqualTo(7)
                lengthEqualTo(10)
                lengthEqualTo(14)
            }
        }
    }
}
```
#### Composed
You can compose the logical validators as follows:
```kotlin
object NameValidator : Validator<String> {
    override val constraints by describe {
        constraints("InvalidLength") {
            //at least of the nested logical validations must be true
            anyOf {
                //it doesn't contain the word "Mourad"
                noneOf {
                    contains(portion = "Mourad", ignoreCase = true)
                }
                //or it starts with "Ahmed" and ends with "Mourad"
                allOf {
                    startsWith(prefix = "Ahmed", ignoreCase = true)
                    endsWith(suffix = "Mourad", ignoreCase = true)
                }
            }
        }
    }
}
```
### Generated Functions
In addition to the violations, the plugin also generates the following extension functions on the validator:
#### validate
Accepts a factory for the item to be validated and returns a result of the type `Case`:
- `Legal` when the item is valid, contains the created item.
- `Illegal` when the item is not valid, contains a set of the violations the item has.
```kotlin
val result = PasswordValidator.validate {
    "somepassword"
}
when (result) {
    is Case.Legal -> //...
    is Case.Illegal -> //...
}
```
#### isValid
Accepts a factory for the item to be validated and return `true` if it's valid, otherwise false:
```kotlin
val result: Boolean = PasswordValidator.isValid {
    "somepassword"
}
```
### Scoping Validations
#### Non-Nullable Properties
You can change the scope of the validation using `on`:
```kotlin
data class Employee(val name: String, val age: Int) {
    companion object : Validator<Employee> {
        override val constraints by describe {
            constraint("NameTooShort") {
                on(Employee::name) {
                    minLength(5)
                }
            }
            constraint("NameContainsSymbols") {
                on(Employee::name) {
                    listOf('@', '!', '^').forEach { char ->
                        containsChar(char)
                    }
                }
            }
        }
    }
}
```
Doing so also changes the validation `subject`:
```kotlin
data class Employee(val name: String, val age: Int) {
    companion object : Validator<Employee> {
        override val constraints by describe {
            //...
            constraint("NegativeAge") {
                validation { subject.age < 0 }
                //is equal to
                on(Employee::age) {
                    validation { subject < 0 }
                }
            }
        }
    }
}
```
#### Nullable Properties
For nullable properties you need to define the scoping behavior using `ifExists` or `mustExist`:
```kotlin
data class Person(val name: String?, val age: Int?) {
    companion object : Validator<Person> {
        override val constraints by describe {
            constraint("NameBlankOrNull") {
                //constraint is violated if name is null
                on(Person::name) mustExist {
                    isNotBlank()
                }
            }
            constraint("NegativeAge") {
                //age is only validated if it's not null
                on(Person::age) ifExists {
                    isNegative(orZero = false)
                }
            }
        }
    }
}
```
They can also be written this way which can be useful when dealing with collections of nullable items:
```kotlin
constraint("NameBlankOrNull") {
    on(Person::name)  {
        mustExist {
            isNotBlank()
        }
    }
}
```
#### Collection Properties
For collection properties, you can scope the validation to the collection entries using `forAll`, `forAny`, or `forNone`:
```kotlin
object HobbiesValidator : Validator<List<String>> {
    override val constraints by describe {
        constraint("InvalidHobby") {
            on(Person::hobbies) {
                //all hobbies must be longer than 4 characters
                forAll { 
                    minLength(4)
                }
                //at least one of the hobbies must contain "ball"
                forAny {
                    contains(portion = "ball", ignoreCase = true)
                }
                //none of the hobbies can be blank
                forNone { 
                    isBlank()
                }
            }
        }
    }
}
```
### Custom Validations
You can easily create custom validations as follows:
```kotlin
fun Constraint<List<Int>>.hasEvenSum() = validation {
    subject.sum() % 2 == 0
}
```
### Lazy Evaluations
Since the validator is independent of the subject and is created before the subject is even available, the `subject` value is not available on all scopes when declaring constraints, which poses a problem when you want to create intermediate values to be used in multiple places, lazy evaluations fix that:
```kotlin
object EmailValidator : Validator<String> {
    override val constraints by describe {
        //`elements` is evaluated lazily when the subject is provided
        val elements = evaluate { subject.split('@') }
        constraint(violation = "InvalidLocal") {
            //you can use evaluated values inside others by calling `get()`
            val local = evaluate { elements.get().dropLast(1).joinToString("@") }
            //you can pass evaluations as metadata
            meta("value", local)
            //you can scope validations to evaluations 
            on(local) {
                //...
            }
        }
        constraint(violation = "InvalidDomain") {
            val domain = evaluate { elements.get().lastOrNull() }
            meta("value", domain)
            on(domain) ifExists {
                //...
            }
        }
    }
}
```
### Composing Validators
One of the areas where kotlin-validations really shines is when it comes to composing validations, instead of having to deal with flatMap hell or monad comprehensions, kotlin-validation allows composing validators:
```kotlin
object EmailValidator : Validator<Email> {
    //...
}

class PasswordValidator(private val minLength: Int) : Validator<Password> {
    //...
}

data class User(val email: Email, val password: Password)
object UserValidator : Validator<User> {
    override val constraints by describe { 
        constraint("InvalidEmail") {
            include("violations") {
                User::email to EmailValidator
            }
        }
        constraint("InvalidPassword") {
            include("violations") {
                User::password to PasswordValidator(minLength = 6)
            }
        }
    }
}
```
`include` generates a property on the constraint violation class, in this case it's called `violations`, and it contains the set of the violations the included validator produced:
```kotlin
//AUTO GENERATED BY THE PLUGIN
sealed interface UserViolation {
    data class InvalidEmail(val violations: Set<EmailViolation>) : UserViolation
    data class InvalidPassword(val violations: Set<PasswordViolation>) : UserViolation
}
```
And you can just use the composite validator as follows:
```kotlin
val result = UserValidator.validate {
    User(
        email = Email("..."),
        password = Password("...")
    )
}
```
### Limitations
Due to current limitations of the plugin the following rules must be followed:

- All calls to the `constraint` builder must be directly inside the `describe` builder:
```kotlin
object SomeValidator : Validator<String> {
    override val constraints by describe {
        //the correct way
        constraint("Good1") {
            //...
        }
    }
    
    //incorrect
    fun ConstraintsBuilder<String>.someConstraints() {
        //compilation error
        constraint("Bad1") {
            
        }
    }
}

//incorrect
fun ConstraintsBuilder<String>.otherConstraints() {
    //compilation error
    constraint("Bad2") {

    }
}
```
- All calls to the `meta` and `include` builders must be directly inside the `constraint` builder:
```kotlin
object SomeValidator : Validator<String> {
    override val constraints by describe {
        constraint("SomeConstraint") {
            //the correct way
            meta("good1") { 
                //...
            }
            //the correct way
            include("good2") {
                //...
            }
        }
    }
    
    //incorrect
    fun ConstraintBuilder<String>.metadata1() {
        //compilation error
        meta("bad1") {
            //...
        }
        //compilation error
        include("bad2") {
            //...
        }
    }
}

//incorrect
fun ConstraintBuilder<String>.metadata2() {
    //compilation error
    meta("bad1") {
        //...
    }
    //compilation error
    include("bad2") {
        //...
    }
}
```
Failing to follow any of these rules produces a compilation error.

### Enforced Validation
With the domain rules declared, most of the time you'll want to enforce those rules on all instances created from that class
#### MustBeValid
You need to do the following:
- Annotate the subject class with `@MustBeValid`
- If you're using the subject class in other modules or projects you need to make its constructors internal

And what this does is that it only allows the subject instance to be constructed inside the `validate` and `isValid` factories of the subject validator and other validators that include it:
```kotlin
@MustBeValid
data class User internal constructor(val email: Email)
object UserValidator : Validator<User> {
    //...
}

fun main() {
    //works fine
    val result = UserValidator.validate {
        User(email = Email("..."))
    }
    //compilation error
    val user = User(email = Email("..."))
}
```

*For more information on how this is achieved and how to extend it, see [Validation Context](#validation-context)*

#### The copy Problem
The `copy` method of a data class acts as its constructor, and it's not affected by the visibility of other constructors, this means that it does not obey our validation rules, you can read more about this [here](https://medium.com/swlh/value-based-classes-and-error-handling-in-kotlin-3f14727c0565)

In order to fix this, you can use the [no-copy](https://github.com/AhmedMourad0/no-copy) compiler plugin, by the same library author, to remove the `copy` method

### Built-in Validations
The `validations` artifact offers hundreds of ready-to-use validations, you can find them listed [here](TODO)

### Built-in Validators
The `validators` artifact offers many ready-to-use validators, you can find them listed [here](TODO)

### Validation Context
In order to be able to [enforce validations](#enforced-validation) on desired types, the concept of validation contexts has been introduced.

For each validator introduced, a validation context interface is generated by the plugin:
```kotlin
data class Email(val v: String)
object EmailValidator : Validator<Email> {
    //...
}
```
```kotlin
//AUTO GENERATED BY THE PLUGIN
interface EmailValidationContext
```
The `validate` and `isValid` factories extend this context:
```kotlin
//AUTO GENERATED BY THE PLUGIN
fun EmailValidator.validate(
    createItem: EmailValidationContext.() -> Email
) {
    //...
}
```
If the subject class is annotated with `@MustBeValid`, a factory is created for each `public` or `internal` constructor that calls the corresponding constructor and extends the validation context:
```kotlin
@MustBeValid
data class Email internal constructor(val v: String)
object EmailValidator : Validator<Email> {
    //...
}
```
```kotlin
//AUTO GENERATED BY THE PLUGIN
@UnsafeValidationContext
fun EmailValidationContext.Email(v: String) = Email(v = v)
```
The compiler plugin makes sure the constructors can't be called anywhere else other than the generated factories, which means you can only create objects of the subject class inside the validation factories:
```kotlin
@MustBeValid
data class User internal constructor(val email: Email)
object UserValidator : Validator<User> {
    //...
}

fun main() {
    //works fine
    val result = UserValidator.validate {
        User(email = Email("..."))
    }
    //compilation error
    val user = User(email = Email("..."))
}
```
If the validator includes any other validators, its context will extend theirs thus allowing you to construct instances of the subjects of the included validators inside the factories of this validator:
```kotlin
@MustBeValid
data class Email internal constructor(val v: String)
object EmailValidator : Validator<Email> {
    //...
}

@MustBeValid
data class Password internal constructor(val v: String)
object PasswordValidator : Validator<Password> {
    //...
}

@MustBeValid
data class User internal constructor(val email: Email, val password: Password)
object UserValidator : Validator<User> {
    //...
}
```
```kotlin
//AUTO GENERATED BY THE PLUGIN
interface EmailValidationContext
//...

interface PasswordValidationContext
//...

interface UserValidationContext : EmailValidationContext, PasswordValidationContext
//...
```
Which enables you to simply construct the object without the need for flatMap hell or monad comprehensions:
```kotlin
val result = User.validate {
    User(
        email = Email("..."),
        password = Password("...")
    )
}
```

### Testing
The `validation-testing` artifact has multiple useful utility functions for testing validations, you can find them listed [here](TODO)

## Features
- Generating sealed interfaces that describe violations (no string matching)
- Generating the `validate` and `isValid` functions per validator
- Support for adding properties to different violations
- Support for Generics
- Support providing extra parameters / type parameters to use during validation
- Support validating the extra parameters to validators
- Support creating validators for Third-party classes.
- Multiple validators per class
- Validators composition
- Ability to enforce classes to be validated before usage
- Declarative type-safe dsl to describe constraints
- Property-specific and class-specific validations
- Nested validations and constraints
- Nesting shortcuts
- Multiplatform ready-to-use validations
- Support doing logical operations on validations
- Validations for elements of different containers
- Easy-to-create custom validations
- Arguments and returned values validation (on-the-fly validators)
- Includes ready-to-use validators (Email, Password, ISBN)

## Roadmap
- Rewrite the compiler plugin for the K2 compiler
- Backend? plugin to replace all property scoping `on` calls with the non-reflection version
- Include NoCopy with this and generate copy extension functions
- move the validations into their own module to reduce the size of the core module
- create a separate testing artifact
- Release first version
- Add an option to include the success case as a child of the sealed interface, with a couple of helper extensions
- More platform-specific ready-to-use validations
- More ready-to-use validators (Email, Password, ISBN)
- (Arrow, Kotest, ...) extensions
- Jetpack Compose support
- Adding validations descriptions to be used by the IDE to describe validators
- IDE plugin to test values against constraints on the spot

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
