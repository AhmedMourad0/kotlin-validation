package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint

inline fun <DT : Comparable<DT>> Constraint<DT>.min(
    crossinline min: (DT) -> DT
) = validation {
    subject >= min(subject)
}

fun <DT : Comparable<DT>> Constraint<DT>.min(min: DT) = validation {
    subject >= min
}

inline fun <DT : Comparable<DT>> Constraint<DT>.max(
    crossinline max: (DT) -> DT
) = validation {
    subject <= max(subject)
}

fun <DT : Comparable<DT>> Constraint<DT>.max(max: DT) = validation {
    subject <= max
}

inline fun <DT : Comparable<DT>> Constraint<DT>.lessThan(
    crossinline maxExclusive: (DT) -> DT
) = validation {
    subject < maxExclusive(subject)
}

fun <DT : Comparable<DT>> Constraint<DT>.lessThan(maxExclusive: DT) = validation {
    subject < maxExclusive
}

inline fun <DT : Comparable<DT>> Constraint<DT>.largerThan(
    crossinline minExclusive: (DT) -> DT
) = validation {
    subject > minExclusive(subject)
}

fun <DT : Comparable<DT>> Constraint<DT>.largerThan(minExclusive: DT) = validation {
    subject > minExclusive
}

inline fun <DT : Comparable<DT>> Constraint<DT>.inRange(
    crossinline range: (DT) -> ClosedRange<DT>
) = validation {
    subject in range(subject)
}

fun <DT : Comparable<DT>> Constraint<DT>.inRange(range: ClosedRange<DT>) = inRange { range }

fun <DT : Comparable<DT>> Constraint<DT>.inRange(min: DT, max: DT) = inRange(min..max)

inline fun <DT : Comparable<DT>> Constraint<DT>.notInRange(
    crossinline range: (DT) -> ClosedRange<DT>
) = validation {
    subject !in range(subject)
}

fun <DT : Comparable<DT>> Constraint<DT>.notInRange(range: ClosedRange<DT>) = notInRange { range }

fun <DT : Comparable<DT>> Constraint<DT>.notInRange(min: DT, max: DT) = notInRange(min..max)
