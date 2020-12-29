package dev.ahmedmourad.validation.core

fun ValidatorBuilder<String>.minLength(min: Int) = validation {
    length >= min
}
