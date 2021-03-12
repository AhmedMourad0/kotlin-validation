package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Validator
import dev.ahmedmourad.validation.core.validation

fun Validator<out Map<*, *>>.isEmpty() = validation {
    it.isEmpty()
}

fun Validator<out Map<*, *>>.isNotEmpty() = validation {
    it.isNotEmpty()
}

inline fun <K, V, DTM : Map<K, V>> Validator<DTM>.minSize(
    crossinline min: (DTM) -> Int
) = validation {
    it.size >= min(it)
}

fun <K, V> Validator<out Map<K, V>>.minSize(min: Int) = minSize { min }

inline fun <K, V, DTM : Map<K, V>> Validator<DTM>.maxSize(
    crossinline max: (DTM) -> Int
) = validation {
    it.size <= max(it)
}

fun <K, V> Validator<out Map<K, V>>.maxSize(max: Int) = maxSize { max }

inline fun <K, V, DTM : Map<K, V>> Validator<DTM>.sizeLessThan(
    crossinline minExclusive: (DTM) -> Int
) = validation {
    it.size > minExclusive(it)
}

fun <K, V> Validator<out Map<K, V>>.sizeLessThan(minExclusive: Int) = sizeLessThan { minExclusive }

inline fun <K, V, DTM : Map<K, V>> Validator<DTM>.sizeLargerThan(
    crossinline maxExclusive: (DTM) -> Int
) = validation {
    it.size < maxExclusive(it)
}

fun <K, V> Validator<out Map<K, V>>.sizeLargerThan(maxExclusive: Int) = sizeLargerThan { maxExclusive }

inline fun <K, V, DTM : Map<K, V>> Validator<DTM>.sizeIn(
    crossinline range: (DTM) -> IntRange
) = validation {
    it.size in range(it)
}

fun <K, V> Validator<out Map<K, V>>.sizeIn(range: IntRange) = sizeIn { range }

fun <K, V> Validator<out Map<K, V>>.sizeIn(min: Int, max: Int) = sizeIn(min..max)

inline fun <K, V, DTM : Map<K, V>> Validator<DTM>.sizeEqualTo(
    crossinline value: (DTM) -> Int
) = validation {
    it.size == value(it)
}

fun <K, V> Validator<out Map<K, V>>.sizeEqualTo(value: Int) = sizeEqualTo { value }

inline fun <K, V, DTM : Map<K, V>> Validator<DTM>.sizeNotEqualTo(
    crossinline value: (DTM) -> Int
) = validation {
    it.size != value(it)
}

fun <K, V> Validator<out Map<K, V>>.sizeNotEqualTo(value: Int) = sizeNotEqualTo { value }

inline fun <K, V> Validator<Map<K, V>>.keys(
    crossinline keysValidator: Validator<Set<K>>.() -> Unit
) = on(Map<K, V>::keys, keysValidator)

inline fun <K, V> Validator<Map<K, V>>.values(
    crossinline valuesValidator: Validator<Collection<V>>.() -> Unit
) = on(Map<K, V>::values, valuesValidator)

inline fun <K, V> Validator<Map<K, V>>.entries(
    crossinline entriesValidator: Validator<Set<Map.Entry<K, V>>>.() -> Unit
) = on(Map<K, V>::entries, entriesValidator)
