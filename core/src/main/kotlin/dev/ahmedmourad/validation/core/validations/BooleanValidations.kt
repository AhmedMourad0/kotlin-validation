package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Validator
import dev.ahmedmourad.validation.core.validation

fun Validator<Boolean>.isTrue() = validation {
    it
}

fun Validator<Boolean>.isFalse() = validation {
    !it
}
