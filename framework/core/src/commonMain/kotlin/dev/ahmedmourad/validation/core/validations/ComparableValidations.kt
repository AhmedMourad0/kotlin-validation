package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint

inline fun <DT : Comparable<DT>> Constraint<DT>.min(
    crossinline min: (DT) -> DT
) = validation {
    it >= min(it)
}

fun <DT : Comparable<DT>> Constraint<DT>.min(min: DT) = validation {
    it >= min
}

inline fun <DT : Comparable<DT>> Constraint<DT>.max(
    crossinline max: (DT) -> DT
) = validation {
    it <= max(it)
}

fun <DT : Comparable<DT>> Constraint<DT>.max(max: DT) = validation {
    it <= max
}

inline fun <DT : Comparable<DT>> Constraint<DT>.lessThan(
    crossinline maxExclusive: (DT) -> DT
) = validation {
    it < maxExclusive(it)
}

fun <DT : Comparable<DT>> Constraint<DT>.lessThan(maxExclusive: DT) = validation {
    it < maxExclusive
}

inline fun <DT : Comparable<DT>> Constraint<DT>.largerThan(
    crossinline minExclusive: (DT) -> DT
) = validation {
    it > minExclusive(it)
}

fun <DT : Comparable<DT>> Constraint<DT>.largerThan(minExclusive: DT) = validation {
    it > minExclusive
}

inline fun <DT : Comparable<DT>> Constraint<DT>.inRange(
    crossinline range: (DT) -> ClosedRange<DT>
) = validation {
    it in range(it)
}

fun <DT : Comparable<DT>> Constraint<DT>.inRange(range: ClosedRange<DT>) = inRange { range }

fun <DT : Comparable<DT>> Constraint<DT>.inRange(min: DT, max: DT) = inRange(min..max)

inline fun <DT : Comparable<DT>> Constraint<DT>.notInRange(
    crossinline range: (DT) -> ClosedRange<DT>
) = validation {
    it !in range(it)
}

fun <DT : Comparable<DT>> Constraint<DT>.notInRange(range: ClosedRange<DT>) = notInRange { range }

fun <DT : Comparable<DT>> Constraint<DT>.notInRange(min: DT, max: DT) = notInRange(min..max)
