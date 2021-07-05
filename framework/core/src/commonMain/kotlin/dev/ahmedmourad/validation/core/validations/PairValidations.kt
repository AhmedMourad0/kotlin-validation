package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint
import dev.ahmedmourad.validation.core.ScopedConstraintBuilder

inline fun <A, B> Constraint<Pair<A, B>>.first(
    crossinline firstConstraint: Constraint<A>.() -> Unit
) = validation {
    ScopedConstraintBuilder<A>().apply(firstConstraint).matchesAll(subject.first)
}

inline fun <A, B> Constraint<Pair<A, B>>.second(
    crossinline secondConstraint: Constraint<B>.() -> Unit
) = validation {
    ScopedConstraintBuilder<B>().apply(secondConstraint).matchesAll(subject.second)
}
