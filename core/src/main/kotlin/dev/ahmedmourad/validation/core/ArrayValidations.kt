package dev.ahmedmourad.validation.core

inline fun <DT> Validator<Array<DT>>.all(
    crossinline itemValidator: Validator<DT>.() -> Unit
) = validation { validated ->
    validated.all {
        Validator<DT>().apply(itemValidator).validateAll(it)
    }
}

inline fun <DT> Validator<Array<DT>>.any(
    crossinline itemValidator: Validator<DT>.() -> Unit
) = validation { validated ->
    validated.any {
        Validator<DT>().apply(itemValidator).validateAll(it)
    }
}

inline fun <DT> Validator<Array<DT>>.none(
    crossinline itemValidator: Validator<DT>.() -> Unit
) = validation { validated ->
    validated.none {
        Validator<DT>().apply(itemValidator).validateAll(it)
    }
}

fun <DT> Validator<Array<DT>>.isEmpty() = validation {
    it.count() == 0
}

fun <DT> Validator<Array<DT>>.isNotEmpty() = validation {
    it.count() > 0
}

fun <DT> Validator<Array<DT>>.isDistinct() = validation {
    it.distinct() == it
}

inline fun <DT> Validator<Array<DT>>.minSize(
    crossinline min: (Array<DT>) -> Int
) = validation {
    it.count() >= min(it)
}

fun <DT> Validator<Array<DT>>.minSize(min: Int) = minSize { min }

inline fun <DT> Validator<Array<DT>>.maxSize(
    crossinline max: (Array<DT>) -> Int
) = validation {
    it.count() <= max(it)
}

fun <DT> Validator<Array<DT>>.maxSize(max: Int) = maxSize { max }

inline fun <DT> Validator<Array<DT>>.sizeLessThan(
    crossinline minExclusive: (Array<DT>) -> Int
) = validation {
    it.count() > minExclusive(it)
}

fun <DT> Validator<Array<DT>>.sizeLessThan(minExclusive: Int) = sizeLessThan { minExclusive }

inline fun <DT> Validator<Array<DT>>.sizeLargerThan(
    crossinline maxExclusive: (Array<DT>) -> Int
) = validation {
    it.count() < maxExclusive(it)
}

fun <DT> Validator<Array<DT>>.sizeLargerThan(maxExclusive: Int) = sizeLargerThan { maxExclusive }

inline fun <DT> Validator<Array<DT>>.sizeIn(
    crossinline range: (Array<DT>) -> IntRange
) = validation {
    it.count() in range(it)
}

fun <DT> Validator<Array<DT>>.sizeIn(range: IntRange) = sizeIn { range }

fun <DT> Validator<Array<DT>>.sizeIn(min: Int, max: Int) = sizeIn(min..max)

inline fun <DT> Validator<Array<DT>>.sizeEqualTo(
    crossinline value: (Array<DT>) -> Int
) = validation {
    it.count() == value(it)
}

fun <DT> Validator<Array<DT>>.sizeEqualTo(value: Int) = sizeEqualTo { value }

inline fun <DT> Validator<Array<DT>>.sizeNotEqualTo(
    crossinline value: (Array<DT>) -> Int
) = validation {
    it.count() != value(it)
}

fun <DT> Validator<Array<DT>>.sizeNotEqualTo(value: Int) = sizeNotEqualTo { value }

inline fun <DT> Validator<Array<DT>>.contains(
    crossinline element: (Array<DT>) -> DT
) = validation {
    it.contains(element(it))
}

fun <DT> Validator<Array<DT>>.contains(
    element: DT
) = validation {
    it.contains(element)
}

inline fun <DT> Validator<Array<DT>>.containsAt(
    index: Int,
    crossinline element: (Array<DT>) -> DT
) = validation {
    try {
        it.elementAt(index) == element(it)
    } catch (e: IndexOutOfBoundsException) {
        false
    }
}

