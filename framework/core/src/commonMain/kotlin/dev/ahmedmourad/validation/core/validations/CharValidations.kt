package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint

fun Constraint<Char>.isDefined() = validation {
    subject.isDefined()
}

fun Constraint<Char>.isLetter() = validation {
    subject.isLetter()
}

fun Constraint<Char>.isLetterOrDigit() = validation {
    subject.isLetterOrDigit()
}

fun Constraint<Char>.isDigit() = validation {
    subject.isDigit()
}

fun Constraint<Char>.isISOControl() = validation {
    subject.isISOControl()
}

fun Constraint<Char>.isWhitespace() = validation {
    subject.isWhitespace()
}

fun Constraint<Char>.isUpperCase() = validation {
    subject.isUpperCase()
}

fun Constraint<Char>.isLowerCase() = validation {
    subject.isLowerCase()
}

fun Constraint<Char>.isTitleCase() = validation {
    subject.isTitleCase()
}

fun Constraint<Char>.isHighSurrogate() = validation {
    subject.isHighSurrogate()
}

fun Constraint<Char>.isLowSurrogate() = validation {
    subject.isLowSurrogate()
}

inline fun Constraint<Char>.isEqualTo(
    ignoreCase: Boolean,
    crossinline other: (Char) -> Char
) = validation {
    subject.equals(other(subject), ignoreCase)
}

fun Constraint<Char>.isEqualTo(
    ignoreCase: Boolean,
    other: Char
) = isEqualTo(ignoreCase) { other }

inline fun Constraint<Char>.isNotEqualTo(
    ignoreCase: Boolean,
    crossinline other: (Char) -> Char
) = validation {
    !subject.equals(other(subject), ignoreCase)
}

fun Constraint<Char>.isNotEqualTo(
    ignoreCase: Boolean,
    other: Char
) = isNotEqualTo(ignoreCase) { other }
