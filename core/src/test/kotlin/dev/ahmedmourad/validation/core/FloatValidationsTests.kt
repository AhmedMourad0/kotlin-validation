package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.utils.allFail
import dev.ahmedmourad.validation.core.utils.allMatch
import dev.ahmedmourad.validation.core.utils.validator
import dev.ahmedmourad.validation.core.validations.*
import org.junit.Test

class FloatValidationsTests {

    @Test
    fun isDivisibleBy_meansThisFloatIsDivisibleByTheGivenFloat() {
        validator<Float> {
            isDivisibleBy(3f)
        }.allMatch(
            0f,
            3f,
            6f,
            9f
        ).allFail(
            Float.NaN,
            1f,
            2f,
            4f,
            5f,
            7f,
            8f
        )
    }

    @Test
    fun isDivisibleByL_meansThisFloatIsDivisibleByTheGivenFloat() {
        validator<Float> {
            isDivisibleBy { 3f }
        }.allMatch(
            0f,
            3f,
            6f,
            9f
        ).allFail(
            Float.NaN,
            1f,
            2f,
            4f,
            5f,
            7f,
            8f
        )
    }

    @Test
    fun isNotDivisibleBy_meansThisFloatIsNotDivisibleByTheGivenFloat() {
        validator<Float> {
            isNotDivisibleBy(3f)
        }.allMatch(
            1f,
            2f,
            4f,
            5f,
            7f,
            8f
        ).allFail(
            0f,
            3f,
            6f,
            9f
        )
    }

    @Test
    fun isNotDivisibleByL_meansThisFloatIsNotDivisibleByTheGivenFloat() {
        validator<Float> {
            isNotDivisibleBy { 3f }
        }.allMatch(
            1f,
            2f,
            4f,
            5f,
            7f,
            8f
        ).allFail(
            0f,
            3f,
            6f,
            9f
        )
    }

    @Test
    fun isPositive_meansThisFloatIsPositive() {

        validator<Float> {
            isPositive(false)
        }.allMatch(
            1f,
            2f,
            3f,
            4f,
            125f
        ).allFail(
            Float.NaN,
            0f,
            -1f,
            -2f,
            -3f,
            -4f,
            -125f
        )

        validator<Float> {
            isPositive(true)
        }.allMatch(
            0f,
            1f,
            2f,
            3f,
            4f,
            125f
        ).allFail(
            Float.NaN,
            -1f,
            -2f,
            -3f,
            -4f,
            -125f
        )
    }

    @Test
    fun isNegative_meansThisFloatIsNegative() {

        validator<Float> {
            isNegative(false)
        }.allMatch(
            -1f,
            -2f,
            -3f,
            -4f,
            -125f
        ).allFail(
            Float.NaN,
            0f,
            1f,
            2f,
            3f,
            4f,
            125f
        )

        validator<Float> {
            isNegative(true)
        }.allMatch(
            0f,
            -1f,
            -2f,
            -3f,
            -4f,
            -125f
        ).allFail(
            Float.NaN,
            1f,
            2f,
            3f,
            4f,
            125f
        )
    }

    @Test
    fun isZero_meansThisFloatEqualsZero() {
        validator<Float> {
            isZero()
        }.allMatch(
            0f
        ).allFail(
            Float.NaN,
            -125f,
            -3f,
            -4f,
            -2f,
            -1f,
            1f,
            2f,
            3f,
            4f,
            125f
        )
    }

    @Test
    fun isNotZero_meansThisFloatDoesNotEqualZero() {
        validator<Float> {
            isNotZero()
        }.allMatch(
            -125f,
            -3f,
            -4f,
            -2f,
            -1f,
            1f,
            2f,
            3f,
            4f,
            125f
        ).allFail(
            0f
        )
    }

    @Test
    fun isNaN_meansThisFloatIsNaN() {
        validator<Float> {
            isNaN()
        }.allMatch(
            Float.NaN
        ).allFail(
            -1f,
            0f,
            1f,
            2f,
            Float.MIN_VALUE,
            Float.MAX_VALUE,
            Float.NEGATIVE_INFINITY,
            Float.POSITIVE_INFINITY
        )
    }

    @Test
    fun isNotNaN_meansThisFloatIsNaN() {
        validator<Float> {
            isNotNaN()
        }.allMatch(
            -1f,
            0f,
            1f,
            2f,
            Float.MIN_VALUE,
            Float.MAX_VALUE,
            Float.NEGATIVE_INFINITY,
            Float.POSITIVE_INFINITY
        ).allFail(
            Float.NaN
        )
    }

    @Test
    fun isInfinite_meansThisFloatIsInfinite() {
        validator<Float> {
            isInfinite()
        }.allMatch(
            Float.NEGATIVE_INFINITY,
            Float.POSITIVE_INFINITY
        ).allFail(
            Float.NaN,
            -1f,
            0f,
            1f,
            2f,
            Float.MIN_VALUE,
            Float.MAX_VALUE
        )
    }

    @Test
    fun isFinite_meansThisFloatIsFinite() {
        validator<Float> {
            isFinite()
        }.allMatch(
            -1f,
            0f,
            1f,
            2f,
            Float.MIN_VALUE,
            Float.MAX_VALUE
        ).allFail(
            Float.NaN,
            Float.NEGATIVE_INFINITY,
            Float.POSITIVE_INFINITY
        )
    }
}
