package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Validator
import dev.ahmedmourad.validation.core.validation

inline fun Validator<Byte>.isDivisibleBy(
    crossinline other: (Byte) -> Byte
) = validation {
    it % other(it) == 0
}

fun Validator<Byte>.isDivisibleBy(other: Byte) = isDivisibleBy { other }

inline fun Validator<Byte>.isNotDivisibleBy(
    crossinline other: (Byte) -> Byte
) = validation {
    it % other(it) != 0
}

fun Validator<Byte>.isNotDivisibleBy(other: Byte) = isNotDivisibleBy { other }

fun Validator<Byte>.isEven() = validation {
    it % 2 == 0
}

fun Validator<Byte>.isOdd() = validation {
    it % 2 != 0
}

fun Validator<Byte>.isPositive(orZero: Boolean) = validation {
    if (orZero) {
        it >= 0
    } else {
        it > 0
    }
}

fun Validator<Byte>.isNegative(orZero: Boolean) = validation {
    if (orZero) {
        it <= 0
    } else {
        it < 0
    }
}

fun Validator<Byte>.isZero() = validation {
    it == 0.toByte()
}

fun Validator<Byte>.isNotZero() = validation {
    it != 0.toByte()
}

fun Validator<Byte>.isPrime() = validation {
    if (it < 2) return@validation false
    (2..(it / 2)).none { n ->
        it % n == 0
    }
}

fun Validator<Byte>.isNotPrime() = validation {
    if (it < 2) return@validation true
    (2..(it / 2)).any { n ->
        it % n == 0
    }
}
