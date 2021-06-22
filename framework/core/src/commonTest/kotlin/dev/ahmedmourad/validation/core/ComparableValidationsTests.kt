package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.utils.allFail
import dev.ahmedmourad.validation.core.utils.allMatch
import dev.ahmedmourad.validation.core.utils.constraint
import dev.ahmedmourad.validation.core.validations.*
import kotlin.test.Test

class ComparableValidationsTests {

    @Test
    fun min_meansThisComparableIsLargerThanOrEqualToTheGivenComparable() {
        constraint<Int> {
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
        constraint<Int> {
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
        constraint<Int> {
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
        constraint<Int> {
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
        constraint<Int> {
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
        constraint<Int> {
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
        constraint<Int> {
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
        constraint<Int> {
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
        constraint<Int> {
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
        constraint<Int> {
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
        constraint<Int> {
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
        constraint<Int> {
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
        constraint<Int> {
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
        constraint<Int> {
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
