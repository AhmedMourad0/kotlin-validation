package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint

fun Constraint<Boolean>.isTrue() = validation {
    subject
}

fun Constraint<Boolean>.isFalse() = validation {
    !subject
}
