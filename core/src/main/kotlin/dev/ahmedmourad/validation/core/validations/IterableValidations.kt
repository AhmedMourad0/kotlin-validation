package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Validator
import dev.ahmedmourad.validation.core.validation

inline fun <DT> Validator<out Iterable<DT>>.forAll(
    crossinline itemValidator: Validator<DT>.() -> Unit
) = validation { validated ->
    validated.all {
        Validator<DT>().apply(itemValidator).validateAll(it)
    }
}

inline fun <DT> Validator<out Iterable<DT>>.forAny(
    crossinline itemValidator: Validator<DT>.() -> Unit
) = validation { validated ->
    validated.any {
        Validator<DT>().apply(itemValidator).validateAll(it)
    }
}

inline fun <DT> Validator<out Iterable<DT>>.forNone(
    crossinline itemValidator: Validator<DT>.() -> Unit
) = validation { validated ->
    validated.none {
        Validator<DT>().apply(itemValidator).validateAll(it)
    }
}

fun <DT> Validator<out Iterable<DT>>.isEmpty() = validation {
    it.count() == 0
}

fun <DT> Validator<out Iterable<DT>>.isNotEmpty() = validation {
    it.count() > 0
}

fun <DT> Validator<out Iterable<DT>>.isDistinct() = validation {
    it.distinct().count() == it.count()
}

inline fun <DT, DTI : Iterable<DT>> Validator<DTI>.minSize(
    crossinline min: (DTI) -> Int
) = validation {
    it.count() >= min(it)
}

fun <DT> Validator<out Iterable<DT>>.minSize(min: Int) = minSize { min }

inline fun <DT, DTI : Iterable<DT>> Validator<DTI>.maxSize(
    crossinline max: (DTI) -> Int
) = validation {
    it.count() <= max(it)
}

fun <DT> Validator<out Iterable<DT>>.maxSize(max: Int) = maxSize { max }

inline fun <DT, DTI : Iterable<DT>> Validator<DTI>.sizeLessThan(
    crossinline maxExclusive: (DTI) -> Int
) = validation {
    it.count() < maxExclusive(it)
}

fun <DT> Validator<out Iterable<DT>>.sizeLessThan(maxExclusive: Int) = sizeLessThan { maxExclusive }

inline fun <DT, DTI : Iterable<DT>> Validator<DTI>.sizeLargerThan(
    crossinline minExclusive: (DTI) -> Int
) = validation {
    it.count() > minExclusive(it)
}

fun <DT> Validator<out Iterable<DT>>.sizeLargerThan(minExclusive: Int) = sizeLargerThan { minExclusive }

inline fun <DT, DTI : Iterable<DT>> Validator<DTI>.sizeIn(
    crossinline range: (DTI) -> IntRange
) = validation {
    it.count() in range(it)
}

fun <DT> Validator<out Iterable<DT>>.sizeIn(range: IntRange) = sizeIn { range }

fun <DT> Validator<out Iterable<DT>>.sizeIn(min: Int, max: Int) = sizeIn(min..max)

inline fun <DT, DTI : Iterable<DT>> Validator<DTI>.sizeNotIn(
    crossinline range: (DTI) -> IntRange
) = validation {
    it.count() !in range(it)
}

fun <DT> Validator<out Iterable<DT>>.sizeNotIn(range: IntRange) = sizeNotIn { range }

fun <DT> Validator<out Iterable<DT>>.sizeNotIn(min: Int, max: Int) = sizeNotIn(min..max)

inline fun <DT, DTI : Iterable<DT>> Validator<DTI>.sizeEqualTo(
    crossinline value: (DTI) -> Int
) = validation {
    it.count() == value(it)
}

fun <DT> Validator<out Iterable<DT>>.sizeEqualTo(value: Int) = sizeEqualTo { value }

inline fun <DT, DTI : Iterable<DT>> Validator<DTI>.sizeNotEqualTo(
    crossinline value: (DTI) -> Int
) = validation {
    it.count() != value(it)
}

