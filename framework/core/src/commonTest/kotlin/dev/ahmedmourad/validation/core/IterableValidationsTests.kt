package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.utils.allFail
import dev.ahmedmourad.validation.core.utils.allMatch
import dev.ahmedmourad.validation.core.utils.constraint
import dev.ahmedmourad.validation.core.validations.*
import kotlin.test.Test

class IterableValidationsTests {

    @Test
    fun forAll_meansTheGivenValidationsMatchAllTheElementsOfTheIterable() {
        constraint<Iterable<Boolean>> {
            forAll {
                isTrue()
            }
        }.allMatch(
            emptyList(),
            listOf(true),
            listOf(true, true),
            listOf(true, true, true)
        ).allFail(
            listOf(false),
            listOf(false, false),
            listOf(false, true),
            listOf(false, false, false),
            listOf(true, false, false),
            listOf(false, true, false),
            listOf(false, false, true),
            listOf(false, true, true)
        )
    }

    @Test
    fun forAny_meansTheGivenValidationsMatchAtLeastOneOfTheElementsOfTheIterable() {
        constraint<Iterable<Boolean>> {
            forAny {
                isTrue()
            }
        }.allMatch(
            listOf(true),
            listOf(true, true),
            listOf(true, false),
            listOf(false, true),
            listOf(true, true, true),
            listOf(true, false, false),
            listOf(false, true, false),
            listOf(false, false, true),
            listOf(false, true, true)
        ).allFail(
            emptyList(),
            listOf(false),
            listOf(false, false),
            listOf(false, false, false)
        )
    }

    @Test
    fun forNone_meansTheGivenValidationsMatchNoneOfTheElementsOfTheIterable() {
        constraint<Iterable<Boolean>> {
            forNone {
                isTrue()
            }
        }.allMatch(
            emptyList(),
            listOf(false),
            listOf(false, false),
            listOf(false, false, false)
        ).allFail(
            listOf(true),
            listOf(true, true),
            listOf(true, false),
            listOf(true, true, true),
            listOf(false, true, true),
            listOf(true, false, true),
            listOf(true, true, false),
            listOf(true, false, false)
        )
    }

    @Test
    fun isEmpty_meansTheIterableIsEmpty() {
        constraint<Iterable<Unit>> {
            isEmpty()
        }.allMatch(
            emptyList()
        ).allFail(
            listOf(Unit),
            listOf(Unit, Unit),
            listOf(Unit, Unit, Unit)
        )
    }

    @Test
    fun isNotEmpty_meansTheIterableIsNotEmpty() {
        constraint<Iterable<Unit>> {
            isNotEmpty()
        }.allMatch(
            listOf(Unit),
            listOf(Unit, Unit),
            listOf(Unit, Unit, Unit)
        ).allFail(
            emptyList()
        )
    }

