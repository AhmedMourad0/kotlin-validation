package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint
import dev.ahmedmourad.validation.core.ScopedConstraintBuilder

inline fun <A, B, C> Constraint<Triple<A, B, C>>.first(
    crossinline firstConstraint: Constraint<A>.() -> Unit
) = validation {
    ScopedConstraintBuilder<A>().apply(firstConstraint).validateAll(it.first)
}

inline fun <A, B, C> Constraint<Triple<A, B, C>>.second(
    crossinline secondConstraint: Constraint<B>.() -> Unit
) = validation {
    ScopedConstraintBuilder<B>().apply(secondConstraint).validateAll(it.second)
}

inline fun <A, B, C> Constraint<Triple<A, B, C>>.third(
    crossinline thirdConstraint: Constraint<C>.() -> Unit
) = validation {
    ScopedConstraintBuilder<C>().apply(thirdConstraint).validateAll(it.third)
}
