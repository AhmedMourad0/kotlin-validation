package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint

fun Constraint<Boolean>.isTrue() = validation {
    it
}

fun Constraint<Boolean>.isFalse() = validation {
    !it
}
