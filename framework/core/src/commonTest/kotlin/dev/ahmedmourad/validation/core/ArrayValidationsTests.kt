package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.utils.allFail
import dev.ahmedmourad.validation.core.utils.allMatch
import dev.ahmedmourad.validation.core.utils.validator
import dev.ahmedmourad.validation.core.validations.*
import kotlin.test.Test

class ArrayValidationsTests {

    @Test
    fun forAll_meansTheGivenValidationsMatchAllTheElementsOfTheArray() {
        validator<Array<Boolean>> {
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
        validator<Array<Boolean>> {
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
        validator<Array<Boolean>> {
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
        validator<Array<Unit>> {
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
        validator<Array<Unit>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Int>> {
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
        validator<Array<Boolean>> {
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
        validator<Array<Boolean>> {
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
        validator<Array<Boolean>> {
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
        validator<Array<Boolean>> {
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

        validator<Array<Int>> {
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

        validator<Array<Int>> {
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

        validator<Array<Int>> {
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

        validator<Array<Int>> {
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

        validator<Array<Int>> {
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

        validator<Array<Int>> {
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

        validator<Array<Int>> {
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

        validator<Array<Int>> {
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

        validator<Array<Int>> {
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

        validator<Array<Int>> {
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

        validator<Array<Int>> {
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

        validator<Array<Int>> {
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

        validator<Array<Int>> {
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

        validator<Array<Int>> {
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

        validator<Array<Int>> {
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

        validator<Array<Int>> {
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
