package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.utils.allFail
import dev.ahmedmourad.validation.core.utils.allMatch
import dev.ahmedmourad.validation.core.utils.constraint
import dev.ahmedmourad.validation.core.validations.*
import kotlin.test.Test

class ByteValidationsTests {

    @Test
    fun isDivisibleBy_meansThisByteIsDivisibleByTheGivenByte() {
        constraint<Byte> {
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
    fun isDivisibleByL_meansThisByteIsDivisibleByTheGivenByte() {
        constraint<Byte> {
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
    fun isNotDivisibleBy_meansThisByteIsNotDivisibleByTheGivenByte() {
        constraint<Byte> {
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
    fun isNotDivisibleByL_meansThisByteIsNotDivisibleByTheGivenByte() {
        constraint<Byte> {
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
    fun isEven_meansThisByteIsEven() {
        constraint<Byte> {
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
    fun isOdd_meansThisByteIsOdd() {
        constraint<Byte> {
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
    fun isPositive_meansThisByteIsPositive() {

        constraint<Byte> {
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

        constraint<Byte> {
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
    fun isNegative_meansThisByteIsNegative() {

        constraint<Byte> {
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

        constraint<Byte> {
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
    fun isZero_meansThisByteEqualsZero() {
        constraint<Byte> {
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
    fun isNotZero_meansThisByteDoesNotEqualZero() {
        constraint<Byte> {
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
    fun isPrime_meansThisByteIsPrime() {
        constraint<Byte> {
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
    fun isNotPrime_meansThisByteIsNotPrime() {
        constraint<Byte> {
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
