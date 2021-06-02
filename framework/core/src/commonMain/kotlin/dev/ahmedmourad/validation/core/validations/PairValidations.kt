package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Validator
import dev.ahmedmourad.validation.core.ValidatorImpl

inline fun <A, B> Validator<Pair<A, B>>.first(
    crossinline firstValidator: Validator<A>.() -> Unit
) = validation {
    ValidatorImpl<A>().apply(firstValidator).validateAll(it.first)
}

inline fun <A, B> Validator<Pair<A, B>>.second(
    crossinline secondValidator: Validator<B>.() -> Unit
) = validation {
    ValidatorImpl<B>().apply(secondValidator).validateAll(it.second)
}
