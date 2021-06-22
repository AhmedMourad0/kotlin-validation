package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.utils.allFail
import dev.ahmedmourad.validation.core.utils.allMatch
import dev.ahmedmourad.validation.core.utils.constraint
import dev.ahmedmourad.validation.core.validations.*
import kotlin.test.Test

class LongValidationsTests {

    @Test
    fun isDivisibleBy_meansThisLongIsDivisibleByTheGivenLong() {
        constraint<Long> {
            isDivisibleBy(3)
        }.allMatch(
            0,
            3,
            6,
            9
        ).allFail(
            1,
            2,
            4,
            5,
            7,
            8
        )
    }

    @Test
    fun isDivisibleByL_meansThisLongIsDivisibleByTheGivenLong() {
        constraint<Long> {
            isDivisibleBy { 3 }
        }.allMatch(
            0,
            3,
            6,
            9
        ).allFail(
            1,
            2,
            4,
            5,
            7,
            8
        )
    }

    @Test
    fun isNotDivisibleBy_meansThisLongIsNotDivisibleByTheGivenLong() {
        constraint<Long> {
            isNotDivisibleBy(3)
        }.allMatch(
            1,
            2,
            4,
            5,
            7,
            8
        ).allFail(
            0,
            3,
            6,
            9
        )
    }

    @Test
    fun isNotDivisibleByL_meansThisLongIsNotDivisibleByTheGivenLong() {
        constraint<Long> {
            isNotDivisibleBy { 3 }
        }.allMatch(
            1,
            2,
            4,
            5,
            7,
            8
        ).allFail(
            0,
            3,
            6,
            9
        )
    }

    @Test
    fun isEven_meansThisLongIsEven() {
        constraint<Long> {
            isEven()
        }.allMatch(
            0,
            2,
            4,
            6,
            8,
            126
        ).allFail(
            1,
            3,
            5,
            7,
            125
        )
    }

    @Test
    fun isOdd_meansThisLongIsOdd() {
        constraint<Long> {
            isOdd()
        }.allMatch(
            1,
            3,
            5,
            7,
            125
        ).allFail(
            0,
            2,
            4,
            6,
            8,
            126
        )
    }

    @Test
    fun isPositive_meansThisLongIsPositive() {

        constraint<Long> {
            isPositive(false)
        }.allMatch(
            1,
            2,
            3,
            4,
            125
        ).allFail(
            0,
            -1,
            -2,
            -3,
            -4,
            -125
        )

        constraint<Long> {
            isPositive(true)
        }.allMatch(
            0,
            1,
            2,
            3,
            4,
            125
        ).allFail(
            -1,
            -2,
            -3,
            -4,
            -125
        )
    }

    @Test
    fun isNegative_meansThisLongIsNegative() {

        constraint<Long> {
            isNegative(false)
        }.allMatch(
            -1,
            -2,
            -3,
            -4,
            -125
        ).allFail(
            0,
            1,
            2,
            3,
            4,
            125
        )

        constraint<Long> {
            isNegative(true)
        }.allMatch(
            0,
            -1,
            -2,
            -3,
            -4,
            -125
        ).allFail(
            1,
            2,
            3,
            4,
            125
        )
    }

    @Test
    fun isZero_meansThisLongEqualsZero() {
        constraint<Long> {
            isZero()
        }.allMatch(
            0
        ).allFail(
            -125,
            -3,
            -4,
            -2,
            -1,
            1,
            2,
            3,
            4,
            125
        )
    }

    @Test
    fun isNotZero_meansThisLongDoesNotEqualZero() {
        constraint<Long> {
            isNotZero()
        }.allMatch(
            -125,
            -3,
            -4,
            -2,
            -1,
            1,
            2,
            3,
            4,
            125
        ).allFail(
            0
        )
    }

    @Test
    fun isPrime_meansThisLongIsPrime() {
        constraint<Long> {
            isPrime()
        }.allMatch(
            2,
            3,
            5,
            7,
            11,
            67,
            71,
            73,
            79,
            83,
            89,
            97
        ).allFail(
            -5,
            -4,
            -3,
            -2,
            -1,
            0,
            1,
            4,
            6,
            8,
            9,
            56,
            57,
            58,
            60,
            62,
            63
        )
    }

    @Test
    fun isNotPrime_meansThisLongIsNotPrime() {
        constraint<Long> {
            isNotPrime()
        }.allMatch(
            -5,
            -4,
            -3,
            -2,
            -1,
            0,
            1,
            4,
            6,
            8,
            9,
            56,
            57,
            58,
            60,
            62,
            63
        ).allFail(
            2,
            3,
            5,
            7,
            11,
            67,
            71,
            73,
            79,
            83,
            89,
            97
        )
    }
}
