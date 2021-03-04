package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Validator
import dev.ahmedmourad.validation.core.validation

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
    crossinline candidates: (DT) -> List<DT>
) = validation {
    it in candidates(it)
}

fun <DT> Validator<DT>.inValues(candidates: List<DT>) = inValues { candidates }

fun <DT> Validator<DT>.inValues(vararg candidates: DT) = validation {
    it in candidates
}

inline fun <DT> Validator<DT>.notInValues(
    crossinline candidates: (DT) -> List<DT>
) = validation {
    it !in candidates(it)
}

fun <DT> Validator<DT>.notInValues(candidates: List<DT>) = notInValues { candidates }

fun <DT> Validator<DT>.notInValues(vararg candidates: DT) = validation {
    it !in candidates
}

inline fun <DT> Validator<DT>.enforceAtLeastOne(
    crossinline validator: Validator<DT>.() -> Unit
) = validation {
    Validator<DT>().apply { validator() }.validateAny(it)
}

inline fun <DT> Validator<DT>.enforceAll(
    crossinline validator: Validator<DT>.() -> Unit
) = validation {
    Validator<DT>().apply { validator() }.validateAll(it)
}

inline fun <DT> Validator<DT>.enforceNone(
    crossinline validator: Validator<DT>.() -> Unit
) = validation {
    Validator<DT>().apply { validator() }.validateNone(it)
}
