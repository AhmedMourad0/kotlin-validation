package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Validator
import dev.ahmedmourad.validation.core.validation

inline fun <A, B> Validator<Pair<A, B>>.first(
    crossinline firstValidator: Validator<A>.() -> Unit
) = validation {
    Validator<A>().apply(firstValidator).validateAll(it.first)
}

inline fun <A, B> Validator<Pair<A, B>>.second(
    crossinline secondValidator: Validator<B>.() -> Unit
) = validation {
    Validator<B>().apply(secondValidator).validateAll(it.second)
}
