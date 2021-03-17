package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Validator
import dev.ahmedmourad.validation.core.ValidatorImpl

inline fun <A, B, C> Validator<Triple<A, B, C>>.first(
    crossinline firstValidator: Validator<A>.() -> Unit
) = validation {
    ValidatorImpl<A>().apply(firstValidator).validateAll(it.first)
}

inline fun <A, B, C> Validator<Triple<A, B, C>>.second(
    crossinline secondValidator: Validator<B>.() -> Unit
) = validation {
    ValidatorImpl<B>().apply(secondValidator).validateAll(it.second)
}

inline fun <A, B, C> Validator<Triple<A, B, C>>.third(
    crossinline thirdValidator: Validator<C>.() -> Unit
) = validation {
    ValidatorImpl<C>().apply(thirdValidator).validateAll(it.third)
}
