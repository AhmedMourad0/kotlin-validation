package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint
import dev.ahmedmourad.validation.core.ScopedConstraintBuilder

inline fun <DT> Constraint<out Iterable<DT>>.forAll(
    crossinline itemConstraint: Constraint<DT>.() -> Unit
) = validation {
    subject.all {
        ScopedConstraintBuilder<DT>().apply(itemConstraint).validateAll(it)
    }
}

inline fun <DT> Constraint<out Iterable<DT>>.forAny(
    crossinline itemConstraint: Constraint<DT>.() -> Unit
) = validation {
    subject.any {
        ScopedConstraintBuilder<DT>().apply(itemConstraint).validateAll(it)
    }
}

inline fun <DT> Constraint<out Iterable<DT>>.forNone(
    crossinline itemConstraint: Constraint<DT>.() -> Unit
) = validation {
    subject.none {
        ScopedConstraintBuilder<DT>().apply(itemConstraint).validateAll(it)
    }
}

fun <DT> Constraint<out Iterable<DT>>.isEmpty() = validation {
    subject.count() == 0
}

fun <DT> Constraint<out Iterable<DT>>.isNotEmpty() = validation {
    subject.count() > 0
}

fun <DT> Constraint<out Iterable<DT>>.isDistinct() = validation {
    subject.distinct().count() == subject.count()
}

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.minSize(
    crossinline min: (DTI) -> Int
) = validation {
    subject.count() >= min(subject)
}

fun <DT> Constraint<out Iterable<DT>>.minSize(min: Int) = minSize { min }

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.maxSize(
    crossinline max: (DTI) -> Int
) = validation {
    subject.count() <= max(subject)
}

fun <DT> Constraint<out Iterable<DT>>.maxSize(max: Int) = maxSize { max }

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.sizeLessThan(
    crossinline maxExclusive: (DTI) -> Int
) = validation {
    subject.count() < maxExclusive(subject)
}

fun <DT> Constraint<out Iterable<DT>>.sizeLessThan(maxExclusive: Int) = sizeLessThan { maxExclusive }

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.sizeLargerThan(
    crossinline minExclusive: (DTI) -> Int
) = validation {
    subject.count() > minExclusive(subject)
}

fun <DT> Constraint<out Iterable<DT>>.sizeLargerThan(minExclusive: Int) = sizeLargerThan { minExclusive }

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.sizeIn(
    crossinline range: (DTI) -> IntRange
) = validation {
    subject.count() in range(subject)
}

fun <DT> Constraint<out Iterable<DT>>.sizeIn(range: IntRange) = sizeIn { range }

fun <DT> Constraint<out Iterable<DT>>.sizeIn(min: Int, max: Int) = sizeIn(min..max)

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.sizeNotIn(
    crossinline range: (DTI) -> IntRange
) = validation {
    subject.count() !in range(subject)
}

fun <DT> Constraint<out Iterable<DT>>.sizeNotIn(range: IntRange) = sizeNotIn { range }

fun <DT> Constraint<out Iterable<DT>>.sizeNotIn(min: Int, max: Int) = sizeNotIn(min..max)

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.sizeEqualTo(
    crossinline value: (DTI) -> Int
) = validation {
    subject.count() == value(subject)
}

fun <DT> Constraint<out Iterable<DT>>.sizeEqualTo(value: Int) = sizeEqualTo { value }

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.sizeNotEqualTo(
    crossinline value: (DTI) -> Int
) = validation {
    subject.count() != value(subject)
}

fun <DT> Constraint<out Iterable<DT>>.sizeNotEqualTo(value: Int) = sizeNotEqualTo { value }

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.contains(
    crossinline element: (DTI) -> DT
) = validation {
    subject.contains(element(subject))
}

fun <DT> Constraint<out Iterable<DT>>.contains(
    element: DT
) = contains { element }

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.doesNotContain(
    crossinline element: (DTI) -> DT
) = validation {
    !subject.contains(element(subject))
}

fun <DT> Constraint<out Iterable<DT>>.doesNotContain(
    element: DT
) = doesNotContain { element }

inline fun <DT, DTI : Iterable<DT>> Constraint<DTI>.containsAt(
    index: Int,
    crossinline element: (DTI) -> DT
) = validation {
    try {
        subject.elementAt(index) == element(subject)
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
        subject.first() == element(subject)
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
        subject.last() == element(subject)
    } catch (e: NoSuchElementException) {
        false
    }
}

fun <DT> Constraint<out Iterable<DT>>.endsWith(
    element: DT
) = endsWith { element }

fun <DT, DTI : Iterable<DT>> Constraint<DTI>.containsAll(
    elements: (DTI) -> Iterable<DT>
) = validation {
    elements(subject).all { subject.contains(it) }
}

fun <DT, DTI : Iterable<DT>> Constraint<DTI>.containsAll(
    vararg elements: DT
) = containsAll { elements.toList() }

fun <DT, DTI : Iterable<DT>> Constraint<DTI>.containsAll(
    elements: Iterable<DT>
) = containsAll { elements }

fun <DT, DTI : Iterable<DT>> Constraint<DTI>.isPartOf(
    elements: (DTI) -> Iterable<DT>
) = validation {
    val elementsList = elements(subject)
    subject.all { elementsList.contains(it) }
}

fun <DT, DTI : Iterable<DT>> Constraint<DTI>.isPartOf(
    vararg elements: DT
) = isPartOf { elements.toList() }

fun <DT, DTI : Iterable<DT>> Constraint<DTI>.isPartOf(
    elements: Iterable<DT>
) = isPartOf { elements }

fun Constraint<out Iterable<Boolean>>.allTrue() = validation {
    subject.all { it }
}

fun Constraint<out Iterable<Boolean>>.anyTrue() = validation {
    subject.any { it }
}

fun Constraint<out Iterable<Boolean>>.anyFalse() = validation {
    subject.any { !it }
}

fun Constraint<out Iterable<Boolean>>.allFalse() = validation {
    subject.none { it }
}

fun <DT, DTI : Iterable<DT>> Constraint<DTI>.contentEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: (DTI) -> Iterable<DT>
) = validation {
    subject.contentEquals(
        ignoreDuplicates,
        ignoreOrder,
        other(subject)
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
) = validation {
    subject.contentNotEquals(
        ignoreDuplicates,
        ignoreOrder,
        other(subject)
    )
}

fun <DT> Constraint<out Iterable<DT>>.contentNotEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: Iterable<DT>
) = contentNotEquals(ignoreDuplicates, ignoreOrder) { other }
