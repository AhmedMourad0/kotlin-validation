package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint
import dev.ahmedmourad.validation.core.ScopedConstraintBuilder

inline fun <DT> Constraint<out Iterable<DT>>.forAll(
    crossinline itemConstraint: Constraint<DT>.() -> Unit
) = validation { validated ->
    validated.all {
        ScopedConstraintBuilder<DT>().apply(itemConstraint).validateAll(it)
    }
}

inline fun <DT> Constraint<out Iterable<DT>>.forAny(
    crossinline itemConstraint: Constraint<DT>.() -> Unit
) = validation { validated ->
    validated.any {
        ScopedConstraintBuilder<DT>().apply(itemConstraint).validateAll(it)
    }
}

inline fun <DT> Constraint<out Iterable<DT>>.forNone(
    crossinline itemConstraint: Constraint<DT>.() -> Unit
) = validation { validated ->
    validated.none {
        ScopedConstraintBuilder<DT>().apply(itemConstraint).validateAll(it)
    }
}

fun <DT> Constraint<out Iterable<DT>>.isEmpty() = validation {
    it.count() == 0
}

fun <DT> Constraint<out Iterable<DT>>.isNotEmpty() = validation {
    it.count() > 0
}

fun <DT> Constraint<out Iterable<DT>>.isDistinct() = validation {
    it.distinct().count() == it.count()
}

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.minSize(
    crossinline min: (DTI) -> Int
) = validation {
    it.count() >= min(it)
}

fun <DT> Constraint<out Iterable<DT>>.minSize(min: Int) = minSize { min }

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.maxSize(
    crossinline max: (DTI) -> Int
) = validation {
    it.count() <= max(it)
}

fun <DT> Constraint<out Iterable<DT>>.maxSize(max: Int) = maxSize { max }

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.sizeLessThan(
    crossinline maxExclusive: (DTI) -> Int
) = validation {
    it.count() < maxExclusive(it)
}

fun <DT> Constraint<out Iterable<DT>>.sizeLessThan(maxExclusive: Int) = sizeLessThan { maxExclusive }

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.sizeLargerThan(
    crossinline minExclusive: (DTI) -> Int
) = validation {
    it.count() > minExclusive(it)
}

fun <DT> Constraint<out Iterable<DT>>.sizeLargerThan(minExclusive: Int) = sizeLargerThan { minExclusive }

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.sizeIn(
    crossinline range: (DTI) -> IntRange
) = validation {
    it.count() in range(it)
}

fun <DT> Constraint<out Iterable<DT>>.sizeIn(range: IntRange) = sizeIn { range }

fun <DT> Constraint<out Iterable<DT>>.sizeIn(min: Int, max: Int) = sizeIn(min..max)

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.sizeNotIn(
    crossinline range: (DTI) -> IntRange
) = validation {
    it.count() !in range(it)
}

fun <DT> Constraint<out Iterable<DT>>.sizeNotIn(range: IntRange) = sizeNotIn { range }

fun <DT> Constraint<out Iterable<DT>>.sizeNotIn(min: Int, max: Int) = sizeNotIn(min..max)

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.sizeEqualTo(
    crossinline value: (DTI) -> Int
) = validation {
    it.count() == value(it)
}

fun <DT> Constraint<out Iterable<DT>>.sizeEqualTo(value: Int) = sizeEqualTo { value }

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.sizeNotEqualTo(
    crossinline value: (DTI) -> Int
) = validation {
    it.count() != value(it)
}

fun <DT> Constraint<out Iterable<DT>>.sizeNotEqualTo(value: Int) = sizeNotEqualTo { value }

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.contains(
    crossinline element: (DTI) -> DT
) = validation {
    it.contains(element(it))
}

fun <DT> Constraint<out Iterable<DT>>.contains(
    element: DT
) = contains { element }

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.doesNotContain(
    crossinline element: (DTI) -> DT
) = validation {
    !it.contains(element(it))
}

fun <DT> Constraint<out Iterable<DT>>.doesNotContain(
    element: DT
) = doesNotContain { element }

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.containsAt(
    index: Int,
    crossinline element: (DTI) -> DT
) = validation {
    try {
        it.elementAt(index) == element(it)
    } catch (e: IndexOutOfBoundsException) {
        false
    }
}

fun <DT> Constraint<out Iterable<DT>>.containsAt(
    index: Int,
    element: DT
) = containsAt(index) { element }

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.startsWith(
    crossinline element: (DTI) -> DT
) = validation {
    try {
        it.first() == element(it)
    } catch (e: NoSuchElementException) {
        false
    }
}

fun <DT> Constraint<out Iterable<DT>>.startsWith(
    element: DT
) = startsWith { element }

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.endsWith(
    crossinline element: (DTI) -> DT
) = validation {
    try {
        it.last() == element(it)
    } catch (e: NoSuchElementException) {
        false
    }
}

fun <DT> Constraint<out Iterable<DT>>.endsWith(
    element: DT
) = endsWith { element }

fun <DT, DTI : Iterable<DT>> Constraint<DTI>.containsAll(
    elements: (DTI) -> Iterable<DT>
) = validation { validated ->
    elements(validated).all { validated.contains(it) }
}

fun <DT, DTI : Iterable<DT>> Constraint<DTI>.containsAll(
    vararg elements: DT
) = containsAll { elements.toList() }

fun <DT, DTI : Iterable<DT>> Constraint<DTI>.containsAll(
    elements: Iterable<DT>
) = containsAll { elements }

fun <DT, DTI : Iterable<DT>> Constraint<DTI>.isPartOf(
    elements: (DTI) -> Iterable<DT>
) = validation { validated ->
    val elementsList = elements(validated)
    validated.all { elementsList.contains(it) }
}

fun <DT, DTI : Iterable<DT>> Constraint<DTI>.isPartOf(
    vararg elements: DT
) = isPartOf { elements.toList() }

fun <DT, DTI : Iterable<DT>> Constraint<DTI>.isPartOf(
    elements: Iterable<DT>
) = isPartOf { elements }

fun Constraint<out Iterable<Boolean>>.allTrue() = validation { validated ->
    validated.all { it }
}

fun Constraint<out Iterable<Boolean>>.anyTrue() = validation { validated ->
    validated.any { it }
}

fun Constraint<out Iterable<Boolean>>.anyFalse() = validation { validated ->
    validated.any { !it }
}

fun Constraint<out Iterable<Boolean>>.allFalse() = validation { validated ->
    validated.none { it }
}

fun <DT, DTI : Iterable<DT>> Constraint<DTI>.contentEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: (DTI) -> Iterable<DT>
) = validation { validated ->
    validated.contentEquals(
        ignoreDuplicates,
        ignoreOrder,
        other(validated)
    )
}

fun <DT> Constraint<out Iterable<DT>>.contentEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: Iterable<DT>
) = contentEquals(ignoreDuplicates, ignoreOrder) { other }

fun <DT, DTI : Iterable<DT>> Constraint<DTI>.contentNotEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: (DTI) -> Iterable<DT>
) = validation { validated ->
    validated.contentNotEquals(
        ignoreDuplicates,
        ignoreOrder,
        other(validated)
    )
}

fun <DT> Constraint<out Iterable<DT>>.contentNotEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: Iterable<DT>
) = contentNotEquals(ignoreDuplicates, ignoreOrder) { other }
