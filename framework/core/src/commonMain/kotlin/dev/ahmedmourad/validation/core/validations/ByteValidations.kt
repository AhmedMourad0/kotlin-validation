package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint

inline fun Constraint<Byte>.isDivisibleBy(
    crossinline other: (Byte) -> Byte
) = validation {
    it % other(it) == 0
}

fun Constraint<Byte>.isDivisibleBy(other: Byte) = isDivisibleBy { other }

inline fun Constraint<Byte>.isNotDivisibleBy(
    crossinline other: (Byte) -> Byte
) = validation {
    it % other(it) != 0
}

fun Constraint<Byte>.isNotDivisibleBy(other: Byte) = isNotDivisibleBy { other }

fun Constraint<Byte>.isEven() = validation {
    it % 2 == 0
}

fun Constraint<Byte>.isOdd() = validation {
    it % 2 != 0
}

fun Constraint<Byte>.isPositive(orZero: Boolean) = validation {
    if (orZero) {
        it >= 0
    } else {
        it > 0
    }
}

fun Constraint<Byte>.isNegative(orZero: Boolean) = validation {
    if (orZero) {
        it <= 0
    } else {
        it < 0
    }
}

fun Constraint<Byte>.isZero() = validation {
    it == 0.toByte()
}

fun Constraint<Byte>.isNotZero() = validation {
    it != 0.toByte()
}

fun Constraint<Byte>.isPrime() = validation {
    if (it < 2) return@validation false
    (2..(it / 2)).none { n ->
        it % n == 0
    }
}

fun Constraint<Byte>.isNotPrime() = validation {
    if (it < 2) return@validation true
    (2..(it / 2)).any { n ->
        it % n == 0
    }
}
