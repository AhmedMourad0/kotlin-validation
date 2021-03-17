package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.validations.*
import org.junit.Test

class ComparableValidationsTests {

    @Test
    fun min_meansThisComparableIsLargerThanOrEqualToTheGivenComparable() {
        validator<Int> {
            min(3)
        }.allMatch(
            3,
            4,
            5,
            6
        ).allFail(
            -1,
            0,
            1,
            2
        )
    }

    @Test
    fun minL_meansThisComparableIsLargerThanOrEqualToTheGivenComparable() {
        validator<Int> {
            min<Int> { 3 }
        }.allMatch(
            3,
            4,
            5,
            6
        ).allFail(
            -1,
            0,
            1,
            2
        )
    }

    @Test
    fun max_meansThisComparableIsLessThanOrEqualToTheGivenComparable() {
        validator<Int> {
            max(3)
        }.allMatch(
            -1,
            0,
            1,
            2,
            3
        ).allFail(
            4,
            5,
            6,
            7
        )
    }

    @Test
    fun maxL_meansThisComparableIsLessThanOrEqualToTheGivenComparable() {
        validator<Int> {
            max<Int> { 3 }
        }.allMatch(
            -1,
            0,
            1,
            2,
            3
        ).allFail(
            4,
            5,
            6,
            7
        )
    }

    @Test
    fun lessThan_meansThisComparableIsLessThanTheGivenComparable() {
        validator<Int> {
            lessThan(3)
        }.allMatch(
            -1,
            0,
            1,
            2
        ).allFail(
            3,
            4,
            5,
            6
        )
    }

    @Test
    fun lessThanL_meansThisComparableIsLessThanTheGivenComparable() {
        validator<Int> {
            lessThan<Int> { 3 }
        }.allMatch(
            -1,
            0,
            1,
            2
        ).allFail(
            3,
            4,
            5,
            6
        )
    }

    @Test
    fun largerThan_meansThisComparableIsLargerThanTheGivenComparable() {
        validator<Int> {
            largerThan(3)
        }.allMatch(
            4,
            5,
            6,
            7
        ).allFail(
            -1,
            0,
            1,
            2,
            3
        )
    }

    @Test
    fun largerThanL_meansThisComparableIsLargerThanTheGivenComparable() {
        validator<Int> {
            largerThan<Int> { 3 }
        }.allMatch(
            4,
            5,
            6,
            7
        ).allFail(
            -1,
            0,
            1,
            2,
            3
        )
    }

    @Test
    fun inRange_meansThisComparableIsInTheGivenRange() {
        validator<Int> {
            inRange(3, 5)
        }.allMatch(
            3,
            4,
            5
        ).allFail(
            -1,
            0,
            1,
            2,
            6,
            7
        )
    }

    @Test
    fun inRangeR_meansThisComparableIsInTheGivenRange() {
        validator<Int> {
            inRange(3..5)
        }.allMatch(
            3,
            4,
            5
        ).allFail(
            -1,
            0,
            1,
            2,
            6,
            7
        )
    }

    @Test
    fun inRangeRL_meansThisComparableIsInTheGivenRange() {
        validator<Int> {
            inRange { 3..5 }
        }.allMatch(
            3,
            4,
            5
        ).allFail(
            -1,
            0,
            1,
            2,
            6,
            7
        )
    }

    @Test
    fun notInRange_meansThisComparableIsNotInTheGivenRange() {
        validator<Int> {
            notInRange(3, 5)
        }.allMatch(
            -1,
            0,
            1,
            2,
            6,
            7
        ).allFail(
            3,
            4,
            5
        )
    }

    @Test
    fun notInRangeR_meansThisComparableIsNotInTheGivenRange() {
        validator<Int> {
            notInRange(3..5)
        }.allMatch(
            -1,
            0,
            1,
            2,
            6,
            7
        ).allFail(
            3,
            4,
            5
        )
    }

    @Test
    fun notInRangeRL_meansThisComparableIsNotInTheGivenRange() {
        validator<Int> {
            notInRange { 3..5 }
        }.allMatch(
            -1,
            0,
            1,
            2,
            6,
            7
        ).allFail(
            3,
            4,
            5
        )
    }
}
