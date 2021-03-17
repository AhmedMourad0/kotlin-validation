package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Validator
import dev.ahmedmourad.validation.core.ValidatorImpl

inline fun <DT> Validator<DT>.isEqualTo(
    crossinline other: (DT) -> DT
) = validation {
    it == other(it)
}

fun <DT> Validator<DT>.isEqualTo(other: DT) = validation {
    it == other
}

inline fun <DT> Validator<DT>.isNotEqualTo(
    crossinline other: (DT) -> DT
) = validation {
    it != other(it)
}

fun <DT> Validator<DT>.isNotEqualTo(other: DT) = validation {
    it != other
}

inline fun <DT> Validator<DT>.inValues(
    crossinline candidates: (DT) -> Iterable<DT>
) = validation {
    it in candidates(it)
}

fun <DT> Validator<DT>.inValues(candidates: Iterable<DT>) = inValues { candidates }

fun <DT> Validator<DT>.inValues(vararg candidates: DT) = validation {
    it in candidates
}

inline fun <DT> Validator<DT>.notInValues(
    crossinline candidates: (DT) -> Iterable<DT>
) = validation {
    it !in candidates(it)
}

fun <DT> Validator<DT>.notInValues(candidates: Iterable<DT>) = notInValues { candidates }

fun <DT> Validator<DT>.notInValues(vararg candidates: DT) = validation {
    it !in candidates
}

inline fun <DT> Validator<DT>.anyOf(
    crossinline validator: Validator<DT>.() -> Unit
) = validation {
    ValidatorImpl<DT>().apply { validator() }.validateAny(it)
}

inline fun <DT> Validator<DT>.allOf(
    crossinline validator: Validator<DT>.() -> Unit
) = validation {
    ValidatorImpl<DT>().apply { validator() }.validateAll(it)
}

inline fun <DT> Validator<DT>.noneOf(
    crossinline validator: Validator<DT>.() -> Unit
) = validation {
    ValidatorImpl<DT>().apply { validator() }.validateNone(it)
}
