package dev.ahmedmourad.validation.core

fun Validator<Boolean>.isTrue() = validation {
    it
}

fun Validator<Boolean>.isFalse() = validation {
    !it
}
