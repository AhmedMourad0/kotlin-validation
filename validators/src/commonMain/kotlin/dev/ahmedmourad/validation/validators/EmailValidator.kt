package dev.ahmedmourad.validation.validators

import dev.ahmedmourad.validation.core.*
import dev.ahmedmourad.validation.core.validations.*

@ValidatorConfig(subjectAlias = "Email")
class EmailValidator : Validator<String> {
    override val constraints by describe {
        val elements = evaluate { subject.split('@') }
        constraint(violation = "MalformedEmail") {
            validation { subject.any { it == '@' } }
        }
        constraint(violation = "InvalidLocal") {
            val local = evaluate { elements.get().dropLast(1).joinToString("@") }
            meta("value", local)
            on(local) {
                isNotEmpty()
                maxLength(64)
                noneOf {
                    startsWith(".")
                    endsWith(".")
                }
            }
        }
        constraint(violation = "InvalidDomain") {
            val domain = evaluate { elements.get().lastOrNull() }
            meta("value", domain)
            on(domain) ifExists {
                isNotEmpty()
                maxLength(255)
            }
        }
    }
}
