package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint
import dev.ahmedmourad.validation.core.ScopedConstraintBuilder
import kotlin.NoSuchElementException

//TODO: CharArray, IntArray, CharIterator, IntIterator, ...
inline fun <DT> Constraint<Array<DT>>.forAll(
    crossinline itemConstraint: Constraint<DT>.() -> Unit
) = validation { validated ->
    validated.all {
        ScopedConstraintBuilder<DT>().apply(itemConstraint).validateAll(it)
    }
}

inline fun <DT> Constraint<Array<DT>>.forAny(
    crossinline itemConstraint: Constraint<DT>.() -> Unit
) = validation { validated ->
    validated.any {
        ScopedConstraintBuilder<DT>().apply(itemConstraint).validateAll(it)
    }
}

inline fun <DT> Constraint<Array<DT>>.forNone(
    crossinline itemConstraint: Constraint<DT>.() -> Unit
) = validation { validated ->
    validated.none {
        ScopedConstraintBuilder<DT>().apply(itemConstraint).validateAll(it)
    }
}

fun <DT> Constraint<Array<DT>>.isEmpty() = validation {
    it.count() == 0
}

fun <DT> Constraint<Array<DT>>.isNotEmpty() = validation {
    it.count() > 0
}

fun <DT> Constraint<Array<DT>>.isDistinct() = validation {
    it.distinct().count() == it.count()
}

inline fun <DT> Constraint<Array<DT>>.minSize(
    crossinline min: (Array<DT>) -> Int
) = validation {
    it.count() >= min(it)
}

fun <DT> Constraint<Array<DT>>.minSize(min: Int) = minSize { min }

inline fun <DT> Constraint<Array<DT>>.maxSize(
    crossinline max: (Array<DT>) -> Int
) = validation {
    it.count() <= max(it)
}

fun <DT> Constraint<Array<DT>>.maxSize(max: Int) = maxSize { max }

inline fun <DT> Constraint<Array<DT>>.sizeLessThan(
    crossinline maxExclusive: (Array<DT>) -> Int
) = validation {
    it.count() < maxExclusive(it)
}

fun <DT> Constraint<Array<DT>>.sizeLessThan(maxExclusive: Int) = sizeLessThan { maxExclusive }

inline fun <DT> Constraint<Array<DT>>.sizeLargerThan(
    crossinline minExclusive: (Array<DT>) -> Int
) = validation {
    it.count() > minExclusive(it)
}

fun <DT> Constraint<Array<DT>>.sizeLargerThan(minExclusive: Int) = sizeLargerThan { minExclusive }

inline fun <DT> Constraint<Array<DT>>.sizeIn(
    crossinline range: (Array<DT>) -> IntRange
) = validation {
    it.count() in range(it)
}

fun <DT> Constraint<Array<DT>>.sizeIn(range: IntRange) = sizeIn { range }

fun <DT> Constraint<Array<DT>>.sizeIn(min: Int, max: Int) = sizeIn(min..max)

inline fun <DT> Constraint<Array<DT>>.sizeNotIn(
    crossinline range: (Array<DT>) -> IntRange
) = validation {
    it.count() !in range(it)
}

fun <DT> Constraint<Array<DT>>.sizeNotIn(range: IntRange) = sizeNotIn { range }

fun <DT> Constraint<Array<DT>>.sizeNotIn(min: Int, max: Int) = sizeNotIn(min..max)

inline fun <DT> Constraint<Array<DT>>.sizeEqualTo(
    crossinline value: (Array<DT>) -> Int
) = validation {
    it.count() == value(it)
}

fun <DT> Constraint<Array<DT>>.sizeEqualTo(value: Int) = sizeEqualTo { value }

inline fun <DT> Constraint<Array<DT>>.sizeNotEqualTo(
    crossinline value: (Array<DT>) -> Int
) = validation {
    it.count() != value(it)
}

fun <DT> Constraint<Array<DT>>.sizeNotEqualTo(value: Int) = sizeNotEqualTo { value }

