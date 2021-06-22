package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.utils.allFail
import dev.ahmedmourad.validation.core.utils.allMatch
import dev.ahmedmourad.validation.core.utils.constraint
import dev.ahmedmourad.validation.core.validations.*
import kotlin.test.Test

class DoubleValidationsTests {

    @Test
    fun isDivisibleBy_meansThisDoubleIsDivisibleByTheGivenDouble() {
        constraint<Double> {
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
        constraint<Double> {
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
        constraint<Double> {
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
        constraint<Double> {
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

        constraint<Double> {
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

        constraint<Double> {
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

        constraint<Double> {
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

        constraint<Double> {
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
        constraint<Double> {
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
        constraint<Double> {
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
        constraint<Double> {
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
        constraint<Double> {
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
        constraint<Double> {
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
        constraint<Double> {
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
