package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.utils.allFail
import dev.ahmedmourad.validation.core.utils.allMatch
import dev.ahmedmourad.validation.core.utils.constraint
import dev.ahmedmourad.validation.core.validations.*
import kotlin.test.Test

class ArrayValidationsTests {

    @Test
    fun forAll_meansTheGivenValidationsMatchAllTheElementsOfTheArray() {
        constraint<Array<Boolean>> {
            forAll {
                isTrue()
            }
        }.allMatch(
            emptyArray(),
            arrayOf(true),
            arrayOf(true, true),
            arrayOf(true, true, true)
        ).allFail(
            arrayOf(false),
            arrayOf(false, false),
            arrayOf(false, true),
            arrayOf(false, false, false),
            arrayOf(true, false, false),
            arrayOf(false, true, false),
            arrayOf(false, false, true),
            arrayOf(false, true, true)
        )
    }

    @Test
    fun forAny_meansTheGivenValidationsMatchAtLeastOneOfTheElementsOfTheArray() {
        constraint<Array<Boolean>> {
            forAny {
                isTrue()
            }
        }.allMatch(
            arrayOf(true),
            arrayOf(true, true),
            arrayOf(true, false),
            arrayOf(false, true),
            arrayOf(true, true, true),
            arrayOf(true, false, false),
            arrayOf(false, true, false),
            arrayOf(false, false, true),
            arrayOf(false, true, true)
        ).allFail(
            emptyArray(),
            arrayOf(false),
            arrayOf(false, false),
            arrayOf(false, false, false)
        )
    }

    @Test
    fun forNone_meansTheGivenValidationsMatchNoneOfTheElementsOfTheArray() {
        constraint<Array<Boolean>> {
            forNone {
                isTrue()
            }
        }.allMatch(
            emptyArray(),
            arrayOf(false),
            arrayOf(false, false),
            arrayOf(false, false, false)
        ).allFail(
            arrayOf(true),
            arrayOf(true, true),
            arrayOf(true, false),
            arrayOf(true, true, true),
            arrayOf(false, true, true),
            arrayOf(true, false, true),
            arrayOf(true, true, false),
            arrayOf(true, false, false)
        )
    }

    @Test
    fun isEmpty_meansTheArrayIsEmpty() {
        constraint<Array<Unit>> {
            isEmpty()
        }.allMatch(
            emptyArray()
        ).allFail(
            arrayOf(Unit),
            arrayOf(Unit, Unit),
            arrayOf(Unit, Unit, Unit)
        )
    }

    @Test
    fun isNotEmpty_meansTheArrayIsNotEmpty() {
        constraint<Array<Unit>> {
            isNotEmpty()
        }.allMatch(
            arrayOf(Unit),
            arrayOf(Unit, Unit),
            arrayOf(Unit, Unit, Unit)
        ).allFail(
            emptyArray()
        )
    }

