package dev.ahmedmourad.validation.core

fun <T : Any> ValidatorBuilder<T, String>.minLength(min: Int) = validation {
    length >= min
}