    @Test
    fun isDistinct_meansAllTheElementsOfTheIterableAreDistinct() {
        constraint<Iterable<Int>> {
            isDistinct()
        }.allMatch(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3)
        ).allFail(
            listOf(1, 1),
            listOf(1, 2, 1),
            listOf(2, 2, 1),
            listOf(1, 2, 2),
            listOf(1, 2, 2, 3)
        )
    }

    @Test
    fun minSize_meansTheIterableMustHaveAtLeastTheGivenNumberOfElements() {
        constraint<Iterable<Int>> {
            minSize(4)
        }.allMatch(
            listOf(1, 2, 3, 4),
            listOf(1, 2, 3, 4, 5),
            listOf(1, 2, 3, 4, 5, 6),
            listOf(1, 2, 3, 4, 5, 6, 7),
            listOf(1, 2, 3, 4, 5, 6, 7, 8)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3)
        )
    }

    @Test
    fun minSizeL_meansTheIterableMustHaveAtLeastTheGivenNumberOfElements() {
        constraint<Iterable<Int>> {
            minSize { 4 }
        }.allMatch(
            listOf(1, 2, 3, 4),
            listOf(1, 2, 3, 4, 5),
            listOf(1, 2, 3, 4, 5, 6),
            listOf(1, 2, 3, 4, 5, 6, 7),
            listOf(1, 2, 3, 4, 5, 6, 7, 8)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3)
        )
    }

    @Test
    fun maxSize_meansTheIterableMustHaveAtMostTheGivenNumberOfElements() {
        constraint<Iterable<Int>> {
            maxSize(4)
        }.allMatch(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3),
            listOf(1, 2, 3, 4)
        ).allFail(
            listOf(1, 2, 3, 4, 5),
            listOf(1, 2, 3, 4, 5, 6),
            listOf(1, 2, 3, 4, 5, 6, 7),
            listOf(1, 2, 3, 4, 5, 6, 7, 8)
        )
    }

    @Test
    fun maxSizeL_meansTheIterableMustHaveAtMostTheGivenNumberOfElements() {
        constraint<Iterable<Int>> {
            maxSize { 4 }
        }.allMatch(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3),
            listOf(1, 2, 3, 4)
        ).allFail(
            listOf(1, 2, 3, 4, 5),
            listOf(1, 2, 3, 4, 5, 6),
            listOf(1, 2, 3, 4, 5, 6, 7),
            listOf(1, 2, 3, 4, 5, 6, 7, 8)
        )
    }

    @Test
    fun sizeLessThan_meansTheIterableMustHaveLessThanTheGivenNumberOfElements() {
        constraint<Iterable<Int>> {
            sizeLessThan(4)
        }.allMatch(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3)
        ).allFail(
            listOf(1, 2, 3, 4),
            listOf(1, 2, 3, 4, 5),
            listOf(1, 2, 3, 4, 5, 6),
            listOf(1, 2, 3, 4, 5, 6, 7),
            listOf(1, 2, 3, 4, 5, 6, 7, 8)
        )
    }

    @Test
    fun sizeLessThanL_meansTheIterableMustHaveLessThanTheGivenNumberOfElements() {
        constraint<Iterable<Int>> {
            sizeLessThan { 4 }
        }.allMatch(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3)
        ).allFail(
            listOf(1, 2, 3, 4),
            listOf(1, 2, 3, 4, 5),
            listOf(1, 2, 3, 4, 5, 6),
            listOf(1, 2, 3, 4, 5, 6, 7),
            listOf(1, 2, 3, 4, 5, 6, 7, 8)
        )
    }

    @Test
    fun sizeLargerThan_meansTheIterableMustHaveMoreThanTheGivenNumberOfElements() {
        constraint<Iterable<Int>> {
            sizeLargerThan(4)
        }.allMatch(
            listOf(1, 2, 3, 4, 5),
            listOf(1, 2, 3, 4, 5, 6),
            listOf(1, 2, 3, 4, 5, 6, 7),
            listOf(1, 2, 3, 4, 5, 6, 7, 8)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3),
            listOf(1, 2, 3, 4)
        )
    }

    @Test
    fun sizeLargerThanL_meansTheIterableMustHaveMoreThanTheGivenNumberOfElements() {
        constraint<Iterable<Int>> {
            sizeLargerThan { 4 }
        }.allMatch(
            listOf(1, 2, 3, 4, 5),
            listOf(1, 2, 3, 4, 5, 6),
            listOf(1, 2, 3, 4, 5, 6, 7),
            listOf(1, 2, 3, 4, 5, 6, 7, 8)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3),
            listOf(1, 2, 3, 4)
        )
    }

    @Test
    fun sizeIn_meansTheIterableMustHaveTheGivenRangeOfElements() {
        constraint<Iterable<Int>> {
            sizeIn(3, 5)
        }.allMatch(
            listOf(1, 2, 3),
            listOf(1, 2, 3, 4),
            listOf(1, 2, 3, 4, 5)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4, 5, 6),
            listOf(1, 2, 3, 4, 5, 6, 7),
            listOf(1, 2, 3, 4, 5, 6, 7, 8)
        )
    }

    @Test
    fun sizeInR_meansTheIterableMustHaveTheGivenRangeOfElements() {
        constraint<Iterable<Int>> {
            sizeIn(3..5)
        }.allMatch(
            listOf(1, 2, 3),
            listOf(1, 2, 3, 4),
            listOf(1, 2, 3, 4, 5)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4, 5, 6),
            listOf(1, 2, 3, 4, 5, 6, 7),
            listOf(1, 2, 3, 4, 5, 6, 7, 8)
        )
    }

    @Test
    fun sizeInRL_meansTheIterableMustHaveTheGivenRangeOfElements() {
        constraint<Iterable<Int>> {
            sizeIn { 3..5 }
        }.allMatch(
            listOf(1, 2, 3),
            listOf(1, 2, 3, 4),
            listOf(1, 2, 3, 4, 5)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4, 5, 6),
            listOf(1, 2, 3, 4, 5, 6, 7),
            listOf(1, 2, 3, 4, 5, 6, 7, 8)
        )
    }

    @Test
    fun sizeNotIn_meansTheIterableMustNotHaveTheGivenRangeOfElements() {
        constraint<Iterable<Int>> {
            sizeNotIn(3, 5)
        }.allMatch(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4, 5, 6),
            listOf(1, 2, 3, 4, 5, 6, 7),
            listOf(1, 2, 3, 4, 5, 6, 7, 8)
        ).allFail(
            listOf(1, 2, 3),
            listOf(1, 2, 3, 4),
            listOf(1, 2, 3, 4, 5)
        )
    }

    @Test
    fun sizeNotInR_meansTheIterableMustNotHaveTheGivenRangeOfElements() {
        constraint<Iterable<Int>> {
            sizeNotIn(3..5)
        }.allMatch(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4, 5, 6),
            listOf(1, 2, 3, 4, 5, 6, 7),
            listOf(1, 2, 3, 4, 5, 6, 7, 8)
        ).allFail(
            listOf(1, 2, 3),
            listOf(1, 2, 3, 4),
            listOf(1, 2, 3, 4, 5)
        )
    }

    @Test
    fun sizeNotInRL_meansTheIterableMustNotHaveTheGivenRangeOfElements() {
        constraint<Iterable<Int>> {
            sizeNotIn { 3..5 }
        }.allMatch(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4, 5, 6),
            listOf(1, 2, 3, 4, 5, 6, 7),
            listOf(1, 2, 3, 4, 5, 6, 7, 8)
        ).allFail(
            listOf(1, 2, 3),
            listOf(1, 2, 3, 4),
            listOf(1, 2, 3, 4, 5)
        )
    }

    @Test
    fun sizeEqualTo_meansTheIterableMustHaveTheGivenNumberOfElements() {
        constraint<Iterable<Int>> {
            sizeEqualTo(3)
        }.allMatch(
            listOf(1, 2, 3)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4),
            listOf(1, 2, 3, 4, 5)
        )
    }

    @Test
    fun sizeEqualToL_meansTheIterableMustHaveTheGivenNumberOfElements() {
        constraint<Iterable<Int>> {
            sizeEqualTo { 3 }
        }.allMatch(
            listOf(1, 2, 3)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4),
            listOf(1, 2, 3, 4, 5)
        )
    }

    @Test
    fun sizeNotEqualTo_meansTheIterableMustNotHaveTheGivenNumberOfElements() {
        constraint<Iterable<Int>> {
            sizeNotEqualTo(3)
        }.allMatch(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4),
            listOf(1, 2, 3, 4, 5)
        ).allFail(
            listOf(1, 2, 3)
        )
    }

    @Test
    fun sizeNotEqualToL_meansTheIterableMustNotHaveTheGivenNumberOfElements() {
        constraint<Iterable<Int>> {
            sizeNotEqualTo { 3 }
        }.allMatch(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4),
            listOf(1, 2, 3, 4, 5)
        ).allFail(
            listOf(1, 2, 3)
        )
    }

    @Test
    fun contains_meansTheIterableMustContainTheGivenElement() {
        constraint<Iterable<Int>> {
            contains(3)
        }.allMatch(
            listOf(3),
            listOf(1, 3),
            listOf(3, 1, 2, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 5)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 4),
            listOf(1, 2, 4, 5)
        )
    }

    @Test
    fun containsL_meansTheIterableMustContainTheGivenElement() {
        constraint<Iterable<Int>> {
            contains { 3 }
        }.allMatch(
            listOf(3),
            listOf(1, 3),
            listOf(3, 1, 2, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 5)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 4),
            listOf(1, 2, 4, 5)
        )
    }

    @Test
    fun doesNotContain_meansTheIterableMustContainTheGivenElement() {
        constraint<Iterable<Int>> {
            doesNotContain(3)
        }.allMatch(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 4),
            listOf(1, 2, 4, 5)
        ).allFail(
            listOf(3),
            listOf(1, 3),
            listOf(3, 1, 2, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 5)
        )
    }

    @Test
    fun doesNotContainL_meansTheIterableMustContainTheGivenElement() {
        constraint<Iterable<Int>> {
            doesNotContain { 3 }
        }.allMatch(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 4),
            listOf(1, 2, 4, 5)
        ).allFail(
            listOf(3),
            listOf(1, 3),
            listOf(3, 1, 2, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 5)
        )
    }

    @Test
    fun containsAt_meansTheIterableMustContainTheGivenElementAtTheGivenPosition() {
        constraint<Iterable<Int>> {
            containsAt(1, 8)
        }.allMatch(
            listOf(8, 8),
            listOf(1, 8),
            listOf(3, 8, 2, 4),
            listOf(3, 8, 2, 4),
            listOf(1, 8, 3, 4, 5)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(8),
            listOf(1, 2),
            listOf(1, 2, 8),
            listOf(8, 2, 8),
            listOf(8, 2, 8, 8)
        )
    }

    @Test
    fun containsAtL_meansTheIterableMustContainTheGivenElementAtTheGivenPosition() {
        constraint<Iterable<Int>> {
            containsAt(1) { 8 }
        }.allMatch(
            listOf(8, 8),
            listOf(1, 8),
            listOf(3, 8, 2, 4),
            listOf(3, 8, 2, 4),
            listOf(1, 8, 3, 4, 5)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(8),
            listOf(1, 2),
            listOf(1, 2, 8),
            listOf(8, 2, 8),
            listOf(8, 2, 8, 8)
        )
    }

    @Test
    fun startsWith_meansTheIterableMustStartWithTheGivenElement() {
        constraint<Iterable<Int>> {
            startsWith(8)
        }.allMatch(
            listOf(8),
            listOf(8, 1),
            listOf(8, 8, 2, 4),
            listOf(8, 8, 2, 4),
            listOf(8, 8, 3, 4, 5)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 8),
            listOf(1, 8, 8),
            listOf(1, 8, 3),
            listOf(1, 8, 3, 8)
        )
    }

    @Test
    fun startsWithL_meansTheIterableMustStartWithTheGivenElement() {
        constraint<Iterable<Int>> {
            startsWith { 8 }
        }.allMatch(
            listOf(8),
            listOf(8, 1),
            listOf(8, 8, 2, 4),
            listOf(8, 8, 2, 4),
            listOf(8, 8, 3, 4, 5)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 8),
            listOf(1, 8, 8),
            listOf(1, 8, 3),
            listOf(1, 8, 3, 8)
        )
    }

    @Test
    fun endsWith_meansTheIterableMustEndWithTheGivenElement() {
        constraint<Iterable<Int>> {
            endsWith(8)
        }.allMatch(
            listOf(8),
            listOf(8, 8),
            listOf(2, 8, 2, 8),
            listOf(2, 8, 2, 8),
            listOf(2, 8, 3, 4, 8)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(8, 7),
            listOf(5, 8, 5),
            listOf(1, 8, 3),
            listOf(8, 8, 8, 7)
        )
    }

    @Test
    fun endsWithL_meansTheIterableMustEndWithTheGivenElement() {
        constraint<Iterable<Int>> {
            endsWith { 8 }
        }.allMatch(
            listOf(8),
            listOf(8, 8),
            listOf(2, 8, 2, 8),
            listOf(2, 8, 2, 8),
            listOf(2, 8, 3, 4, 8)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(8, 7),
            listOf(5, 8, 5),
            listOf(1, 8, 3),
            listOf(8, 8, 8, 7)
        )
    }

    @Test
    fun containsAll_meansTheIterableMustContainAllTheGivenElements() {
        constraint<Iterable<Int>> {
            containsAll(2, 3)
        }.allMatch(
            listOf(2, 3),
            listOf(3, 2),
            listOf(3, 1, 2, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 5)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 4),
            listOf(1, 2, 4, 5)
        )
    }

    @Test
    fun containsAllL_meansTheIterableMustContainAllTheGivenElements() {
        constraint<Iterable<Int>> {
            containsAll(listOf(2, 3))
        }.allMatch(
            listOf(2, 3),
            listOf(3, 2),
            listOf(3, 1, 2, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 5)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 4),
            listOf(1, 2, 4, 5)
        )
    }

    @Test
    fun containsAllLL_meansTheIterableMustContainAllTheGivenElements() {
        constraint<Iterable<Int>> {
            containsAll { listOf(2, 3) }
        }.allMatch(
            listOf(2, 3),
            listOf(3, 2),
            listOf(3, 1, 2, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 5)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 4),
            listOf(1, 2, 4, 5)
        )
    }

    @Test
    fun isPartOf_meansAllTheElementsOfTheIterableMustExistInTheGivenElements() {
        constraint<Iterable<Int>> {
            isPartOf(1, 2, 3, 4, 5)
        }.allMatch(
            emptyList(),
            listOf(3),
            listOf(2, 3),
            listOf(3, 2),
            listOf(3, 1, 2, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 4, 3, 5)
        ).allFail(
            listOf(6),
            listOf(6, 1),
            listOf(7, 3, 9),
            listOf(4, 9, 1, 2)
        )
    }

    @Test
    fun isPartOfL_meansAllTheElementsOfTheIterableMustExistInTheGivenElements() {
        constraint<Iterable<Int>> {
            isPartOf(listOf(1, 2, 3, 4, 5))
        }.allMatch(
            emptyList(),
            listOf(3),
            listOf(2, 3),
            listOf(3, 2),
            listOf(3, 1, 2, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 4, 3, 5)
        ).allFail(
            listOf(6),
            listOf(6, 1),
            listOf(7, 3, 9),
            listOf(4, 9, 1, 2)
        )
    }

    @Test
    fun isPartOfLL_meansAllTheElementsOfTheIterableMustExistInTheGivenElements() {
        constraint<Iterable<Int>> {
            isPartOf { listOf(1, 2, 3, 4, 5) }
        }.allMatch(
            emptyList(),
            listOf(3),
            listOf(2, 3),
            listOf(3, 2),
            listOf(3, 1, 2, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 4, 3, 5)
        ).allFail(
            listOf(6),
            listOf(6, 1),
            listOf(7, 3, 9),
            listOf(4, 9, 1, 2)
        )
    }

    @Test
    fun allTrue_meansAllTheBooleansInTheIterableAreTrue() {
        constraint<Iterable<Boolean>> {
            allTrue()
        }.allMatch(
            emptyList(),
            listOf(true),
            listOf(true, true),
            listOf(true, true, true)
        ).allFail(
            listOf(false),
            listOf(false, false),
            listOf(false, true),
            listOf(false, false, false),
            listOf(true, false, false),
            listOf(false, true, false),
            listOf(false, false, true),
            listOf(false, true, true)
        )
    }

    @Test
    fun anyTrue_meansAtLeastOneOfTheBooleansInTheIterableIsTrue() {
        constraint<Iterable<Boolean>> {
            anyTrue()
        }.allMatch(
            listOf(true),
            listOf(true, true),
            listOf(true, false),
            listOf(false, true),
            listOf(true, true, true),
            listOf(true, false, false),
            listOf(false, true, false),
            listOf(false, false, true),
            listOf(false, true, true)
        ).allFail(
            emptyList(),
            listOf(false),
            listOf(false, false),
            listOf(false, false, false)
        )
    }

    @Test
    fun anyFalse_meansAtLeastOneOfTheBooleansInTheIterableIsFalse() {
        constraint<Iterable<Boolean>> {
            anyFalse()
        }.allMatch(
            listOf(false),
            listOf(false, false),
            listOf(false, true),
            listOf(true, false),
            listOf(false, false, false),
            listOf(false, true, true),
            listOf(true, false, true),
            listOf(true, true, false),
            listOf(true, false, false)
        ).allFail(
            emptyList(),
            listOf(true),
            listOf(true, true),
            listOf(true, true, true)
        )
    }

    @Test
    fun allFalse_meansAllOfTheBooleansInTheIterableAreFalse() {
        constraint<Iterable<Boolean>> {
            allFalse()
        }.allMatch(
            emptyList(),
            listOf(false),
            listOf(false, false),
            listOf(false, false, false)
        ).allFail(
            listOf(true),
            listOf(true, true),
            listOf(true, false),
            listOf(true, true, true),
            listOf(false, true, true),
            listOf(true, false, true),
            listOf(true, true, false),
            listOf(true, false, false)
        )
    }

    @Test
    fun contentEquals_meansAllTheElementsOfTheIterableMustExistInTheGivenElements() {

        constraint<Iterable<Int>> {
            contentEquals(
                ignoreDuplicates = false,
                ignoreOrder = false,
                other = listOf(1, 2, 3, 4, 5)
            )
        }.allMatch(
            listOf(1, 2, 3, 4, 5)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 4, 5),
            listOf(1, 2, 3, 4, 4),
            listOf(1, 2, 3, 5, 4),
            listOf(1, 2, 3, 5, 4, 5),
            listOf(5, 4, 3, 2, 1)
        )

        constraint<Iterable<Int>> {
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = false,
                other = listOf(1, 2, 3, 4, 5)
            )
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = false,
                other = listOf(1, 2, 3, 3, 4, 5, 5, 5)
            )
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = false,
                other = listOf(1, 2, 1, 3, 1, 3, 1, 4, 1, 5)
            )
        }.allMatch(
            listOf(1, 2, 3, 4, 5),
            listOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            listOf(1, 2, 2, 3, 4, 4, 5)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 5, 6),
            listOf(1, 2, 3, 4, 4),
            listOf(1, 2, 3, 5, 4),
            listOf(5, 4, 3, 2, 1)
        )

        constraint<Iterable<Int>> {
            contentEquals(
                ignoreDuplicates = false,
                ignoreOrder = true,
                other = listOf(1, 2, 3, 4, 5)
            )
        }.allMatch(
            listOf(1, 2, 3, 4, 5),
            listOf(2, 1, 5, 4, 3),
            listOf(1, 2, 3, 5, 4),
            listOf(5, 4, 3, 2, 1)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 4, 5),
            listOf(1, 2, 3, 4, 4),
            listOf(1, 2, 3, 5, 4, 5)
        )

        constraint<Iterable<Int>> {
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = true,
                other = listOf(1, 2, 3, 4, 5)
            )
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = true,
                other = listOf(3, 2, 1, 3, 3, 4, 5, 5, 5)
            )
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = true,
                other = listOf(2, 1, 3, 1, 3, 1, 4, 1, 5)
            )
        }.allMatch(
            listOf(1, 2, 3, 4, 5),
            listOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            listOf(1, 2, 2, 3, 4, 4, 5),
            listOf(5, 4, 3, 2, 1),
            listOf(3, 2, 2, 3, 1, 4, 4, 5, 5, 5, 5),
            listOf(5, 2, 2, 3, 1, 4, 4, 5)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 5, 6),
            listOf(1, 2, 3, 4, 4)
        )
    }

    @Test
    fun contentEqualsL_meansAllTheElementsOfTheIterableMustExistInTheGivenElements() {

        constraint<Iterable<Int>> {
            contentEquals(
                ignoreDuplicates = false,
                ignoreOrder = false
            ) { listOf(1, 2, 3, 4, 5) }
        }.allMatch(
            listOf(1, 2, 3, 4, 5)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 4, 5),
            listOf(1, 2, 3, 4, 4),
            listOf(1, 2, 3, 5, 4),
            listOf(1, 2, 3, 5, 4, 5),
            listOf(5, 4, 3, 2, 1)
        )

        constraint<Iterable<Int>> {
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = false
            ) { listOf(1, 2, 3, 4, 5) }
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = false
            ) { listOf(1, 2, 3, 3, 4, 5, 5, 5) }
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = false
            ) { listOf(1, 2, 1, 3, 1, 3, 1, 4, 1, 5) }
        }.allMatch(
            listOf(1, 2, 3, 4, 5),
            listOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            listOf(1, 2, 2, 3, 4, 4, 5)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 5, 6),
            listOf(1, 2, 3, 4, 4),
            listOf(1, 2, 3, 5, 4),
            listOf(5, 4, 3, 2, 1)
        )

        constraint<Iterable<Int>> {
            contentEquals(
                ignoreDuplicates = false,
                ignoreOrder = true
            ) { listOf(1, 2, 3, 4, 5) }
        }.allMatch(
            listOf(1, 2, 3, 4, 5),
            listOf(2, 1, 5, 4, 3),
            listOf(1, 2, 3, 5, 4),
            listOf(5, 4, 3, 2, 1)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 4, 5),
            listOf(1, 2, 3, 4, 4),
            listOf(1, 2, 3, 5, 4, 5)
        )

        constraint<Iterable<Int>> {
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = true
            ) { listOf(1, 2, 3, 4, 5) }
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = true
            ) { listOf(3, 2, 1, 3, 3, 4, 5, 5, 5) }
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = true
            ) { listOf(2, 1, 3, 1, 3, 1, 4, 1, 5) }
        }.allMatch(
            listOf(1, 2, 3, 4, 5),
            listOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            listOf(1, 2, 2, 3, 4, 4, 5),
            listOf(5, 4, 3, 2, 1),
            listOf(3, 2, 2, 3, 1, 4, 4, 5, 5, 5, 5),
            listOf(5, 2, 2, 3, 1, 4, 4, 5)
        ).allFail(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 5, 6),
            listOf(1, 2, 3, 4, 4)
        )
    }

    @Test
    fun contentNotEquals_meansAllTheElementsOfTheIterableMustNotEqualTheGivenElements() {

        constraint<Iterable<Int>> {
            contentNotEquals(
                ignoreDuplicates = false,
                ignoreOrder = false,
                other = listOf(1, 2, 3, 4, 5)
            )
        }.allMatch(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 4, 5),
            listOf(1, 2, 3, 4, 4),
            listOf(1, 2, 3, 5, 4),
            listOf(1, 2, 3, 5, 4, 5),
            listOf(5, 4, 3, 2, 1)
        ).allFail(
            listOf(1, 2, 3, 4, 5)
        )

        constraint<Iterable<Int>> {
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = false,
                other = listOf(1, 2, 3, 4, 5)
            )
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = false,
                other = listOf(1, 2, 3, 3, 4, 5, 5, 5)
            )
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = false,
                other = listOf(1, 2, 1, 3, 1, 3, 1, 4, 1, 5)
            )
        }.allMatch(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 5, 6),
            listOf(1, 2, 3, 4, 4),
            listOf(1, 2, 3, 5, 4),
            listOf(5, 4, 3, 2, 1)
        ).allFail(
            listOf(1, 2, 3, 4, 5),
            listOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            listOf(1, 2, 2, 3, 4, 4, 5)
        )

        constraint<Iterable<Int>> {
            contentNotEquals(
                ignoreDuplicates = false,
                ignoreOrder = true,
                other = listOf(1, 2, 3, 4, 5)
            )
        }.allMatch(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 4, 5),
            listOf(1, 2, 3, 4, 4),
            listOf(1, 2, 3, 5, 4, 5)
        ).allFail(
            listOf(1, 2, 3, 4, 5),
            listOf(2, 1, 5, 4, 3),
            listOf(1, 2, 3, 5, 4),
            listOf(5, 4, 3, 2, 1)
        )

        constraint<Iterable<Int>> {
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = true,
                other = listOf(1, 2, 3, 4, 5)
            )
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = true,
                other = listOf(3, 2, 1, 3, 3, 4, 5, 5, 5)
            )
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = true,
                other = listOf(2, 1, 3, 1, 3, 1, 4, 1, 5)
            )
        }.allMatch(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 5, 6),
            listOf(1, 2, 3, 4, 4)
        ).allFail(
            listOf(1, 2, 3, 4, 5),
            listOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            listOf(1, 2, 2, 3, 4, 4, 5),
            listOf(5, 4, 3, 2, 1),
            listOf(3, 2, 2, 3, 1, 4, 4, 5, 5, 5, 5),
            listOf(5, 2, 2, 3, 1, 4, 4, 5)
        )
    }

    @Test
    fun contentNotEqualsL_meansAllTheElementsOfTheIterableMustNotEqualTheGivenElements() {

        constraint<Iterable<Int>> {
            contentNotEquals(
                ignoreDuplicates = false,
                ignoreOrder = false
            ) { listOf(1, 2, 3, 4, 5) }
        }.allMatch(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 4, 5),
            listOf(1, 2, 3, 4, 4),
            listOf(1, 2, 3, 5, 4),
            listOf(1, 2, 3, 5, 4, 5),
            listOf(5, 4, 3, 2, 1)
        ).allFail(
            listOf(1, 2, 3, 4, 5)
        )

        constraint<Iterable<Int>> {
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = false
            ) { listOf(1, 2, 3, 4, 5) }
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = false
            ) { listOf(1, 2, 3, 3, 4, 5, 5, 5) }
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = false
            ) { listOf(1, 2, 1, 3, 1, 3, 1, 4, 1, 5) }
        }.allMatch(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 5, 6),
            listOf(1, 2, 3, 4, 4),
            listOf(1, 2, 3, 5, 4),
            listOf(5, 4, 3, 2, 1)
        ).allFail(
            listOf(1, 2, 3, 4, 5),
            listOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            listOf(1, 2, 2, 3, 4, 4, 5)
        )

        constraint<Iterable<Int>> {
            contentNotEquals(
                ignoreDuplicates = false,
                ignoreOrder = true
            ) { listOf(1, 2, 3, 4, 5) }
        }.allMatch(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 4, 5),
            listOf(1, 2, 3, 4, 4),
            listOf(1, 2, 3, 5, 4, 5)
        ).allFail(
            listOf(1, 2, 3, 4, 5),
            listOf(2, 1, 5, 4, 3),
            listOf(1, 2, 3, 5, 4),
            listOf(5, 4, 3, 2, 1)
        )

        constraint<Iterable<Int>> {
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = true
            ) { listOf(1, 2, 3, 4, 5) }
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = true
            ) { listOf(3, 2, 1, 3, 3, 4, 5, 5, 5) }
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = true
            ) { listOf(2, 1, 3, 1, 3, 1, 4, 1, 5) }
        }.allMatch(
            emptyList(),
            listOf(1),
            listOf(1, 2),
            listOf(1, 2, 3, 4),
            listOf(3, 3, 2, 4),
            listOf(1, 2, 3, 4, 5, 6),
            listOf(1, 2, 3, 4, 4)
        ).allFail(
            listOf(1, 2, 3, 4, 5),
            listOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            listOf(1, 2, 2, 3, 4, 4, 5),
            listOf(5, 4, 3, 2, 1),
            listOf(3, 2, 2, 3, 1, 4, 4, 5, 5, 5, 5),
            listOf(5, 2, 2, 3, 1, 4, 4, 5)
        )
    }
}
