package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.validations.*
import org.junit.Test

class DoubleValidationsTests {

    @Test
    fun isDivisibleBy_meansThisDoubleIsDivisibleByTheGivenDouble() {
        validator<Double> {
            isDivisibleBy(3.0)
        }.allMatch(
            0.0,
            3.0,
            6.0,
            9.0
        ).allFail(
            Double.NaN,
            1.0,
            2.0,
            4.0,
            5.0,
            7.0,
            8.0
        )
    }

    @Test
    fun isDivisibleByL_meansThisDoubleIsDivisibleByTheGivenDouble() {
        validator<Double> {
            isDivisibleBy { 3.0 }
        }.allMatch(
            0.0,
            3.0,
            6.0,
            9.0
        ).allFail(
            Double.NaN,
            1.0,
            2.0,
            4.0,
            5.0,
            7.0,
            8.0
        )
    }

    @Test
    fun isNotDivisibleBy_meansThisDoubleIsNotDivisibleByTheGivenDouble() {
        validator<Double> {
            isNotDivisibleBy(3.0)
        }.allMatch(
            1.0,
            2.0,
            4.0,
            5.0,
            7.0,
            8.0
        ).allFail(
            0.0,
            3.0,
            6.0,
            9.0
        )
    }

    @Test
    fun isNotDivisibleByL_meansThisDoubleIsNotDivisibleByTheGivenDouble() {
        validator<Double> {
            isNotDivisibleBy { 3.0 }
        }.allMatch(
            1.0,
            2.0,
            4.0,
            5.0,
            7.0,
            8.0
        ).allFail(
            0.0,
            3.0,
            6.0,
            9.0
        )
    }

    @Test
    fun isPositive_meansThisDoubleIsPositive() {

        validator<Double> {
            isPositive(false)
        }.allMatch(
            1.0,
            2.0,
            3.0,
            4.0,
            125.0
        ).allFail(
            Double.NaN,
            0.0,
            -1.0,
            -2.0,
            -3.0,
            -4.0,
            -125.0
        )

        validator<Double> {
            isPositive(true)
        }.allMatch(
            0.0,
            1.0,
            2.0,
            3.0,
            4.0,
            125.0
        ).allFail(
            Double.NaN,
            -1.0,
            -2.0,
            -3.0,
            -4.0,
            -125.0
        )
    }

    @Test
    fun isNegative_meansThisDoubleIsNegative() {

        validator<Double> {
            isNegative(false)
        }.allMatch(
            -1.0,
            -2.0,
            -3.0,
            -4.0,
            -125.0
        ).allFail(
            Double.NaN,
            0.0,
            1.0,
            2.0,
            3.0,
            4.0,
            125.0
        )

        validator<Double> {
            isNegative(true)
        }.allMatch(
            0.0,
            -1.0,
            -2.0,
            -3.0,
            -4.0,
            -125.0
        ).allFail(
            Double.NaN,
            1.0,
            2.0,
            3.0,
            4.0,
            125.0
        )
    }

    @Test
    fun isZero_meansThisDoubleEqualsZero() {
        validator<Double> {
            isZero()
        }.allMatch(
            0.0
        ).allFail(
            Double.NaN,
            -125.0,
            -3.0,
            -4.0,
            -2.0,
            -1.0,
            1.0,
            2.0,
            3.0,
            4.0,
            125.0
        )
    }

    @Test
    fun isNotZero_meansThisDoubleDoesNotEqualZero() {
        validator<Double> {
            isNotZero()
        }.allMatch(
            -125.0,
            -3.0,
            -4.0,
            -2.0,
            -1.0,
            1.0,
            2.0,
            3.0,
            4.0,
            125.0
        ).allFail(
            0.0
        )
    }

    @Test
    fun isNaN_meansThisDoubleIsNaN() {
        validator<Double> {
            isNaN()
        }.allMatch(
            Double.NaN
        ).allFail(
            -1.0,
            0.0,
            1.0,
            2.0,
            Double.MIN_VALUE,
            Double.MAX_VALUE,
            Double.NEGATIVE_INFINITY,
            Double.POSITIVE_INFINITY
        )
    }

    @Test
    fun isNotNaN_meansThisDoubleIsNaN() {
        validator<Double> {
            isNotNaN()
        }.allMatch(
            -1.0,
            0.0,
            1.0,
            2.0,
            Double.MIN_VALUE,
            Double.MAX_VALUE,
            Double.NEGATIVE_INFINITY,
            Double.POSITIVE_INFINITY
        ).allFail(
            Double.NaN
        )
    }

    @Test
    fun isInfinite_meansThisDoubleIsInfinite() {
        validator<Double> {
            isInfinite()
        }.allMatch(
            Double.NEGATIVE_INFINITY,
            Double.POSITIVE_INFINITY
        ).allFail(
            Double.NaN,
            -1.0,
            0.0,
            1.0,
            2.0,
            Double.MIN_VALUE,
            Double.MAX_VALUE
        )
    }

    @Test
    fun isFinite_meansThisDoubleIsFinite() {
        validator<Double> {
            isFinite()
        }.allMatch(
            -1.0,
            0.0,
            1.0,
            2.0,
            Double.MIN_VALUE,
            Double.MAX_VALUE
        ).allFail(
            Double.NaN,
            Double.NEGATIVE_INFINITY,
            Double.POSITIVE_INFINITY
        )
    }
}
