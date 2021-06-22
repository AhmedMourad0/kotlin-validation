package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint
import dev.ahmedmourad.validation.core.ScopedConstraintBuilder

inline fun <DT> Constraint<out Sequence<DT>>.forAll(
    crossinline itemConstraint: Constraint<DT>.() -> Unit
) = validation { validated ->
    validated.all {
        ScopedConstraintBuilder<DT>().apply(itemConstraint).validateAll(it)
    }
}

inline fun <DT> Constraint<out Sequence<DT>>.forAny(
    crossinline itemConstraint: Constraint<DT>.() -> Unit
) = validation { validated ->
    validated.any {
        ScopedConstraintBuilder<DT>().apply(itemConstraint).validateAll(it)
    }
}

inline fun <DT> Constraint<out Sequence<DT>>.forNone(
    crossinline itemConstraint: Constraint<DT>.() -> Unit
) = validation { validated ->
    validated.none {
        ScopedConstraintBuilder<DT>().apply(itemConstraint).validateAll(it)
    }
}

fun <DT> Constraint<out Sequence<DT>>.isEmpty() = validation {
    it.count() == 0
}

fun <DT> Constraint<out Sequence<DT>>.isNotEmpty() = validation {
    it.count() > 0
}

fun <DT> Constraint<out Sequence<DT>>.isDistinct() = validation {
    it.distinct().count() == it.count()
}

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.minSize(
    crossinline min: (DTS) -> Int
) = validation {
    it.count() >= min(it)
}

fun <DT> Constraint<out Sequence<DT>>.minSize(min: Int) = minSize { min }

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.maxSize(
    crossinline max: (DTS) -> Int
) = validation {
    it.count() <= max(it)
}

fun <DT> Constraint<out Sequence<DT>>.maxSize(max: Int) = maxSize { max }

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.sizeLessThan(
    crossinline maxExclusive: (DTS) -> Int
) = validation {
    it.count() < maxExclusive(it)
}

fun <DT> Constraint<out Sequence<DT>>.sizeLessThan(maxExclusive: Int) = sizeLessThan { maxExclusive }

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.sizeLargerThan(
    crossinline minExclusive: (DTS) -> Int
) = validation {
    it.count() > minExclusive(it)
}

fun <DT> Constraint<out Sequence<DT>>.sizeLargerThan(minExclusive: Int) = sizeLargerThan { minExclusive }

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.sizeIn(
    crossinline range: (DTS) -> IntRange
) = validation {
    it.count() in range(it)
}

fun <DT> Constraint<out Sequence<DT>>.sizeIn(range: IntRange) = sizeIn { range }

fun <DT> Constraint<out Sequence<DT>>.sizeIn(min: Int, max: Int) = sizeIn(min..max)

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.sizeNotIn(
    crossinline range: (DTS) -> IntRange
) = validation {
    it.count() !in range(it)
}

fun <DT> Constraint<out Sequence<DT>>.sizeNotIn(range: IntRange) = sizeNotIn { range }

fun <DT> Constraint<out Sequence<DT>>.sizeNotIn(min: Int, max: Int) = sizeNotIn(min..max)

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.sizeEqualTo(
    crossinline value: (DTS) -> Int
) = validation {
    it.count() == value(it)
}

fun <DT> Constraint<out Sequence<DT>>.sizeEqualTo(value: Int) = sizeEqualTo { value }

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.sizeNotEqualTo(
    crossinline value: (DTS) -> Int
) = validation {
    it.count() != value(it)
}

fun <DT> Constraint<out Sequence<DT>>.sizeNotEqualTo(value: Int) = sizeNotEqualTo { value }

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.contains(
    crossinline element: (DTS) -> DT
) = validation {
    it.contains(element(it))
}

fun <DT> Constraint<out Sequence<DT>>.contains(
    element: DT
) = contains { element }

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.doesNotContain(
    crossinline element: (DTS) -> DT
) = validation {
    !it.contains(element(it))
}

fun <DT> Constraint<out Sequence<DT>>.doesNotContain(
    element: DT
) = doesNotContain { element }

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.containsAt(
    index: Int,
    crossinline element: (DTS) -> DT
) = validation {
    try {
        it.elementAt(index) == element(it)
    } catch (e: IndexOutOfBoundsException) {
        false
    }
}

fun <DT> Constraint<out Sequence<DT>>.containsAt(
    index: Int,
    element: DT
) = containsAt(index) { element }

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.startsWith(
    crossinline element: (DTS) -> DT
) = validation {
    try {
        it.first() == element(it)
    } catch (e: NoSuchElementException) {
        false
    }
}

fun <DT> Constraint<out Sequence<DT>>.startsWith(
    element: DT
) = startsWith { element }

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.endsWith(
    crossinline element: (DTS) -> DT
) = validation {
    try {
        it.last() == element(it)
    } catch (e: NoSuchElementException) {
        false
    }
}

fun <DT> Constraint<out Sequence<DT>>.endsWith(
    element: DT
) = endsWith { element }

fun <DT, DTS : Sequence<DT>> Constraint<DTS>.containsAll(
    elements: (DTS) -> Sequence<DT>
) = validation { validated ->
    elements(validated).all { validated.contains(it) }
}

fun <DT, DTS : Sequence<DT>> Constraint<DTS>.containsAll(
    vararg elements: DT
) = containsAll { elements.asSequence() }

fun <DT, DTS : Sequence<DT>> Constraint<DTS>.containsAll(
    elements: Sequence<DT>
) = containsAll { elements }

fun <DT, DTS : Sequence<DT>> Constraint<DTS>.isPartOf(
    elements: (DTS) -> Sequence<DT>
) = validation { validated ->
    val elementsList = elements(validated)
    validated.all { elementsList.contains(it) }
}

fun <DT, DTS : Sequence<DT>> Constraint<DTS>.isPartOf(
    vararg elements: DT
) = isPartOf { elements.asSequence() }

fun <DT, DTS : Sequence<DT>> Constraint<DTS>.isPartOf(
    elements: Sequence<DT>
) = isPartOf { elements }

fun Constraint<out Sequence<Boolean>>.allTrue() = validation { validated ->
    validated.all { it }
}

fun Constraint<out Sequence<Boolean>>.anyTrue() = validation { validated ->
    validated.any { it }
}

fun Constraint<out Sequence<Boolean>>.anyFalse() = validation { validated ->
    validated.any { !it }
}

fun Constraint<out Sequence<Boolean>>.allFalse() = validation { validated ->
    validated.none { it }
}

fun <DT, DTS : Sequence<DT>> Constraint<DTS>.contentEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: (DTS) -> Sequence<DT>
) = validation { validated ->
    validated.toList().contentEquals(
        ignoreDuplicates,
        ignoreOrder,
        other(validated).toList()
    )
}

fun <DT> Constraint<out Sequence<DT>>.contentEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: Sequence<DT>
) = contentEquals(ignoreDuplicates, ignoreOrder) { other }

fun <DT, DTS : Sequence<DT>> Constraint<DTS>.contentNotEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: (DTS) -> Sequence<DT>
) = validation { validated ->
    validated.toList().contentNotEquals(
        ignoreDuplicates,
        ignoreOrder,
        other(validated).toList()
    )
}

fun <DT> Constraint<out Sequence<DT>>.contentNotEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: Sequence<DT>
) = contentNotEquals(ignoreDuplicates, ignoreOrder) { other }
