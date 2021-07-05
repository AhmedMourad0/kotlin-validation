package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint
import dev.ahmedmourad.validation.core.ScopedConstraintBuilder

fun Constraint<out Map<*, *>>.isEmpty() = validation {
    subject.isEmpty()
}

fun Constraint<out Map<*, *>>.isNotEmpty() = validation {
    subject.isNotEmpty()
}

inline fun <K, V, DTM : Map<K, V>> Constraint<DTM>.minSize(
    crossinline min: (DTM) -> Int
) = validation {
    subject.size >= min(subject)
}

fun <K, V> Constraint<out Map<K, V>>.minSize(min: Int) = minSize { min }

inline fun <K, V, DTM : Map<K, V>> Constraint<DTM>.maxSize(
    crossinline max: (DTM) -> Int
) = validation {
    subject.size <= max(subject)
}

fun <K, V> Constraint<out Map<K, V>>.maxSize(max: Int) = maxSize { max }

inline fun <K, V, DTM : Map<K, V>> Constraint<DTM>.sizeLessThan(
    crossinline maxExclusive: (DTM) -> Int
) = validation {
    subject.size < maxExclusive(subject)
}

fun <K, V> Constraint<out Map<K, V>>.sizeLessThan(maxExclusive: Int) = sizeLessThan { maxExclusive }

inline fun <K, V, DTM : Map<K, V>> Constraint<DTM>.sizeLargerThan(
    crossinline minExclusive: (DTM) -> Int
) = validation {
    subject.size > minExclusive(subject)
}

fun <K, V> Constraint<out Map<K, V>>.sizeLargerThan(minExclusive: Int) = sizeLargerThan { minExclusive }

inline fun <K, V, DTM : Map<K, V>> Constraint<DTM>.sizeIn(
    crossinline range: (DTM) -> IntRange
) = validation {
    subject.size in range(subject)
}

fun <K, V> Constraint<out Map<K, V>>.sizeIn(range: IntRange) = sizeIn { range }

fun <K, V> Constraint<out Map<K, V>>.sizeIn(min: Int, max: Int) = sizeIn(min..max)

inline fun <K, V, DTM : Map<K, V>> Constraint<DTM>.sizeNotIn(
    crossinline range: (DTM) -> IntRange
) = validation {
    subject.size !in range(subject)
}

fun <K, V> Constraint<out Map<K, V>>.sizeNotIn(range: IntRange) = sizeNotIn { range }

fun <K, V> Constraint<out Map<K, V>>.sizeNotIn(min: Int, max: Int) = sizeNotIn(min..max)

inline fun <K, V, DTM : Map<K, V>> Constraint<DTM>.sizeEqualTo(
    crossinline value: (DTM) -> Int
) = validation {
    subject.size == value(subject)
}

fun <K, V> Constraint<out Map<K, V>>.sizeEqualTo(value: Int) = sizeEqualTo { value }

inline fun <K, V, DTM : Map<K, V>> Constraint<DTM>.sizeNotEqualTo(
    crossinline value: (DTM) -> Int
) = validation {
    subject.size != value(subject)
}

fun <K, V> Constraint<out Map<K, V>>.sizeNotEqualTo(value: Int) = sizeNotEqualTo { value }

fun <K, V> Constraint<Map<K, V>>.keys(
    keysConstraint: Constraint<Set<K>>.() -> Unit
) = on(Map<K, V>::keys, keysConstraint)

fun <K, V> Constraint<Map<K, V>>.values(
    valuesConstraint: Constraint<Collection<V>>.() -> Unit
) = on(Map<K, V>::values, valuesConstraint)

fun <K, V> Constraint<Map<K, V>>.entries(
    entriesConstraint: Constraint<Set<Map.Entry<K, V>>>.() -> Unit
) = on(Map<K, V>::entries, entriesConstraint)

fun <K, V> Constraint<Map.Entry<K, V>>.key(
    keyConstraint: Constraint<K>.() -> Unit
) = validation {
    ScopedConstraintBuilder<K>().apply(keyConstraint).matchesAll(subject.key)
}

inline fun <K, V> Constraint<Map.Entry<K, V>>.value(
    crossinline valueConstraint: Constraint<V>.() -> Unit
) = validation {
    ScopedConstraintBuilder<V>().apply(valueConstraint).matchesAll(subject.value)
}
