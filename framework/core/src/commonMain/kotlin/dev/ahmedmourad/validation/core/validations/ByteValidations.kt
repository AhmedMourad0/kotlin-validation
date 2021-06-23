package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint

inline fun Constraint<Byte>.isDivisibleBy(
    crossinline other: (Byte) -> Byte
) = validation {
    subject % other(subject) == 0
}

fun Constraint<Byte>.isDivisibleBy(other: Byte) = isDivisibleBy { other }

inline fun Constraint<Byte>.isNotDivisibleBy(
    crossinline other: (Byte) -> Byte
) = validation {
    subject % other(subject) != 0
}

fun Constraint<Byte>.isNotDivisibleBy(other: Byte) = isNotDivisibleBy { other }

fun Constraint<Byte>.isEven() = validation {
    subject % 2 == 0
}

fun Constraint<Byte>.isOdd() = validation {
    subject % 2 != 0
}

fun Constraint<Byte>.isPositive(orZero: Boolean) = validation {
    if (orZero) {
        subject >= 0
    } else {
        subject > 0
    }
}

fun Constraint<Byte>.isNegative(orZero: Boolean) = validation {
    if (orZero) {
        subject <= 0
    } else {
        subject < 0
    }
}

fun Constraint<Byte>.isZero() = validation {
    subject == 0.toByte()
}

fun Constraint<Byte>.isNotZero() = validation {
    subject != 0.toByte()
}

fun Constraint<Byte>.isPrime() = validation {
    if (subject < 2) return@validation false
    (2..(subject / 2)).none { n ->
        subject % n == 0
    }
}

fun Constraint<Byte>.isNotPrime() = validation {
    if (subject < 2) return@validation true
    (2..(subject / 2)).any { n ->
        subject % n == 0
    }
}