fun <DT> Validator<Array<DT>>.containsAt(
    index: Int,
    element: DT
) = validation {
    try {
        it.elementAt(index) == element
    } catch (e: IndexOutOfBoundsException) {
        false
    }
}

inline fun <DT> Validator<Array<DT>>.startsWith(
    crossinline element: (Array<DT>) -> DT
) = validation {
    try {
        it.first() == element(it)
    } catch (e: NoSuchElementException) {
        false
    }
}

fun <DT> Validator<Array<DT>>.startsWith(
    element: DT
) = validation {
    try {
        it.first() == element
    } catch (e: NoSuchElementException) {
        false
    }
}

inline fun <DT> Validator<Array<DT>>.endsWith(
    crossinline element: (Array<DT>) -> DT
) = validation {
    try {
        it.last() == element(it)
    } catch (e: NoSuchElementException) {
        false
    }
}

fun <DT> Validator<Array<DT>>.endsWith(
    element: DT
) = validation {
    try {
        it.last() == element
    } catch (e: NoSuchElementException) {
        false
    }
}

fun <DT> Validator<Array<DT>>.containsAll(
    elements: (Array<DT>) -> Array<DT>
) = validation { validated ->
    elements(validated).all { validated.contains(it) }
}

fun <DT> Validator<Array<DT>>.containsAll(
    vararg elements: DT
) = validation { validated ->
    elements.all { validated.contains(it) }
}

fun <DT> Validator<Array<DT>>.isPartOf(
    elements: (Array<DT>) -> Array<DT>
) = validation { validated ->
    val elementsList = elements(validated)
    validated.all { elementsList.contains(it) }
}

fun <DT> Validator<Array<DT>>.isPartOf(
    vararg elements: DT
) = validation { validated ->
    validated.all { elements.contains(it) }
}

fun Validator<Array<Boolean>>.all() = validation { validated ->
    validated.all { it }
}

fun Validator<Array<Boolean>>.any() = validation { validated ->
    validated.any { it }
}

fun Validator<Array<Boolean>>.none() = validation { validated ->
    validated.none { it }
}

fun <DT> Validator<Array<DT>>.contentEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: (Array<DT>) -> Array<DT>
) = validation { validated ->

    val thisWithDistinct = if (ignoreDuplicates) validated.distinct() else validated.asList()
    val otherWithDistinct = if (ignoreDuplicates) other(validated).distinct() else other(validated).asList()

    val thisCount = thisWithDistinct.count()
    if (thisCount != otherWithDistinct.count()) {
        return@validation false
    }

    if (ignoreOrder) {
        thisWithDistinct.all { otherWithDistinct.contains(it) }
    } else {
        for (i in 0 until thisCount) {
            if (thisWithDistinct.elementAt(i) != otherWithDistinct.elementAt(i)) {
                return@validation false
            }
        }
        true
    }
}

fun <DT> Validator<Array<DT>>.contentEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: Array<DT>
) = contentEquals(ignoreDuplicates, ignoreOrder) { other }

fun <DT> Validator<Array<DT>>.contentNotEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: (Array<DT>) -> Array<DT>
): Unit = validation { validated ->

    val thisWithDistinct = if (ignoreDuplicates) validated.distinct() else validated.asList()
    val otherWithDistinct = if (ignoreDuplicates) other(validated).distinct() else other(validated).asList()

    val thisCount = thisWithDistinct.count()
    if (thisCount != otherWithDistinct.count()) {
        return@validation true
    }

    if (ignoreOrder) {
        thisWithDistinct.any { !otherWithDistinct.contains(it) }
    } else {
        for (i in 0 until thisCount) {
            if (thisWithDistinct.elementAt(i) != otherWithDistinct.elementAt(i)) {
                return@validation true
            }
        }
        false
    }
}

fun <DT> Validator<Array<DT>>.contentNotEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: Array<DT>
): Unit = this.contentNotEquals(ignoreDuplicates, ignoreOrder) { other }
