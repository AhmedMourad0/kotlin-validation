package dev.ahmedmourad.validation.validators

import dev.ahmedmourad.validation.core.*
import dev.ahmedmourad.validation.core.validations.*

@ValidatorConfig(subjectAlias = "Password")
class PasswordValidator(
    private val minLength: Int? = null,
    private val maxLength: Int? = null,
    private val minUpperCaseLettersCount: Int? = null,
    private val minLowerCaseLettersCount: Int? = null,
    private val minDigitsCount: Int? = null,
    private val minSymbolsCount: Int? = null,
    private val minDistinctCount: Int? = null,
    private val allowSequence: Boolean = true
) : Validator<String> {
    override val constraints by describe {
        val minLength = this@PasswordValidator.minLength
        val maxLength = this@PasswordValidator.maxLength
        val minUpperCaseLettersCount = this@PasswordValidator.minUpperCaseLettersCount
        val minLowerCaseLettersCount = this@PasswordValidator.minLowerCaseLettersCount
        val minDigitsCount = this@PasswordValidator.minDigitsCount
        val minSymbolsCount = this@PasswordValidator.minSymbolsCount
        val minDistinctCount = this@PasswordValidator.minDistinctCount
        val allowSequential = this@PasswordValidator.allowSequence
        constraint(violation = "TooShort") {
            meta("min") { minLength }
            meta("actual") { subject.length }
            if (minLength != null) {
                minLength(minLength)
            }
        }
        constraint(violation = "TooLong") {
            meta("max") { maxLength }
            meta("actual") { subject.length }
            if (maxLength != null) {
                maxLength(maxLength)
            }
        }
        constraint(violation = "FewUpperCaseLetters") {
            val count = evaluate { subject.count(Char::isUpperCase) }
            meta("min") { minUpperCaseLettersCount }
            meta("actual", count)
            if (minUpperCaseLettersCount != null) {
                validation { count.get() >= minUpperCaseLettersCount }
            }
        }
        constraint(violation = "FewLowerCaseLetters") {
            val count = evaluate { subject.count(Char::isLowerCase) }
            meta("min") { minLowerCaseLettersCount }
            meta("actual", count)
            if (minLowerCaseLettersCount != null) {
                validation { count.get() >= minLowerCaseLettersCount }
            }
        }
        constraint(violation = "FewDigits") {
            val count = evaluate { subject.count(Char::isDigit) }
            meta("min") { minDigitsCount }
            meta("actual", count)
            if (minDigitsCount != null) {
                validation { count.get() >= minDigitsCount }
            }
        }
        constraint(violation = "FewSymbols") {
            val count = evaluate { subject.count { !it.isLetterOrDigit() } }
            meta("min") { minSymbolsCount }
            meta("actual", count)
            if (minSymbolsCount != null) {
                validation { count.get() >= minSymbolsCount }
            }
        }
        constraint(violation = "FewDistinctCharacters") {
            val count = evaluate { subject.toCharArray().distinct().size }
            meta("min") { minDistinctCount }
            meta("actual", count)
            if (minDistinctCount != null) {
                validation { count.get() >= minDistinctCount }
            }
        }
        constraint(violation = "OnlySequentialCharactersOrDigits") {
            if (!allowSequential) {
                isNotOnlySequentialChars()
            }
        }
    }
}

fun Constraint<String>.isNotOnlySequentialChars() = validation {

    if (subject.length < 3) {
        return@validation true
    }

    //TODO: might cause problems for letters
    val gap = subject[1].code - subject[0].code

    for (i in 1..subject.lastIndex) {
        if (subject[i].code - subject[i - 1].code != gap) {
            return@validation true
        }
    }
    false
}
