package dev.ahmedmourad.validation.core

inline fun <DT> Validator<out Sequence<DT>>.all(
    crossinline itemValidator: Validator<DT>.() -> Unit
) = validation { validated ->
    validated.all {
        Validator<DT>().apply(itemValidator).validateAll(it)
    }
}

inline fun <DT> Validator<out Sequence<DT>>.any(
    crossinline itemValidator: Validator<DT>.() -> Unit
) = validation { validated ->
    validated.any {
        Validator<DT>().apply(itemValidator).validateAll(it)
    }
}

inline fun <DT> Validator<out Sequence<DT>>.none(
    crossinline itemValidator: Validator<DT>.() -> Unit
) = validation { validated ->
    validated.none {
        Validator<DT>().apply(itemValidator).validateAll(it)
    }
}

fun <DT> Validator<out Sequence<DT>>.isEmpty() = validation {
    it.count() == 0
}

fun <DT> Validator<out Sequence<DT>>.isNotEmpty() = validation {
    it.count() > 0
}

fun <DT> Validator<out Sequence<DT>>.isDistinct() = validation {
    it.distinct() == it
}

inline fun <DT, DTS : Sequence<DT>> Validator<DTS>.minSize(
    crossinline min: (DTS) -> Int
) = validation {
    it.count() >= min(it)
}

fun <DT> Validator<out Sequence<DT>>.minSize(min: Int) = minSize { min }

inline fun <DT, DTS : Sequence<DT>> Validator<DTS>.maxSize(
    crossinline max: (DTS) -> Int
) = validation {
    it.count() <= max(it)
}

fun <DT> Validator<out Sequence<DT>>.maxSize(max: Int) = maxSize { max }

inline fun <DT, DTS : Sequence<DT>> Validator<DTS>.sizeLessThan(
    crossinline minExclusive: (DTS) -> Int
) = validation {
    it.count() > minExclusive(it)
}

fun <DT> Validator<out Sequence<DT>>.sizeLessThan(minExclusive: Int) = sizeLessThan { minExclusive }

inline fun <DT, DTS : Sequence<DT>> Validator<DTS>.sizeLargerThan(
    crossinline maxExclusive: (DTS) -> Int
) = validation {
    it.count() < maxExclusive(it)
}

fun <DT> Validator<out Sequence<DT>>.sizeLargerThan(maxExclusive: Int) = sizeLargerThan { maxExclusive }

inline fun <DT, DTS : Sequence<DT>> Validator<DTS>.sizeIn(
    crossinline range: (DTS) -> IntRange
) = validation {
    it.count() in range(it)
}

fun <DT> Validator<out Sequence<DT>>.sizeIn(range: IntRange) = sizeIn { range }

fun <DT> Validator<out Sequence<DT>>.sizeIn(min: Int, max: Int) = sizeIn(min..max)

inline fun <DT, DTS : Sequence<DT>> Validator<DTS>.sizeEqualTo(
    crossinline value: (DTS) -> Int
) = validation {
    it.count() == value(it)
}

fun <DT> Validator<out Sequence<DT>>.sizeEqualTo(value: Int) = sizeEqualTo { value }

inline fun <DT, DTS : Sequence<DT>> Validator<DTS>.sizeNotEqualTo(
    crossinline value: (DTS) -> Int
) = validation {
    it.count() != value(it)
}

fun <DT> Validator<out Sequence<DT>>.sizeNotEqualTo(value: Int) = sizeNotEqualTo { value }

inline fun <DT, DTS : Sequence<DT>> Validator<DTS>.contains(
    crossinline element: (DTS) -> DT
) = validation {
    it.contains(element(it))
}

fun <DT> Validator<out Sequence<DT>>.contains(
    element: DT
) = contains { element }

inline fun <DT, DTS : Sequence<DT>> Validator<DTS>.containsAt(
    index: Int,
    crossinline element: (DTS) -> DT
) = validation {
    try {
        it.elementAt(index) == element(it)
    } catch (e: IndexOutOfBoundsException) {
        false
    }
}

fun <DT> Validator<out Sequence<DT>>.containsAt(
    index: Int,
    element: DT
) = containsAt(index) { element }

inline fun <DT, DTS : Sequence<DT>> Validator<DTS>.startsWith(
    crossinline element: (DTS) -> DT
) = validation {
    try {
        it.first() == element(it)
    } catch (e: NoSuchElementException) {
        false
    }
}

fun <DT> Validator<out Sequence<DT>>.startsWith(
    element: DT
): Unit = startsWith { element }

inline fun <DT, DTS : Sequence<DT>> Validator<DTS>.endsWith(
    crossinline element: (DTS) -> DT
) = validation {
    try {
        it.last() == element(it)
    } catch (e: NoSuchElementException) {
        false
    }
}

fun <DT> Validator<out Sequence<DT>>.endsWith(
    element: DT
) = endsWith { element }

fun <DT, DTS : Sequence<DT>> Validator<DTS>.containsAll(
    elements: (DTS) -> Sequence<DT>
) = validation { validated ->
    elements(validated).all { validated.contains(it) }
}

fun <DT, DTS : Sequence<DT>> Validator<DTS>.containsAll(
    vararg elements: DT
) = containsAll { elements.asSequence() }

fun <DT, DTS : Sequence<DT>> Validator<DTS>.containsAll(
    elements: Sequence<DT>
) = containsAll { elements }

fun <DT, DTS : Sequence<DT>> Validator<DTS>.isPartOf(
    elements: (DTS) -> Sequence<DT>
) = validation { validated ->
    val elementsList = elements(validated)
    validated.all { elementsList.contains(it) }
}

fun <DT, DTS : Sequence<DT>> Validator<DTS>.isPartOf(
    vararg elements: DT
) = isPartOf { elements.asSequence() }

fun <DT, DTS : Sequence<DT>> Validator<DTS>.isPartOf(
    elements: Sequence<DT>
) = isPartOf { elements }

fun Validator<out Sequence<Boolean>>.all() = validation { validated ->
    validated.all { it }
}

fun Validator<out Sequence<Boolean>>.any() = validation { validated ->
    validated.any { it }
}

fun Validator<out Sequence<Boolean>>.none() = validation { validated ->
    validated.none { it }
}

fun <DT, DTS : Sequence<DT>> Validator<DTS>.isEqualTo(other: DTS) = validation {
    val thisCount = it.count()
    if (thisCount != other.count()) {
        false
    } else {
        for (i in 0 until thisCount) {
            if (it.elementAt(i) != other.elementAt(i)) {
                return@validation false
            }
        }
        true
    }
}

fun <DT, DTS : Sequence<DT>> Validator<DTS>.isNotEqualTo(other: DTS) = validation {
    val thisCount = it.count()
    if (thisCount != other.count()) {
        true
    } else {
        for (i in 0 until thisCount) {
            if (it.elementAt(i) != other.elementAt(i)) {
                return@validation true
            }
        }
        false
    }
}