fun <DT> Validator<out Iterable<DT>>.sizeNotEqualTo(value: Int) = sizeNotEqualTo { value }

inline fun <DT, DTI : Iterable<DT>> Validator<DTI>.contains(
    crossinline element: (DTI) -> DT
) = validation {
    it.contains(element(it))
}

fun <DT> Validator<out Iterable<DT>>.contains(
    element: DT
) = contains { element }

inline fun <DT, DTI : Iterable<DT>> Validator<DTI>.doesNotContain(
    crossinline element: (DTI) -> DT
) = validation {
    !it.contains(element(it))
}

fun <DT> Validator<out Iterable<DT>>.doesNotContain(
    element: DT
) = doesNotContain { element }

inline fun <DT, DTI : Iterable<DT>> Validator<DTI>.containsAt(
    index: Int,
    crossinline element: (DTI) -> DT
) = validation {
    try {
        it.elementAt(index) == element(it)
    } catch (e: IndexOutOfBoundsException) {
        false
    }
}

fun <DT> Validator<out Iterable<DT>>.containsAt(
    index: Int,
    element: DT
) = containsAt(index) { element }

inline fun <DT, DTI : Iterable<DT>> Validator<DTI>.startsWith(
    crossinline element: (DTI) -> DT
) = validation {
    try {
        it.first() == element(it)
    } catch (e: NoSuchElementException) {
        false
    }
}

fun <DT> Validator<out Iterable<DT>>.startsWith(
    element: DT
) = startsWith { element }

inline fun <DT, DTI : Iterable<DT>> Validator<DTI>.endsWith(
    crossinline element: (DTI) -> DT
) = validation {
    try {
        it.last() == element(it)
    } catch (e: NoSuchElementException) {
        false
    }
}

fun <DT> Validator<out Iterable<DT>>.endsWith(
    element: DT
) = endsWith { element }

fun <DT, DTI : Iterable<DT>> Validator<DTI>.containsAll(
    elements: (DTI) -> Iterable<DT>
) = validation { validated ->
    elements(validated).all { validated.contains(it) }
}

fun <DT, DTI : Iterable<DT>> Validator<DTI>.containsAll(
    vararg elements: DT
) = containsAll { elements.toList() }

fun <DT, DTI : Iterable<DT>> Validator<DTI>.containsAll(
    elements: Iterable<DT>
) = containsAll { elements }

fun <DT, DTI : Iterable<DT>> Validator<DTI>.isPartOf(
    elements: (DTI) -> Iterable<DT>
) = validation { validated ->
    val elementsList = elements(validated)
    validated.all { elementsList.contains(it) }
}

fun <DT, DTI : Iterable<DT>> Validator<DTI>.isPartOf(
    vararg elements: DT
) = isPartOf { elements.toList() }

fun <DT, DTI : Iterable<DT>> Validator<DTI>.isPartOf(
    elements: Iterable<DT>
) = isPartOf { elements }

fun Validator<out Iterable<Boolean>>.allTrue() = validation { validated ->
    validated.all { it }
}

fun Validator<out Iterable<Boolean>>.anyTrue() = validation { validated ->
    validated.any { it }
}

fun Validator<out Iterable<Boolean>>.anyFalse() = validation { validated ->
    validated.any { !it }
}

fun Validator<out Iterable<Boolean>>.allFalse() = validation { validated ->
    validated.none { it }
}

fun <DT, DTI : Iterable<DT>> Validator<DTI>.contentEquals(
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

fun <DT> Validator<out Iterable<DT>>.contentEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: Iterable<DT>
) = contentEquals(ignoreDuplicates, ignoreOrder) { other }

fun <DT, DTI : Iterable<DT>> Validator<DTI>.contentNotEquals(
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

fun <DT> Validator<out Iterable<DT>>.contentNotEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: Iterable<DT>
) = contentNotEquals(ignoreDuplicates, ignoreOrder) { other }