    @Test
    fun isDistinct_meansAllTheElementsOfTheArrayAreDistinct() {
        constraint<Array<Int>> {
            isDistinct()
        }.allMatch(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3)
        ).allFail(
            arrayOf(1, 1),
            arrayOf(1, 2, 1),
            arrayOf(2, 2, 1),
            arrayOf(1, 2, 2),
            arrayOf(1, 2, 2, 3)
        )
    }

    @Test
    fun minSize_meansTheArrayMustHaveAtLeastTheGivenNumberOfElements() {
        constraint<Array<Int>> {
            minSize(4)
        }.allMatch(
            arrayOf(1, 2, 3, 4),
            arrayOf(1, 2, 3, 4, 5),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(1, 2, 3, 4, 5, 6, 7),
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3)
        )
    }

    @Test
    fun minSizeL_meansTheArrayMustHaveAtLeastTheGivenNumberOfElements() {
        constraint<Array<Int>> {
            minSize { 4 }
        }.allMatch(
            arrayOf(1, 2, 3, 4),
            arrayOf(1, 2, 3, 4, 5),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(1, 2, 3, 4, 5, 6, 7),
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3)
        )
    }

    @Test
    fun maxSize_meansTheArrayMustHaveAtMostTheGivenNumberOfElements() {
        constraint<Array<Int>> {
            maxSize(4)
        }.allMatch(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3),
            arrayOf(1, 2, 3, 4)
        ).allFail(
            arrayOf(1, 2, 3, 4, 5),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(1, 2, 3, 4, 5, 6, 7),
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8)
        )
    }

    @Test
    fun maxSizeL_meansTheArrayMustHaveAtMostTheGivenNumberOfElements() {
        constraint<Array<Int>> {
            maxSize { 4 }
        }.allMatch(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3),
            arrayOf(1, 2, 3, 4)
        ).allFail(
            arrayOf(1, 2, 3, 4, 5),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(1, 2, 3, 4, 5, 6, 7),
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8)
        )
    }

    @Test
    fun sizeLessThan_meansTheArrayMustHaveLessThanTheGivenNumberOfElements() {
        constraint<Array<Int>> {
            sizeLessThan(4)
        }.allMatch(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3)
        ).allFail(
            arrayOf(1, 2, 3, 4),
            arrayOf(1, 2, 3, 4, 5),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(1, 2, 3, 4, 5, 6, 7),
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8)
        )
    }

    @Test
    fun sizeLessThanL_meansTheArrayMustHaveLessThanTheGivenNumberOfElements() {
        constraint<Array<Int>> {
            sizeLessThan { 4 }
        }.allMatch(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3)
        ).allFail(
            arrayOf(1, 2, 3, 4),
            arrayOf(1, 2, 3, 4, 5),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(1, 2, 3, 4, 5, 6, 7),
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8)
        )
    }

    @Test
    fun sizeLargerThan_meansTheArrayMustHaveMoreThanTheGivenNumberOfElements() {
        constraint<Array<Int>> {
            sizeLargerThan(4)
        }.allMatch(
            arrayOf(1, 2, 3, 4, 5),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(1, 2, 3, 4, 5, 6, 7),
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3),
            arrayOf(1, 2, 3, 4)
        )
    }

    @Test
    fun sizeLargerThanL_meansTheArrayMustHaveMoreThanTheGivenNumberOfElements() {
        constraint<Array<Int>> {
            sizeLargerThan { 4 }
        }.allMatch(
            arrayOf(1, 2, 3, 4, 5),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(1, 2, 3, 4, 5, 6, 7),
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3),
            arrayOf(1, 2, 3, 4)
        )
    }

    @Test
    fun sizeIn_meansTheArrayMustHaveTheGivenRangeOfElements() {
        constraint<Array<Int>> {
            sizeIn(3, 5)
        }.allMatch(
            arrayOf(1, 2, 3),
            arrayOf(1, 2, 3, 4),
            arrayOf(1, 2, 3, 4, 5)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(1, 2, 3, 4, 5, 6, 7),
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8)
        )
    }

    @Test
    fun sizeInR_meansTheArrayMustHaveTheGivenRangeOfElements() {
        constraint<Array<Int>> {
            sizeIn(3..5)
        }.allMatch(
            arrayOf(1, 2, 3),
            arrayOf(1, 2, 3, 4),
            arrayOf(1, 2, 3, 4, 5)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(1, 2, 3, 4, 5, 6, 7),
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8)
        )
    }

    @Test
    fun sizeInRL_meansTheArrayMustHaveTheGivenRangeOfElements() {
        constraint<Array<Int>> {
            sizeIn { 3..5 }
        }.allMatch(
            arrayOf(1, 2, 3),
            arrayOf(1, 2, 3, 4),
            arrayOf(1, 2, 3, 4, 5)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(1, 2, 3, 4, 5, 6, 7),
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8)
        )
    }

    @Test
    fun sizeNotIn_meansTheArrayMustNotHaveTheGivenRangeOfElements() {
        constraint<Array<Int>> {
            sizeNotIn(3, 5)
        }.allMatch(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(1, 2, 3, 4, 5, 6, 7),
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8)
        ).allFail(
            arrayOf(1, 2, 3),
            arrayOf(1, 2, 3, 4),
            arrayOf(1, 2, 3, 4, 5)
        )
    }

    @Test
    fun sizeNotInR_meansTheArrayMustNotHaveTheGivenRangeOfElements() {
        constraint<Array<Int>> {
            sizeNotIn(3..5)
        }.allMatch(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(1, 2, 3, 4, 5, 6, 7),
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8)
        ).allFail(
            arrayOf(1, 2, 3),
            arrayOf(1, 2, 3, 4),
            arrayOf(1, 2, 3, 4, 5)
        )
    }

    @Test
    fun sizeNotInRL_meansTheArrayMustNotHaveTheGivenRangeOfElements() {
        constraint<Array<Int>> {
            sizeNotIn { 3..5 }
        }.allMatch(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(1, 2, 3, 4, 5, 6, 7),
            arrayOf(1, 2, 3, 4, 5, 6, 7, 8)
        ).allFail(
            arrayOf(1, 2, 3),
            arrayOf(1, 2, 3, 4),
            arrayOf(1, 2, 3, 4, 5)
        )
    }

    @Test
    fun sizeEqualTo_meansTheArrayMustHaveTheGivenNumberOfElements() {
        constraint<Array<Int>> {
            sizeEqualTo(3)
        }.allMatch(
            arrayOf(1, 2, 3)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4),
            arrayOf(1, 2, 3, 4, 5)
        )
    }

    @Test
    fun sizeEqualToL_meansTheArrayMustHaveTheGivenNumberOfElements() {
        constraint<Array<Int>> {
            sizeEqualTo { 3 }
        }.allMatch(
            arrayOf(1, 2, 3)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4),
            arrayOf(1, 2, 3, 4, 5)
        )
    }

    @Test
    fun sizeNotEqualTo_meansTheArrayMustNotHaveTheGivenNumberOfElements() {
        constraint<Array<Int>> {
            sizeNotEqualTo(3)
        }.allMatch(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4),
            arrayOf(1, 2, 3, 4, 5)
        ).allFail(
            arrayOf(1, 2, 3)
        )
    }

    @Test
    fun sizeNotEqualToL_meansTheArrayMustNotHaveTheGivenNumberOfElements() {
        constraint<Array<Int>> {
            sizeNotEqualTo { 3 }
        }.allMatch(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4),
            arrayOf(1, 2, 3, 4, 5)
        ).allFail(
            arrayOf(1, 2, 3)
        )
    }

    @Test
    fun contains_meansTheArrayMustContainTheGivenElement() {
        constraint<Array<Int>> {
            contains(3)
        }.allMatch(
            arrayOf(3),
            arrayOf(1, 3),
            arrayOf(3, 1, 2, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 3, 4, 5)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 4),
            arrayOf(1, 2, 4, 5)
        )
    }

    @Test
    fun containsL_meansTheArrayMustContainTheGivenElement() {
        constraint<Array<Int>> {
            contains<Int> { 3 }
        }.allMatch(
            arrayOf(3),
            arrayOf(1, 3),
            arrayOf(3, 1, 2, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 3, 4, 5)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 4),
            arrayOf(1, 2, 4, 5)
        )
    }

    @Test
    fun doesNotContain_meansTheArrayMustContainTheGivenElement() {
        constraint<Array<Int>> {
            doesNotContain(3)
        }.allMatch(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 4),
            arrayOf(1, 2, 4, 5)
        ).allFail(
            arrayOf(3),
            arrayOf(1, 3),
            arrayOf(3, 1, 2, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 3, 4, 5)
        )
    }

    @Test
    fun doesNotContainL_meansTheArrayMustContainTheGivenElement() {
        constraint<Array<Int>> {
            doesNotContain<Int> { 3 }
        }.allMatch(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 4),
            arrayOf(1, 2, 4, 5)
        ).allFail(
            arrayOf(3),
            arrayOf(1, 3),
            arrayOf(3, 1, 2, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 3, 4, 5)
        )
    }

    @Test
    fun containsAt_meansTheArrayMustContainTheGivenElementAtTheGivenPosition() {
        constraint<Array<Int>> {
            containsAt(1, 8)
        }.allMatch(
            arrayOf(8, 8),
            arrayOf(1, 8),
            arrayOf(3, 8, 2, 4),
            arrayOf(3, 8, 2, 4),
            arrayOf(1, 8, 3, 4, 5)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(8),
            arrayOf(1, 2),
            arrayOf(1, 2, 8),
            arrayOf(8, 2, 8),
            arrayOf(8, 2, 8, 8)
        )
    }

    @Test
    fun containsAtL_meansTheArrayMustContainTheGivenElementAtTheGivenPosition() {
        constraint<Array<Int>> {
            containsAt<Int>(1) { 8 }
        }.allMatch(
            arrayOf(8, 8),
            arrayOf(1, 8),
            arrayOf(3, 8, 2, 4),
            arrayOf(3, 8, 2, 4),
            arrayOf(1, 8, 3, 4, 5)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(8),
            arrayOf(1, 2),
            arrayOf(1, 2, 8),
            arrayOf(8, 2, 8),
            arrayOf(8, 2, 8, 8)
        )
    }

    @Test
    fun startsWith_meansTheArrayMustStartWithTheGivenElement() {
        constraint<Array<Int>> {
            startsWith(8)
        }.allMatch(
            arrayOf(8),
            arrayOf(8, 1),
            arrayOf(8, 8, 2, 4),
            arrayOf(8, 8, 2, 4),
            arrayOf(8, 8, 3, 4, 5)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 8),
            arrayOf(1, 8, 8),
            arrayOf(1, 8, 3),
            arrayOf(1, 8, 3, 8)
        )
    }

    @Test
    fun startsWithL_meansTheArrayMustStartWithTheGivenElement() {
        constraint<Array<Int>> {
            startsWith<Int> { 8 }
        }.allMatch(
            arrayOf(8),
            arrayOf(8, 1),
            arrayOf(8, 8, 2, 4),
            arrayOf(8, 8, 2, 4),
            arrayOf(8, 8, 3, 4, 5)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 8),
            arrayOf(1, 8, 8),
            arrayOf(1, 8, 3),
            arrayOf(1, 8, 3, 8)
        )
    }

    @Test
    fun endsWith_meansTheArrayMustEndWithTheGivenElement() {
        constraint<Array<Int>> {
            endsWith(8)
        }.allMatch(
            arrayOf(8),
            arrayOf(8, 8),
            arrayOf(2, 8, 2, 8),
            arrayOf(2, 8, 2, 8),
            arrayOf(2, 8, 3, 4, 8)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(8, 7),
            arrayOf(5, 8, 5),
            arrayOf(1, 8, 3),
            arrayOf(8, 8, 8, 7)
        )
    }

    @Test
    fun endsWithL_meansTheArrayMustEndWithTheGivenElement() {
        constraint<Array<Int>> {
            endsWith<Int> { 8 }
        }.allMatch(
            arrayOf(8),
            arrayOf(8, 8),
            arrayOf(2, 8, 2, 8),
            arrayOf(2, 8, 2, 8),
            arrayOf(2, 8, 3, 4, 8)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(8, 7),
            arrayOf(5, 8, 5),
            arrayOf(1, 8, 3),
            arrayOf(8, 8, 8, 7)
        )
    }

    @Test
    fun containsAll_meansTheArrayMustContainAllTheGivenElements() {
        constraint<Array<Int>> {
            containsAll(2, 3)
        }.allMatch(
            arrayOf(2, 3),
            arrayOf(3, 2),
            arrayOf(3, 1, 2, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 3, 4, 5)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 4),
            arrayOf(1, 2, 4, 5)
        )
    }

    @Test
    fun containsAllL_meansTheArrayMustContainAllTheGivenElements() {
        constraint<Array<Int>> {
            containsAll { arrayOf(2, 3) }
        }.allMatch(
            arrayOf(2, 3),
            arrayOf(3, 2),
            arrayOf(3, 1, 2, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 3, 4, 5)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 4),
            arrayOf(1, 2, 4, 5)
        )
    }

    @Test
    fun isPartOf_meansAllTheElementsOfTheArrayMustExistInTheGivenElements() {
        constraint<Array<Int>> {
            isPartOf(1, 2, 3, 4, 5)
        }.allMatch(
            emptyArray(),
            arrayOf(3),
            arrayOf(2, 3),
            arrayOf(3, 2),
            arrayOf(3, 1, 2, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 4, 3, 5)
        ).allFail(
            arrayOf(6),
            arrayOf(6, 1),
            arrayOf(7, 3, 9),
            arrayOf(4, 9, 1, 2)
        )
    }

    @Test
    fun isPartOfL_meansAllTheElementsOfTheArrayMustExistInTheGivenElements() {
        constraint<Array<Int>> {
            isPartOf { arrayOf(1, 2, 3, 4, 5) }
        }.allMatch(
            emptyArray(),
            arrayOf(3),
            arrayOf(2, 3),
            arrayOf(3, 2),
            arrayOf(3, 1, 2, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 4, 3, 5)
        ).allFail(
            arrayOf(6),
            arrayOf(6, 1),
            arrayOf(7, 3, 9),
            arrayOf(4, 9, 1, 2)
        )
    }

    @Test
    fun allTrue_meansAllTheBooleansInTheArrayAreTrue() {
        constraint<Array<Boolean>> {
            allTrue()
        }.allMatch(
            emptyArray(),
            arrayOf(true),
            arrayOf(true, true),
            arrayOf(true, true, true)
        ).allFail(
            arrayOf(false),
            arrayOf(false, false),
            arrayOf(false, true),
            arrayOf(false, false, false),
            arrayOf(true, false, false),
            arrayOf(false, true, false),
            arrayOf(false, false, true),
            arrayOf(false, true, true)
        )
    }

    @Test
    fun anyTrue_meansAtLeastOneOfTheBooleansInTheArrayIsTrue() {
        constraint<Array<Boolean>> {
            anyTrue()
        }.allMatch(
            arrayOf(true),
            arrayOf(true, true),
            arrayOf(true, false),
            arrayOf(false, true),
            arrayOf(true, true, true),
            arrayOf(true, false, false),
            arrayOf(false, true, false),
            arrayOf(false, false, true),
            arrayOf(false, true, true)
        ).allFail(
            emptyArray(),
            arrayOf(false),
            arrayOf(false, false),
            arrayOf(false, false, false)
        )
    }

    @Test
    fun anyFalse_meansAtLeastOneOfTheBooleansInTheArrayIsFalse() {
        constraint<Array<Boolean>> {
            anyFalse()
        }.allMatch(
            arrayOf(false),
            arrayOf(false, false),
            arrayOf(false, true),
            arrayOf(true, false),
            arrayOf(false, false, false),
            arrayOf(false, true, true),
            arrayOf(true, false, true),
            arrayOf(true, true, false),
            arrayOf(true, false, false)
        ).allFail(
            emptyArray(),
            arrayOf(true),
            arrayOf(true, true),
            arrayOf(true, true, true)
        )
    }

    @Test
    fun allFalse_meansAllOfTheBooleansInTheArrayAreFalse() {
        constraint<Array<Boolean>> {
            allFalse()
        }.allMatch(
            emptyArray(),
            arrayOf(false),
            arrayOf(false, false),
            arrayOf(false, false, false)
        ).allFail(
            arrayOf(true),
            arrayOf(true, true),
            arrayOf(true, false),
            arrayOf(true, true, true),
            arrayOf(false, true, true),
            arrayOf(true, false, true),
            arrayOf(true, true, false),
            arrayOf(true, false, false)
        )
    }

    @Test
    fun contentEquals_meansAllTheElementsOfTheArrayMustExistInTheGivenElements() {

        constraint<Array<Int>> {
            contentEquals(
                ignoreDuplicates = false,
                ignoreOrder = false,
                other = arrayOf(1, 2, 3, 4, 5)
            )
        }.allMatch(
            arrayOf(1, 2, 3, 4, 5)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 3, 4, 4, 5),
            arrayOf(1, 2, 3, 4, 4),
            arrayOf(1, 2, 3, 5, 4),
            arrayOf(1, 2, 3, 5, 4, 5),
            arrayOf(5, 4, 3, 2, 1)
        )

        constraint<Array<Int>> {
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = false,
                other = arrayOf(1, 2, 3, 4, 5)
            )
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = false,
                other = arrayOf(1, 2, 3, 3, 4, 5, 5, 5)
            )
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = false,
                other = arrayOf(1, 2, 1, 3, 1, 3, 1, 4, 1, 5)
            )
        }.allMatch(
            arrayOf(1, 2, 3, 4, 5),
            arrayOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            arrayOf(1, 2, 2, 3, 4, 4, 5)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(1, 2, 3, 4, 4),
            arrayOf(1, 2, 3, 5, 4),
            arrayOf(5, 4, 3, 2, 1)
        )

        constraint<Array<Int>> {
            contentEquals(
                ignoreDuplicates = false,
                ignoreOrder = true,
                other = arrayOf(1, 2, 3, 4, 5)
            )
        }.allMatch(
            arrayOf(1, 2, 3, 4, 5),
            arrayOf(2, 1, 5, 4, 3),
            arrayOf(1, 2, 3, 5, 4),
            arrayOf(5, 4, 3, 2, 1)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 3, 4, 4, 5),
            arrayOf(1, 2, 3, 4, 4),
            arrayOf(1, 2, 3, 5, 4, 5)
        )

        constraint<Array<Int>> {
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = true,
                other = arrayOf(1, 2, 3, 4, 5)
            )
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = true,
                other = arrayOf(3, 2, 1, 3, 3, 4, 5, 5, 5)
            )
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = true,
                other = arrayOf(2, 1, 3, 1, 3, 1, 4, 1, 5)
            )
        }.allMatch(
            arrayOf(1, 2, 3, 4, 5),
            arrayOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            arrayOf(1, 2, 2, 3, 4, 4, 5),
            arrayOf(5, 4, 3, 2, 1),
            arrayOf(3, 2, 2, 3, 1, 4, 4, 5, 5, 5, 5),
            arrayOf(5, 2, 2, 3, 1, 4, 4, 5)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(1, 2, 3, 4, 4)
        )
    }

    @Test
    fun contentEqualsL_meansAllTheElementsOfTheArrayMustExistInTheGivenElements() {

        constraint<Array<Int>> {
            contentEquals(
                ignoreDuplicates = false,
                ignoreOrder = false
            ) { arrayOf(1, 2, 3, 4, 5) }
        }.allMatch(
            arrayOf(1, 2, 3, 4, 5)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 3, 4, 4, 5),
            arrayOf(1, 2, 3, 4, 4),
            arrayOf(1, 2, 3, 5, 4),
            arrayOf(1, 2, 3, 5, 4, 5),
            arrayOf(5, 4, 3, 2, 1)
        )

        constraint<Array<Int>> {
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = false
            ) { arrayOf(1, 2, 3, 4, 5) }
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = false
            ) { arrayOf(1, 2, 3, 3, 4, 5, 5, 5) }
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = false
            ) { arrayOf(1, 2, 1, 3, 1, 3, 1, 4, 1, 5) }
        }.allMatch(
            arrayOf(1, 2, 3, 4, 5),
            arrayOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            arrayOf(1, 2, 2, 3, 4, 4, 5)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(1, 2, 3, 4, 4),
            arrayOf(1, 2, 3, 5, 4),
            arrayOf(5, 4, 3, 2, 1)
        )

        constraint<Array<Int>> {
            contentEquals(
                ignoreDuplicates = false,
                ignoreOrder = true
            ) { arrayOf(1, 2, 3, 4, 5) }
        }.allMatch(
            arrayOf(1, 2, 3, 4, 5),
            arrayOf(2, 1, 5, 4, 3),
            arrayOf(1, 2, 3, 5, 4),
            arrayOf(5, 4, 3, 2, 1)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 3, 4, 4, 5),
            arrayOf(1, 2, 3, 4, 4),
            arrayOf(1, 2, 3, 5, 4, 5)
        )

        constraint<Array<Int>> {
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = true
            ) { arrayOf(1, 2, 3, 4, 5) }
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = true
            ) { arrayOf(3, 2, 1, 3, 3, 4, 5, 5, 5) }
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = true
            ) { arrayOf(2, 1, 3, 1, 3, 1, 4, 1, 5) }
        }.allMatch(
            arrayOf(1, 2, 3, 4, 5),
            arrayOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            arrayOf(1, 2, 2, 3, 4, 4, 5),
            arrayOf(5, 4, 3, 2, 1),
            arrayOf(3, 2, 2, 3, 1, 4, 4, 5, 5, 5, 5),
            arrayOf(5, 2, 2, 3, 1, 4, 4, 5)
        ).allFail(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(1, 2, 3, 4, 4)
        )
    }

    @Test
    fun contentNotEquals_meansAllTheElementsOfTheArrayMustNotEqualTheGivenElements() {

        constraint<Array<Int>> {
            contentNotEquals(
                ignoreDuplicates = false,
                ignoreOrder = false,
                other = arrayOf(1, 2, 3, 4, 5)
            )
        }.allMatch(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 3, 4, 4, 5),
            arrayOf(1, 2, 3, 4, 4),
            arrayOf(1, 2, 3, 5, 4),
            arrayOf(1, 2, 3, 5, 4, 5),
            arrayOf(5, 4, 3, 2, 1)
        ).allFail(
            arrayOf(1, 2, 3, 4, 5)
        )

        constraint<Array<Int>> {
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = false,
                other = arrayOf(1, 2, 3, 4, 5)
            )
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = false,
                other = arrayOf(1, 2, 3, 3, 4, 5, 5, 5)
            )
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = false,
                other = arrayOf(1, 2, 1, 3, 1, 3, 1, 4, 1, 5)
            )
        }.allMatch(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(1, 2, 3, 4, 4),
            arrayOf(1, 2, 3, 5, 4),
            arrayOf(5, 4, 3, 2, 1)
        ).allFail(
            arrayOf(1, 2, 3, 4, 5),
            arrayOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            arrayOf(1, 2, 2, 3, 4, 4, 5)
        )

        constraint<Array<Int>> {
            contentNotEquals(
                ignoreDuplicates = false,
                ignoreOrder = true,
                other = arrayOf(1, 2, 3, 4, 5)
            )
        }.allMatch(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 3, 4, 4, 5),
            arrayOf(1, 2, 3, 4, 4),
            arrayOf(1, 2, 3, 5, 4, 5)
        ).allFail(
            arrayOf(1, 2, 3, 4, 5),
            arrayOf(2, 1, 5, 4, 3),
            arrayOf(1, 2, 3, 5, 4),
            arrayOf(5, 4, 3, 2, 1)
        )

        constraint<Array<Int>> {
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = true,
                other = arrayOf(1, 2, 3, 4, 5)
            )
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = true,
                other = arrayOf(3, 2, 1, 3, 3, 4, 5, 5, 5)
            )
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = true,
                other = arrayOf(2, 1, 3, 1, 3, 1, 4, 1, 5)
            )
        }.allMatch(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(1, 2, 3, 4, 4)
        ).allFail(
            arrayOf(1, 2, 3, 4, 5),
            arrayOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            arrayOf(1, 2, 2, 3, 4, 4, 5),
            arrayOf(5, 4, 3, 2, 1),
            arrayOf(3, 2, 2, 3, 1, 4, 4, 5, 5, 5, 5),
            arrayOf(5, 2, 2, 3, 1, 4, 4, 5)
        )
    }

    @Test
    fun contentNotEqualsL_meansAllTheElementsOfTheArrayMustNotEqualTheGivenElements() {

        constraint<Array<Int>> {
            contentNotEquals(
                ignoreDuplicates = false,
                ignoreOrder = false
            ) { arrayOf(1, 2, 3, 4, 5) }
        }.allMatch(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 3, 4, 4, 5),
            arrayOf(1, 2, 3, 4, 4),
            arrayOf(1, 2, 3, 5, 4),
            arrayOf(1, 2, 3, 5, 4, 5),
            arrayOf(5, 4, 3, 2, 1)
        ).allFail(
            arrayOf(1, 2, 3, 4, 5)
        )

        constraint<Array<Int>> {
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = false
            ) { arrayOf(1, 2, 3, 4, 5) }
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = false
            ) { arrayOf(1, 2, 3, 3, 4, 5, 5, 5) }
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = false
            ) { arrayOf(1, 2, 1, 3, 1, 3, 1, 4, 1, 5) }
        }.allMatch(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(1, 2, 3, 4, 4),
            arrayOf(1, 2, 3, 5, 4),
            arrayOf(5, 4, 3, 2, 1)
        ).allFail(
            arrayOf(1, 2, 3, 4, 5),
            arrayOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            arrayOf(1, 2, 2, 3, 4, 4, 5)
        )

        constraint<Array<Int>> {
            contentNotEquals(
                ignoreDuplicates = false,
                ignoreOrder = true
            ) { arrayOf(1, 2, 3, 4, 5) }
        }.allMatch(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 3, 4, 4, 5),
            arrayOf(1, 2, 3, 4, 4),
            arrayOf(1, 2, 3, 5, 4, 5)
        ).allFail(
            arrayOf(1, 2, 3, 4, 5),
            arrayOf(2, 1, 5, 4, 3),
            arrayOf(1, 2, 3, 5, 4),
            arrayOf(5, 4, 3, 2, 1)
        )

        constraint<Array<Int>> {
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = true
            ) { arrayOf(1, 2, 3, 4, 5) }
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = true
            ) { arrayOf(3, 2, 1, 3, 3, 4, 5, 5, 5) }
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = true
            ) { arrayOf(2, 1, 3, 1, 3, 1, 4, 1, 5) }
        }.allMatch(
            emptyArray(),
            arrayOf(1),
            arrayOf(1, 2),
            arrayOf(1, 2, 3, 4),
            arrayOf(3, 3, 2, 4),
            arrayOf(1, 2, 3, 4, 5, 6),
            arrayOf(1, 2, 3, 4, 4)
        ).allFail(
            arrayOf(1, 2, 3, 4, 5),
            arrayOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            arrayOf(1, 2, 2, 3, 4, 4, 5),
            arrayOf(5, 4, 3, 2, 1),
            arrayOf(3, 2, 2, 3, 1, 4, 4, 5, 5, 5, 5),
            arrayOf(5, 2, 2, 3, 1, 4, 4, 5)
        )
    }
}
