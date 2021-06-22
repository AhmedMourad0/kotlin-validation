package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint
import dev.ahmedmourad.validation.core.ScopedConstraintBuilder

inline fun <DT> Constraint<DT>.isEqualTo(
    crossinline other: (DT) -> DT
) = validation {
    it == other(it)
}

fun <DT> Constraint<DT>.isEqualTo(other: DT) = validation {
    it == other
}

inline fun <DT> Constraint<DT>.isNotEqualTo(
    crossinline other: (DT) -> DT
) = validation {
    it != other(it)
}

fun <DT> Constraint<DT>.isNotEqualTo(other: DT) = validation {
    it != other
}

inline fun <DT> Constraint<DT>.inValues(
    crossinline candidates: (DT) -> Iterable<DT>
) = validation {
    it in candidates(it)
}

fun <DT> Constraint<DT>.inValues(candidates: Iterable<DT>) = inValues { candidates }

fun <DT> Constraint<DT>.inValues(vararg candidates: DT) = validation {
    it in candidates
}

inline fun <DT> Constraint<DT>.notInValues(
    crossinline candidates: (DT) -> Iterable<DT>
) = validation {
    it !in candidates(it)
}

fun <DT> Constraint<DT>.notInValues(candidates: Iterable<DT>) = notInValues { candidates }

fun <DT> Constraint<DT>.notInValues(vararg candidates: DT) = validation {
    it !in candidates
}

inline fun <DT> Constraint<DT>.anyOf(
    crossinline constraint: Constraint<DT>.() -> Unit
) = validation {
    ScopedConstraintBuilder<DT>().apply { constraint() }.validateAny(it)
}

inline fun <DT> Constraint<DT>.allOf(
    crossinline constraint: Constraint<DT>.() -> Unit
) = validation {
    ScopedConstraintBuilder<DT>().apply { constraint() }.validateAll(it)
}

inline fun <DT> Constraint<DT>.noneOf(
    crossinline constraint: Constraint<DT>.() -> Unit
) = validation {
    ScopedConstraintBuilder<DT>().apply { constraint() }.validateNone(it)
}
