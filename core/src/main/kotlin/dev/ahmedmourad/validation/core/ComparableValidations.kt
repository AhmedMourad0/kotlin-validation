package dev.ahmedmourad.validation.core

inline fun <DT : Comparable<DT>> Validator<DT>.min(
    crossinline min: (DT) -> DT
) = validation {
    it >= min(it)
}

fun <DT : Comparable<DT>> Validator<DT>.min(min: DT) = validation {
    it >= min
}

inline fun <DT : Comparable<DT>> Validator<DT>.max(
    crossinline max: (DT) -> DT
) = validation {
    it <= max(it)
}

fun <DT : Comparable<DT>> Validator<DT>.max(max: DT) = validation {
    it <= max
}

inline fun <DT : Comparable<DT>> Validator<DT>.lessThan(
    crossinline minExclusive: (DT) -> DT
) = validation {
    it > minExclusive(it)
}

fun <DT : Comparable<DT>> Validator<DT>.lessThan(minExclusive: DT) = validation {
    it > minExclusive
}

inline fun <DT : Comparable<DT>> Validator<DT>.largerThan(
    crossinline maxExclusive: (DT) -> DT
) = validation {
    it < maxExclusive(it)
}

fun <DT : Comparable<DT>> Validator<DT>.largerThan(maxExclusive: DT) = validation {
    it < maxExclusive
}

inline fun <DT : Comparable<DT>> Validator<DT>.inRange(
    crossinline range: (DT) -> ClosedRange<DT>
) = validation {
    it in range(it)
}

fun <DT : Comparable<DT>> Validator<DT>.inRange(range: ClosedRange<DT>) = inRange { range }

fun <DT : Comparable<DT>> Validator<DT>.inRange(min: DT, max: DT) = inRange(min..max)
