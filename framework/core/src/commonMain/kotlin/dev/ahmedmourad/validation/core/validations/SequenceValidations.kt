package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint
import dev.ahmedmourad.validation.core.ScopedConstraintBuilder

inline fun <DT> Constraint<out Sequence<DT>>.forAll(
    crossinline itemConstraint: Constraint<DT>.() -> Unit
) = validation {
    subject.all {
        ScopedConstraintBuilder<DT>().apply(itemConstraint).validateAll(it)
    }
}

inline fun <DT> Constraint<out Sequence<DT>>.forAny(
    crossinline itemConstraint: Constraint<DT>.() -> Unit
) = validation {
    subject.any {
        ScopedConstraintBuilder<DT>().apply(itemConstraint).validateAll(it)
    }
}

inline fun <DT> Constraint<out Sequence<DT>>.forNone(
    crossinline itemConstraint: Constraint<DT>.() -> Unit
) = validation {
    subject.none {
        ScopedConstraintBuilder<DT>().apply(itemConstraint).validateAll(it)
    }
}

fun <DT> Constraint<out Sequence<DT>>.isEmpty() = validation {
    subject.count() == 0
}

fun <DT> Constraint<out Sequence<DT>>.isNotEmpty() = validation {
    subject.count() > 0
}

fun <DT> Constraint<out Sequence<DT>>.isDistinct() = validation {
    subject.distinct().count() == subject.count()
}

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.minSize(
    crossinline min: (DTS) -> Int
) = validation {
    subject.count() >= min(subject)
}

fun <DT> Constraint<out Sequence<DT>>.minSize(min: Int) = minSize { min }

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.maxSize(
    crossinline max: (DTS) -> Int
) = validation {
    subject.count() <= max(subject)
}

fun <DT> Constraint<out Sequence<DT>>.maxSize(max: Int) = maxSize { max }

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.sizeLessThan(
    crossinline maxExclusive: (DTS) -> Int
) = validation {
    subject.count() < maxExclusive(subject)
}

fun <DT> Constraint<out Sequence<DT>>.sizeLessThan(maxExclusive: Int) = sizeLessThan { maxExclusive }

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.sizeLargerThan(
    crossinline minExclusive: (DTS) -> Int
) = validation {
    subject.count() > minExclusive(subject)
}

fun <DT> Constraint<out Sequence<DT>>.sizeLargerThan(minExclusive: Int) = sizeLargerThan { minExclusive }

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.sizeIn(
    crossinline range: (DTS) -> IntRange
) = validation {
    subject.count() in range(subject)
}

fun <DT> Constraint<out Sequence<DT>>.sizeIn(range: IntRange) = sizeIn { range }

fun <DT> Constraint<out Sequence<DT>>.sizeIn(min: Int, max: Int) = sizeIn(min..max)

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.sizeNotIn(
    crossinline range: (DTS) -> IntRange
) = validation {
    subject.count() !in range(subject)
}

fun <DT> Constraint<out Sequence<DT>>.sizeNotIn(range: IntRange) = sizeNotIn { range }

fun <DT> Constraint<out Sequence<DT>>.sizeNotIn(min: Int, max: Int) = sizeNotIn(min..max)

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.sizeEqualTo(
    crossinline value: (DTS) -> Int
) = validation {
    subject.count() == value(subject)
}

fun <DT> Constraint<out Sequence<DT>>.sizeEqualTo(value: Int) = sizeEqualTo { value }

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.sizeNotEqualTo(
    crossinline value: (DTS) -> Int
) = validation {
    subject.count() != value(subject)
}

fun <DT> Constraint<out Sequence<DT>>.sizeNotEqualTo(value: Int) = sizeNotEqualTo { value }

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.contains(
    crossinline element: (DTS) -> DT
) = validation {
    subject.contains(element(subject))
}

fun <DT> Constraint<out Sequence<DT>>.contains(
    element: DT
) = contains { element }

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.doesNotContain(
    crossinline element: (DTS) -> DT
) = validation {
    !subject.contains(element(subject))
}

fun <DT> Constraint<out Sequence<DT>>.doesNotContain(
    element: DT
) = doesNotContain { element }

inline fun <DT, DTS : Sequence<DT>> Constraint<DTS>.containsAt(
    index: Int,
    crossinline element: (DTS) -> DT
) = validation {
    try {
        subject.elementAt(index) == element(subject)
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
        subject.first() == element(subject)
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
        subject.last() == element(subject)
    } catch (e: NoSuchElementException) {
        false
    }
}

fun <DT> Constraint<out Sequence<DT>>.endsWith(
    element: DT
) = endsWith { element }

fun <DT, DTS : Sequence<DT>> Constraint<DTS>.containsAll(
    elements: (DTS) -> Sequence<DT>
) = validation {
    elements(subject).all { subject.contains(it) }
}

fun <DT, DTS : Sequence<DT>> Constraint<DTS>.containsAll(
    vararg elements: DT
) = containsAll { elements.asSequence() }

fun <DT, DTS : Sequence<DT>> Constraint<DTS>.containsAll(
    elements: Sequence<DT>
) = containsAll { elements }

fun <DT, DTS : Sequence<DT>> Constraint<DTS>.isPartOf(
    elements: (DTS) -> Sequence<DT>
) = validation {
    val elementsList = elements(subject)
    subject.all { elementsList.contains(it) }
}

fun <DT, DTS : Sequence<DT>> Constraint<DTS>.isPartOf(
    vararg elements: DT
) = isPartOf { elements.asSequence() }

fun <DT, DTS : Sequence<DT>> Constraint<DTS>.isPartOf(
    elements: Sequence<DT>
) = isPartOf { elements }

fun Constraint<out Sequence<Boolean>>.allTrue() = validation {
    subject.all { it }
}

fun Constraint<out Sequence<Boolean>>.anyTrue() = validation {
    subject.any { it }
}

fun Constraint<out Sequence<Boolean>>.anyFalse() = validation {
    subject.any { !it }
}

fun Constraint<out Sequence<Boolean>>.allFalse() = validation {
    subject.none { it }
}

fun <DT, DTS : Sequence<DT>> Constraint<DTS>.contentEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: (DTS) -> Sequence<DT>
) = validation {
    subject.toList().contentEquals(
        ignoreDuplicates,
        ignoreOrder,
        other(subject).toList()
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
) = validation {
    subject.toList().contentNotEquals(
        ignoreDuplicates,
        ignoreOrder,
        other(subject).toList()
    )
}

fun <DT> Constraint<out Sequence<DT>>.contentNotEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: Sequence<DT>
) = contentNotEquals(ignoreDuplicates, ignoreOrder) { other }
