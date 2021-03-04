package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Validator
import dev.ahmedmourad.validation.core.validation

inline fun Validator<Int>.isDivisibleBy(
    crossinline other: (Int) -> Int
) = validation {
    it % other(it) == 0
}

fun Validator<Int>.isDivisibleBy(other: Int) = isDivisibleBy { other }

inline fun Validator<Int>.isNotDivisibleBy(
    crossinline other: (Int) -> Int
) = validation {
    it % other(it) != 0
}

fun Validator<Int>.isNotDivisibleBy(other: Int) = isNotDivisibleBy { other }

fun Validator<Int>.isEven() = validation {
    it % 2 == 0
}

fun Validator<Int>.isOdd() = validation {
    it % 2 != 0
}

fun Validator<Int>.isPositive(orZero: Boolean) = validation {
    if (orZero) {
        it >= 0
    } else {
        it > 0
    }
}

fun Validator<Int>.isNegative(orZero: Boolean) = validation {
    if (orZero) {
        it <= 0
    } else {
        it < 0
    }
}

fun Validator<Int>.isZero() = validation {
    it == 0
}

fun Validator<Int>.isNotZero() = validation {
    it != 0
}

fun Validator<Int>.isPrime() = validation {
    (2..(it / 2)).none { n ->
        it % n == 0
    }
}

fun Validator<Int>.isNotPrime() = validation {
    (2..(it / 2)).any { n ->
        it % n == 0
    }
}
