# Kotlin-Validation _(Under Development)_
A multiplatform, declarative, flexible and type-safe Kotlin validation framework.

## Target Syntax
Check the samples for current syntax.

## Features
- Generating sealed classes that describe violations (no string matching)
- Generating the `validate` and `isValid` functions per validator
- Support for adding properties to different violations
- Support for Generics
- Support providing extra parameters / type parameters to use during validation
- Support validating the extra metadata to violations
- Third-party classes validators
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

## Roadmap
- Providing ready-to-use validators (email, phone-number, ...)
- More Multiplatform ready-to-use validations
- Library-specific ready-to-use validations 
- K/Jvm ready-to-use validations
- K/N ready-to-use validations
- K/Js ready-to-use validations
- Arguments and returned values validation (call-site validation)
- (Arrow, Kotest, ...) extensions
- Jetpack Compose support?
- To suspend or not to suspend.
- Adding validations descriptions to be used by the IDE to describe validators
- IDE plugin to test values against constraints on the spot?

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
 