inline fun <DT> Constraint<Array<DT>>.contains(
    crossinline element: (Array<DT>) -> DT
) = validation {
    it.contains(element(it))
}

fun <DT> Constraint<Array<DT>>.contains(
    element: DT
) = validation {
    it.contains(element)
}

inline fun <DT> Constraint<Array<DT>>.doesNotContain(
    crossinline element: (Array<DT>) -> DT
) = validation {
    !it.contains(element(it))
}

fun <DT> Constraint<Array<DT>>.doesNotContain(
    element: DT
) = validation {
    !it.contains(element)
}

inline fun <DT> Constraint<Array<DT>>.containsAt(
    index: Int,
    crossinline element: (Array<DT>) -> DT
) = validation {
    try {
        it.elementAt(index) == element(it)
    } catch (e: IndexOutOfBoundsException) {
        false
    }
}

fun <DT> Constraint<Array<DT>>.containsAt(
    index: Int,
    element: DT
) = validation {
    try {
        it.elementAt(index) == element
    } catch (e: IndexOutOfBoundsException) {
        false
    }
}

inline fun <DT> Constraint<Array<DT>>.startsWith(
    crossinline element: (Array<DT>) -> DT
) = validation {
    try {
        it.first() == element(it)
    } catch (e: NoSuchElementException) {
        false
    }
}

fun <DT> Constraint<Array<DT>>.startsWith(
    element: DT
) = validation {
    try {
        it.first() == element
    } catch (e: NoSuchElementException) {
        false
    }
}

inline fun <DT> Constraint<Array<DT>>.endsWith(
    crossinline element: (Array<DT>) -> DT
) = validation {
    try {
        it.last() == element(it)
    } catch (e: NoSuchElementException) {
        false
    }
}

fun <DT> Constraint<Array<DT>>.endsWith(
    element: DT
) = validation {
    try {
        it.last() == element
    } catch (e: NoSuchElementException) {
        false
    }
}

fun <DT> Constraint<Array<DT>>.containsAll(
    elements: (Array<DT>) -> Array<DT>
) = validation { validated ->
    elements(validated).all { validated.contains(it) }
}

fun <DT> Constraint<Array<DT>>.containsAll(
    vararg elements: DT
) = validation { validated ->
    elements.all { validated.contains(it) }
}

fun <DT> Constraint<Array<DT>>.isPartOf(
    elements: (Array<DT>) -> Array<DT>
) = validation { validated ->
    val elementsList = elements(validated)
    validated.all { elementsList.contains(it) }
}

fun <DT> Constraint<Array<DT>>.isPartOf(
    vararg elements: DT
) = validation { validated ->
    validated.all { elements.contains(it) }
}

fun Constraint<Array<Boolean>>.allTrue() = validation { validated ->
    validated.all { it }
}

fun Constraint<Array<Boolean>>.anyTrue() = validation { validated ->
    validated.any { it }
}

fun Constraint<Array<Boolean>>.anyFalse() = validation { validated ->
    validated.any { !it }
}

fun Constraint<Array<Boolean>>.allFalse() = validation { validated ->
    validated.none { it }
}

fun <DT> Constraint<Array<DT>>.contentEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: (Array<DT>) -> Array<DT>
) = validation { validated ->
    validated.asList().contentEquals(
        ignoreDuplicates,
        ignoreOrder,
        other(validated).asList()
    )
}

//TODO: contentDeepEquals?
fun <DT> Constraint<Array<DT>>.contentEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: Array<DT>
) = contentEquals(ignoreDuplicates, ignoreOrder) { other }

fun <DT> Constraint<Array<DT>>.contentNotEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: (Array<DT>) -> Array<DT>
) = validation { validated ->
    validated.asList().contentNotEquals(
        ignoreDuplicates,
        ignoreOrder,
        other(validated).asList()
    )
}

fun <DT> Constraint<Array<DT>>.contentNotEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: Array<DT>
) = this.contentNotEquals(ignoreDuplicates, ignoreOrder) { other }
