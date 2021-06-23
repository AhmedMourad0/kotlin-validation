package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint
import dev.ahmedmourad.validation.core.ScopedConstraintBuilder
import kotlin.NoSuchElementException

//TODO: CharArray, IntArray, CharIterator, IntIterator, ...
inline fun <DT> Constraint<Array<DT>>.forAll(
    crossinline itemConstraint: Constraint<DT>.() -> Unit
) = validation {
    subject.all {
        ScopedConstraintBuilder<DT>().apply(itemConstraint).validateAll(it)
    }
}

inline fun <DT> Constraint<Array<DT>>.forAny(
    crossinline itemConstraint: Constraint<DT>.() -> Unit
) = validation {
    subject.any {
        ScopedConstraintBuilder<DT>().apply(itemConstraint).validateAll(it)
    }
}

inline fun <DT> Constraint<Array<DT>>.forNone(
    crossinline itemConstraint: Constraint<DT>.() -> Unit
) = validation {
    subject.none {
        ScopedConstraintBuilder<DT>().apply(itemConstraint).validateAll(it)
    }
}

fun <DT> Constraint<Array<DT>>.isEmpty() = validation {
    subject.count() == 0
}

fun <DT> Constraint<Array<DT>>.isNotEmpty() = validation {
    subject.count() > 0
}

fun <DT> Constraint<Array<DT>>.isDistinct() = validation {
    subject.distinct().count() == subject.count()
}

inline fun <DT> Constraint<Array<DT>>.minSize(
    crossinline min: (Array<DT>) -> Int
) = validation {
    subject.count() >= min(subject)
}

fun <DT> Constraint<Array<DT>>.minSize(min: Int) = minSize { min }

inline fun <DT> Constraint<Array<DT>>.maxSize(
    crossinline max: (Array<DT>) -> Int
) = validation {
    subject.count() <= max(subject)
}

fun <DT> Constraint<Array<DT>>.maxSize(max: Int) = maxSize { max }

inline fun <DT> Constraint<Array<DT>>.sizeLessThan(
    crossinline maxExclusive: (Array<DT>) -> Int
) = validation {
    subject.count() < maxExclusive(subject)
}

fun <DT> Constraint<Array<DT>>.sizeLessThan(maxExclusive: Int) = sizeLessThan { maxExclusive }

inline fun <DT> Constraint<Array<DT>>.sizeLargerThan(
    crossinline minExclusive: (Array<DT>) -> Int
) = validation {
    subject.count() > minExclusive(subject)
}

fun <DT> Constraint<Array<DT>>.sizeLargerThan(minExclusive: Int) = sizeLargerThan { minExclusive }

inline fun <DT> Constraint<Array<DT>>.sizeIn(
    crossinline range: (Array<DT>) -> IntRange
) = validation {
    subject.count() in range(subject)
}

fun <DT> Constraint<Array<DT>>.sizeIn(range: IntRange) = sizeIn { range }

fun <DT> Constraint<Array<DT>>.sizeIn(min: Int, max: Int) = sizeIn(min..max)

inline fun <DT> Constraint<Array<DT>>.sizeNotIn(
    crossinline range: (Array<DT>) -> IntRange
) = validation {
    subject.count() !in range(subject)
}

fun <DT> Constraint<Array<DT>>.sizeNotIn(range: IntRange) = sizeNotIn { range }

fun <DT> Constraint<Array<DT>>.sizeNotIn(min: Int, max: Int) = sizeNotIn(min..max)

inline fun <DT> Constraint<Array<DT>>.sizeEqualTo(
    crossinline value: (Array<DT>) -> Int
) = validation {
    subject.count() == value(subject)
}

fun <DT> Constraint<Array<DT>>.sizeEqualTo(value: Int) = sizeEqualTo { value }

inline fun <DT> Constraint<Array<DT>>.sizeNotEqualTo(
    crossinline value: (Array<DT>) -> Int
) = validation {
    subject.count() != value(subject)
}

fun <DT> Constraint<Array<DT>>.sizeNotEqualTo(value: Int) = sizeNotEqualTo { value }

inline fun <DT> Constraint<Array<DT>>.contains(
    crossinline element: (Array<DT>) -> DT
) = validation {
    subject.contains(element(subject))
}

fun <DT> Constraint<Array<DT>>.contains(
    element: DT
) = validation {
    subject.contains(element)
}

inline fun <DT> Constraint<Array<DT>>.doesNotContain(
    crossinline element: (Array<DT>) -> DT
) = validation {
    !subject.contains(element(subject))
}

fun <DT> Constraint<Array<DT>>.doesNotContain(
    element: DT
) = validation {
    !subject.contains(element)
}

inline fun <DT> Constraint<Array<DT>>.containsAt(
    index: Int,
    crossinline element: (Array<DT>) -> DT
) = validation {
    try {
        subject.elementAt(index) == element(subject)
    } catch (e: IndexOutOfBoundsException) {
        false
    }
}

fun <DT> Constraint<Array<DT>>.containsAt(
    index: Int,
    element: DT
) = validation {
    try {
        subject.elementAt(index) == element
    } catch (e: IndexOutOfBoundsException) {
        false
    }
}

inline fun <DT> Constraint<Array<DT>>.startsWith(
    crossinline element: (Array<DT>) -> DT
) = validation {
    try {
        subject.first() == element(subject)
    } catch (e: NoSuchElementException) {
        false
    }
}

fun <DT> Constraint<Array<DT>>.startsWith(
    element: DT
) = validation {
    try {
        subject.first() == element
    } catch (e: NoSuchElementException) {
        false
    }
}

inline fun <DT> Constraint<Array<DT>>.endsWith(
    crossinline element: (Array<DT>) -> DT
) = validation {
    try {
        subject.last() == element(subject)
    } catch (e: NoSuchElementException) {
        false
    }
}

fun <DT> Constraint<Array<DT>>.endsWith(
    element: DT
) = validation {
    try {
        subject.last() == element
    } catch (e: NoSuchElementException) {
        false
    }
}

fun <DT> Constraint<Array<DT>>.containsAll(
    elements: (Array<DT>) -> Array<DT>
) = validation {
    elements(subject).all { subject.contains(it) }
}

fun <DT> Constraint<Array<DT>>.containsAll(
    vararg elements: DT
) = validation {
    elements.all { subject.contains(it) }
}

fun <DT> Constraint<Array<DT>>.isPartOf(
    elements: (Array<DT>) -> Array<DT>
) = validation {
    val elementsList = elements(subject)
    subject.all { elementsList.contains(it) }
}

fun <DT> Constraint<Array<DT>>.isPartOf(
    vararg elements: DT
) = validation {
    subject.all { elements.contains(it) }
}

fun Constraint<Array<Boolean>>.allTrue() = validation {
    subject.all { it }
}

fun Constraint<Array<Boolean>>.anyTrue() = validation {
    subject.any { it }
}

fun Constraint<Array<Boolean>>.anyFalse() = validation {
    subject.any { !it }
}

fun Constraint<Array<Boolean>>.allFalse() = validation {
    subject.none { it }
}

fun <DT> Constraint<Array<DT>>.contentEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: (Array<DT>) -> Array<DT>
) = validation {
    subject.asList().contentEquals(
        ignoreDuplicates,
        ignoreOrder,
        other(subject).asList()
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
) = validation {
    subject.asList().contentNotEquals(
        ignoreDuplicates,
        ignoreOrder,
        other(subject).asList()
    )
}

fun <DT> Constraint<Array<DT>>.contentNotEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: Array<DT>
) = this.contentNotEquals(ignoreDuplicates, ignoreOrder) { other }
