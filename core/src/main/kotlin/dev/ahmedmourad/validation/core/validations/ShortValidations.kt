package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Validator
import dev.ahmedmourad.validation.core.validation

inline fun Validator<Short>.isDivisibleBy(
    crossinline other: (Short) -> Short
) = validation {
    it % other(it) == 0
}

fun Validator<Short>.isDivisibleBy(other: Short) = isDivisibleBy { other }

inline fun Validator<Short>.isNotDivisibleBy(
    crossinline other: (Short) -> Short
) = validation {
    it % other(it) != 0
}

fun Validator<Short>.isNotDivisibleBy(other: Short) = isNotDivisibleBy { other }

fun Validator<Short>.isEven() = validation {
    it % 2 == 0
}

fun Validator<Short>.isOdd() = validation {
    it % 2 != 0
}

fun Validator<Short>.isPositive(orZero: Boolean) = validation {
    if (orZero) {
        it >= 0
    } else {
        it > 0
    }
}

fun Validator<Short>.isNegative(orZero: Boolean) = validation {
    if (orZero) {
        it <= 0
    } else {
        it < 0
    }
}

fun Validator<Short>.isZero() = validation {
    it == 0.toShort()
}

fun Validator<Short>.isNotZero() = validation {
    it != 0.toShort()
}

fun Validator<Short>.isPrime() = validation {
    if (it < 2) return@validation false
    (2..(it / 2)).none { n ->
        it % n == 0
    }
}

fun Validator<Short>.isNotPrime() = validation {
    if (it < 2) return@validation true
    (2..(it / 2)).any { n ->
        it % n == 0
    }
}
