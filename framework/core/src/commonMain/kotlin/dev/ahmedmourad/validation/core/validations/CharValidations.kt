package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint

fun Constraint<Char>.isDefined() = validation {
    it.isDefined()
}

fun Constraint<Char>.isLetter() = validation {
    it.isLetter()
}

fun Constraint<Char>.isLetterOrDigit() = validation {
    it.isLetterOrDigit()
}

fun Constraint<Char>.isDigit() = validation {
    it.isDigit()
}

fun Constraint<Char>.isISOControl() = validation {
    it.isISOControl()
}

fun Constraint<Char>.isWhitespace() = validation {
    it.isWhitespace()
}

fun Constraint<Char>.isUpperCase() = validation {
    it.isUpperCase()
}

fun Constraint<Char>.isLowerCase() = validation {
    it.isLowerCase()
}

fun Constraint<Char>.isTitleCase() = validation {
    it.isTitleCase()
}

fun Constraint<Char>.isHighSurrogate() = validation {
    it.isHighSurrogate()
}

fun Constraint<Char>.isLowSurrogate() = validation {
    it.isLowSurrogate()
}

inline fun Constraint<Char>.isEqualTo(
    ignoreCase: Boolean,
    crossinline other: (Char) -> Char
) = validation {
    it.equals(other(it), ignoreCase)
}

fun Constraint<Char>.isEqualTo(
    ignoreCase: Boolean,
    other: Char
) = isEqualTo(ignoreCase) { other }

inline fun Constraint<Char>.isNotEqualTo(
    ignoreCase: Boolean,
    crossinline other: (Char) -> Char
) = validation {
    !it.equals(other(it), ignoreCase)
}

fun Constraint<Char>.isNotEqualTo(
    ignoreCase: Boolean,
    other: Char
) = isNotEqualTo(ignoreCase) { other }
