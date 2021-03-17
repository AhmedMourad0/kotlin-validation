package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Validator
import dev.ahmedmourad.validation.core.validation

inline fun <A, B, C> Validator<Triple<A, B, C>>.first(
    crossinline firstValidator: Validator<A>.() -> Unit
) = validation {
    Validator<A>().apply(firstValidator).validateAll(it.first)
}

inline fun <A, B, C> Validator<Triple<A, B, C>>.second(
    crossinline secondValidator: Validator<B>.() -> Unit
) = validation {
    Validator<B>().apply(secondValidator).validateAll(it.second)
}

inline fun <A, B, C> Validator<Triple<A, B, C>>.third(
    crossinline thirdValidator: Validator<C>.() -> Unit
) = validation {
    Validator<C>().apply(thirdValidator).validateAll(it.third)
}
