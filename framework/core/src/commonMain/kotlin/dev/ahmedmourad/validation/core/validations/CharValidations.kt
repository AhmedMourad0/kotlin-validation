package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Validator

fun Validator<Char>.isDefined() = validation {
    it.isDefined()
}

fun Validator<Char>.isLetter() = validation {
    it.isLetter()
}

fun Validator<Char>.isLetterOrDigit() = validation {
    it.isLetterOrDigit()
}

fun Validator<Char>.isDigit() = validation {
    it.isDigit()
}

fun Validator<Char>.isISOControl() = validation {
    it.isISOControl()
}

fun Validator<Char>.isWhitespace() = validation {
    it.isWhitespace()
}

fun Validator<Char>.isUpperCase() = validation {
    it.isUpperCase()
}

fun Validator<Char>.isLowerCase() = validation {
    it.isLowerCase()
}

fun Validator<Char>.isTitleCase() = validation {
    it.isTitleCase()
}

fun Validator<Char>.isHighSurrogate() = validation {
    it.isHighSurrogate()
}

fun Validator<Char>.isLowSurrogate() = validation {
    it.isLowSurrogate()
}

inline fun Validator<Char>.isEqualTo(
    ignoreCase: Boolean,
    crossinline other: (Char) -> Char
) = validation {
    it.equals(other(it), ignoreCase)
}

fun Validator<Char>.isEqualTo(
    ignoreCase: Boolean,
    other: Char
) = isEqualTo(ignoreCase) { other }

inline fun Validator<Char>.isNotEqualTo(
    ignoreCase: Boolean,
    crossinline other: (Char) -> Char
) = validation {
    !it.equals(other(it), ignoreCase)
}

fun Validator<Char>.isNotEqualTo(
    ignoreCase: Boolean,
    other: Char
) = isNotEqualTo(ignoreCase) { other }
