package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.utils.allFail
import dev.ahmedmourad.validation.core.utils.allMatch
import dev.ahmedmourad.validation.core.utils.validator
import dev.ahmedmourad.validation.core.validations.*
import kotlin.test.Test

class ShortValidationsTests {

    @Test
    fun isDivisibleBy_meansThisShortIsDivisibleByTheGivenShort() {
        validator<Short> {
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
    fun isDivisibleByL_meansThisShortIsDivisibleByTheGivenShort() {
        validator<Short> {
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
    fun isNotDivisibleBy_meansThisShortIsNotDivisibleByTheGivenShort() {
        validator<Short> {
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
    fun isNotDivisibleByL_meansThisShortIsNotDivisibleByTheGivenShort() {
        validator<Short> {
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
    fun isEven_meansThisShortIsEven() {
        validator<Short> {
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
    fun isOdd_meansThisShortIsOdd() {
        validator<Short> {
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
    fun isPositive_meansThisShortIsPositive() {

        validator<Short> {
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

        validator<Short> {
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
    fun isNegative_meansThisShortIsNegative() {

        validator<Short> {
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

        validator<Short> {
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
    fun isZero_meansThisShortEqualsZero() {
        validator<Short> {
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
    fun isNotZero_meansThisShortDoesNotEqualZero() {
        validator<Short> {
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
    fun isPrime_meansThisShortIsPrime() {
        validator<Short> {
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
    fun isNotPrime_meansThisShortIsNotPrime() {
        validator<Short> {
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
