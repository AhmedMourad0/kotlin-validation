package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.utils.allFail
import dev.ahmedmourad.validation.core.utils.allMatch
import dev.ahmedmourad.validation.core.utils.constraint
import dev.ahmedmourad.validation.core.validations.*
import kotlin.test.Test

class SequenceValidationsTests {

    @Test
    fun forAll_meansTheGivenValidationsMatchAllTheElementsOfTheSequence() {
        constraint<Sequence<Boolean>> {
            forAll {
                isTrue()
            }
        }.allMatch(
            emptySequence(),
            sequenceOf(true),
            sequenceOf(true, true),
            sequenceOf(true, true, true)
        ).allFail(
            sequenceOf(false),
            sequenceOf(false, false),
            sequenceOf(false, true),
            sequenceOf(false, false, false),
            sequenceOf(true, false, false),
            sequenceOf(false, true, false),
            sequenceOf(false, false, true),
            sequenceOf(false, true, true)
        )
    }

    @Test
    fun forAny_meansTheGivenValidationsMatchAtLeastOneOfTheElementsOfTheSequence() {
        constraint<Sequence<Boolean>> {
            forAny {
                isTrue()
            }
        }.allMatch(
            sequenceOf(true),
            sequenceOf(true, true),
            sequenceOf(true, false),
            sequenceOf(false, true),
            sequenceOf(true, true, true),
            sequenceOf(true, false, false),
            sequenceOf(false, true, false),
            sequenceOf(false, false, true),
            sequenceOf(false, true, true)
        ).allFail(
            emptySequence(),
            sequenceOf(false),
            sequenceOf(false, false),
            sequenceOf(false, false, false)
        )
    }

    @Test
    fun forNone_meansTheGivenValidationsMatchNoneOfTheElementsOfTheSequence() {
        constraint<Sequence<Boolean>> {
            forNone {
                isTrue()
            }
        }.allMatch(
            emptySequence(),
            sequenceOf(false),
            sequenceOf(false, false),
            sequenceOf(false, false, false)
        ).allFail(
            sequenceOf(true),
            sequenceOf(true, true),
            sequenceOf(true, false),
            sequenceOf(true, true, true),
            sequenceOf(false, true, true),
            sequenceOf(true, false, true),
            sequenceOf(true, true, false),
            sequenceOf(true, false, false)
        )
    }

    @Test
    fun isEmpty_meansTheSequenceIsEmpty() {
        constraint<Sequence<Unit>> {
            isEmpty()
        }.allMatch(
            emptySequence()
        ).allFail(
            sequenceOf(Unit),
            sequenceOf(Unit, Unit),
            sequenceOf(Unit, Unit, Unit)
        )
    }

    @Test
    fun isNotEmpty_meansTheSequenceIsNotEmpty() {
        constraint<Sequence<Unit>> {
            isNotEmpty()
        }.allMatch(
            sequenceOf(Unit),
            sequenceOf(Unit, Unit),
            sequenceOf(Unit, Unit, Unit)
        ).allFail(
            emptySequence()
        )
    }

    @Test
    fun isDistinct_meansAllTheElementsOfTheSequenceAreDistinct() {
        constraint<Sequence<Int>> {
            isDistinct()
        }.allMatch(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3)
        ).allFail(
            sequenceOf(1, 1),
            sequenceOf(1, 2, 1),
            sequenceOf(2, 2, 1),
            sequenceOf(1, 2, 2),
            sequenceOf(1, 2, 2, 3)
        )
    }

    @Test
    fun minSize_meansTheSequenceMustHaveAtLeastTheGivenNumberOfElements() {
        constraint<Sequence<Int>> {
            minSize(4)
        }.allMatch(
            sequenceOf(1, 2, 3, 4),
            sequenceOf(1, 2, 3, 4, 5),
            sequenceOf(1, 2, 3, 4, 5, 6),
            sequenceOf(1, 2, 3, 4, 5, 6, 7),
            sequenceOf(1, 2, 3, 4, 5, 6, 7, 8)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3)
        )
    }

    @Test
    fun minSizeL_meansTheSequenceMustHaveAtLeastTheGivenNumberOfElements() {
        constraint<Sequence<Int>> {
            minSize { 4 }
        }.allMatch(
            sequenceOf(1, 2, 3, 4),
            sequenceOf(1, 2, 3, 4, 5),
            sequenceOf(1, 2, 3, 4, 5, 6),
            sequenceOf(1, 2, 3, 4, 5, 6, 7),
            sequenceOf(1, 2, 3, 4, 5, 6, 7, 8)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3)
        )
    }

    @Test
    fun maxSize_meansTheSequenceMustHaveAtMostTheGivenNumberOfElements() {
        constraint<Sequence<Int>> {
            maxSize(4)
        }.allMatch(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3),
            sequenceOf(1, 2, 3, 4)
        ).allFail(
            sequenceOf(1, 2, 3, 4, 5),
            sequenceOf(1, 2, 3, 4, 5, 6),
            sequenceOf(1, 2, 3, 4, 5, 6, 7),
            sequenceOf(1, 2, 3, 4, 5, 6, 7, 8)
        )
    }

    @Test
    fun maxSizeL_meansTheSequenceMustHaveAtMostTheGivenNumberOfElements() {
        constraint<Sequence<Int>> {
            maxSize { 4 }
        }.allMatch(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3),
            sequenceOf(1, 2, 3, 4)
        ).allFail(
            sequenceOf(1, 2, 3, 4, 5),
            sequenceOf(1, 2, 3, 4, 5, 6),
            sequenceOf(1, 2, 3, 4, 5, 6, 7),
            sequenceOf(1, 2, 3, 4, 5, 6, 7, 8)
        )
    }

    @Test
    fun sizeLessThan_meansTheSequenceMustHaveLessThanTheGivenNumberOfElements() {
        constraint<Sequence<Int>> {
            sizeLessThan(4)
        }.allMatch(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3)
        ).allFail(
            sequenceOf(1, 2, 3, 4),
            sequenceOf(1, 2, 3, 4, 5),
            sequenceOf(1, 2, 3, 4, 5, 6),
            sequenceOf(1, 2, 3, 4, 5, 6, 7),
            sequenceOf(1, 2, 3, 4, 5, 6, 7, 8)
        )
    }

    @Test
    fun sizeLessThanL_meansTheSequenceMustHaveLessThanTheGivenNumberOfElements() {
        constraint<Sequence<Int>> {
            sizeLessThan { 4 }
        }.allMatch(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3)
        ).allFail(
            sequenceOf(1, 2, 3, 4),
            sequenceOf(1, 2, 3, 4, 5),
            sequenceOf(1, 2, 3, 4, 5, 6),
            sequenceOf(1, 2, 3, 4, 5, 6, 7),
            sequenceOf(1, 2, 3, 4, 5, 6, 7, 8)
        )
    }

    @Test
    fun sizeLargerThan_meansTheSequenceMustHaveMoreThanTheGivenNumberOfElements() {
        constraint<Sequence<Int>> {
            sizeLargerThan(4)
        }.allMatch(
            sequenceOf(1, 2, 3, 4, 5),
            sequenceOf(1, 2, 3, 4, 5, 6),
            sequenceOf(1, 2, 3, 4, 5, 6, 7),
            sequenceOf(1, 2, 3, 4, 5, 6, 7, 8)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3),
            sequenceOf(1, 2, 3, 4)
        )
    }

    @Test
    fun sizeLargerThanL_meansTheSequenceMustHaveMoreThanTheGivenNumberOfElements() {
        constraint<Sequence<Int>> {
            sizeLargerThan { 4 }
        }.allMatch(
            sequenceOf(1, 2, 3, 4, 5),
            sequenceOf(1, 2, 3, 4, 5, 6),
            sequenceOf(1, 2, 3, 4, 5, 6, 7),
            sequenceOf(1, 2, 3, 4, 5, 6, 7, 8)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3),
            sequenceOf(1, 2, 3, 4)
        )
    }

    @Test
    fun sizeIn_meansTheSequenceMustHaveTheGivenRangeOfElements() {
        constraint<Sequence<Int>> {
            sizeIn(3, 5)
        }.allMatch(
            sequenceOf(1, 2, 3),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(1, 2, 3, 4, 5)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4, 5, 6),
            sequenceOf(1, 2, 3, 4, 5, 6, 7),
            sequenceOf(1, 2, 3, 4, 5, 6, 7, 8)
        )
    }

    @Test
    fun sizeInR_meansTheSequenceMustHaveTheGivenRangeOfElements() {
        constraint<Sequence<Int>> {
            sizeIn(3..5)
        }.allMatch(
            sequenceOf(1, 2, 3),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(1, 2, 3, 4, 5)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4, 5, 6),
            sequenceOf(1, 2, 3, 4, 5, 6, 7),
            sequenceOf(1, 2, 3, 4, 5, 6, 7, 8)
        )
    }

    @Test
    fun sizeInRL_meansTheSequenceMustHaveTheGivenRangeOfElements() {
        constraint<Sequence<Int>> {
            sizeIn { 3..5 }
        }.allMatch(
            sequenceOf(1, 2, 3),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(1, 2, 3, 4, 5)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4, 5, 6),
            sequenceOf(1, 2, 3, 4, 5, 6, 7),
            sequenceOf(1, 2, 3, 4, 5, 6, 7, 8)
        )
    }

    @Test
    fun sizeNotIn_meansTheSequenceMustNotHaveTheGivenRangeOfElements() {
        constraint<Sequence<Int>> {
            sizeNotIn(3, 5)
        }.allMatch(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4, 5, 6),
            sequenceOf(1, 2, 3, 4, 5, 6, 7),
            sequenceOf(1, 2, 3, 4, 5, 6, 7, 8)
        ).allFail(
            sequenceOf(1, 2, 3),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(1, 2, 3, 4, 5)
        )
    }

    @Test
    fun sizeNotInR_meansTheSequenceMustNotHaveTheGivenRangeOfElements() {
        constraint<Sequence<Int>> {
            sizeNotIn(3..5)
        }.allMatch(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4, 5, 6),
            sequenceOf(1, 2, 3, 4, 5, 6, 7),
            sequenceOf(1, 2, 3, 4, 5, 6, 7, 8)
        ).allFail(
            sequenceOf(1, 2, 3),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(1, 2, 3, 4, 5)
        )
    }

    @Test
    fun sizeNotInRL_meansTheSequenceMustNotHaveTheGivenRangeOfElements() {
        constraint<Sequence<Int>> {
            sizeNotIn { 3..5 }
        }.allMatch(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4, 5, 6),
            sequenceOf(1, 2, 3, 4, 5, 6, 7),
            sequenceOf(1, 2, 3, 4, 5, 6, 7, 8)
        ).allFail(
            sequenceOf(1, 2, 3),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(1, 2, 3, 4, 5)
        )
    }

    @Test
    fun sizeEqualTo_meansTheSequenceMustHaveTheGivenNumberOfElements() {
        constraint<Sequence<Int>> {
            sizeEqualTo(3)
        }.allMatch(
            sequenceOf(1, 2, 3)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(1, 2, 3, 4, 5)
        )
    }

    @Test
    fun sizeEqualToL_meansTheSequenceMustHaveTheGivenNumberOfElements() {
        constraint<Sequence<Int>> {
            sizeEqualTo { 3 }
        }.allMatch(
            sequenceOf(1, 2, 3)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(1, 2, 3, 4, 5)
        )
    }

    @Test
    fun sizeNotEqualTo_meansTheSequenceMustNotHaveTheGivenNumberOfElements() {
        constraint<Sequence<Int>> {
            sizeNotEqualTo(3)
        }.allMatch(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(1, 2, 3, 4, 5)
        ).allFail(
            sequenceOf(1, 2, 3)
        )
    }

    @Test
    fun sizeNotEqualToL_meansTheSequenceMustNotHaveTheGivenNumberOfElements() {
        constraint<Sequence<Int>> {
            sizeNotEqualTo { 3 }
        }.allMatch(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(1, 2, 3, 4, 5)
        ).allFail(
            sequenceOf(1, 2, 3)
        )
    }

    @Test
    fun contains_meansTheSequenceMustContainTheGivenElement() {
        constraint<Sequence<Int>> {
            contains(3)
        }.allMatch(
            sequenceOf(3),
            sequenceOf(1, 3),
            sequenceOf(3, 1, 2, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 5)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 4),
            sequenceOf(1, 2, 4, 5)
        )
    }

    @Test
    fun containsL_meansTheSequenceMustContainTheGivenElement() {
        constraint<Sequence<Int>> {
            contains { 3 }
        }.allMatch(
            sequenceOf(3),
            sequenceOf(1, 3),
            sequenceOf(3, 1, 2, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 5)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 4),
            sequenceOf(1, 2, 4, 5)
        )
    }

    @Test
    fun doesNotContain_meansTheSequenceMustContainTheGivenElement() {
        constraint<Sequence<Int>> {
            doesNotContain(3)
        }.allMatch(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 4),
            sequenceOf(1, 2, 4, 5)
        ).allFail(
            sequenceOf(3),
            sequenceOf(1, 3),
            sequenceOf(3, 1, 2, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 5)
        )
    }

    @Test
    fun doesNotContainL_meansTheSequenceMustContainTheGivenElement() {
        constraint<Sequence<Int>> {
            doesNotContain { 3 }
        }.allMatch(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 4),
            sequenceOf(1, 2, 4, 5)
        ).allFail(
            sequenceOf(3),
            sequenceOf(1, 3),
            sequenceOf(3, 1, 2, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 5)
        )
    }

    @Test
    fun containsAt_meansTheSequenceMustContainTheGivenElementAtTheGivenPosition() {
        constraint<Sequence<Int>> {
            containsAt(1, 8)
        }.allMatch(
            sequenceOf(8, 8),
            sequenceOf(1, 8),
            sequenceOf(3, 8, 2, 4),
            sequenceOf(3, 8, 2, 4),
            sequenceOf(1, 8, 3, 4, 5)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(8),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 8),
            sequenceOf(8, 2, 8),
            sequenceOf(8, 2, 8, 8)
        )
    }

    @Test
    fun containsAtL_meansTheSequenceMustContainTheGivenElementAtTheGivenPosition() {
        constraint<Sequence<Int>> {
            containsAt(1) { 8 }
        }.allMatch(
            sequenceOf(8, 8),
            sequenceOf(1, 8),
            sequenceOf(3, 8, 2, 4),
            sequenceOf(3, 8, 2, 4),
            sequenceOf(1, 8, 3, 4, 5)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(8),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 8),
            sequenceOf(8, 2, 8),
            sequenceOf(8, 2, 8, 8)
        )
    }

    @Test
    fun startsWith_meansTheSequenceMustStartWithTheGivenElement() {
        constraint<Sequence<Int>> {
            startsWith(8)
        }.allMatch(
            sequenceOf(8),
            sequenceOf(8, 1),
            sequenceOf(8, 8, 2, 4),
            sequenceOf(8, 8, 2, 4),
            sequenceOf(8, 8, 3, 4, 5)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 8),
            sequenceOf(1, 8, 8),
            sequenceOf(1, 8, 3),
            sequenceOf(1, 8, 3, 8)
        )
    }

    @Test
    fun startsWithL_meansTheSequenceMustStartWithTheGivenElement() {
        constraint<Sequence<Int>> {
            startsWith { 8 }
        }.allMatch(
            sequenceOf(8),
            sequenceOf(8, 1),
            sequenceOf(8, 8, 2, 4),
            sequenceOf(8, 8, 2, 4),
            sequenceOf(8, 8, 3, 4, 5)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 8),
            sequenceOf(1, 8, 8),
            sequenceOf(1, 8, 3),
            sequenceOf(1, 8, 3, 8)
        )
    }

    @Test
    fun endsWith_meansTheSequenceMustEndWithTheGivenElement() {
        constraint<Sequence<Int>> {
            endsWith(8)
        }.allMatch(
            sequenceOf(8),
            sequenceOf(8, 8),
            sequenceOf(2, 8, 2, 8),
            sequenceOf(2, 8, 2, 8),
            sequenceOf(2, 8, 3, 4, 8)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(8, 7),
            sequenceOf(5, 8, 5),
            sequenceOf(1, 8, 3),
            sequenceOf(8, 8, 8, 7)
        )
    }

    @Test
    fun endsWithL_meansTheSequenceMustEndWithTheGivenElement() {
        constraint<Sequence<Int>> {
            endsWith { 8 }
        }.allMatch(
            sequenceOf(8),
            sequenceOf(8, 8),
            sequenceOf(2, 8, 2, 8),
            sequenceOf(2, 8, 2, 8),
            sequenceOf(2, 8, 3, 4, 8)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(8, 7),
            sequenceOf(5, 8, 5),
            sequenceOf(1, 8, 3),
            sequenceOf(8, 8, 8, 7)
        )
    }

    @Test
    fun containsAll_meansTheSequenceMustContainAllTheGivenElements() {
        constraint<Sequence<Int>> {
            containsAll(2, 3)
        }.allMatch(
            sequenceOf(2, 3),
            sequenceOf(3, 2),
            sequenceOf(3, 1, 2, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 5)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 4),
            sequenceOf(1, 2, 4, 5)
        )
    }

    @Test
    fun containsAllS_meansTheSequenceMustContainAllTheGivenElements() {
        constraint<Sequence<Int>> {
            containsAll(sequenceOf(2, 3))
        }.allMatch(
            sequenceOf(2, 3),
            sequenceOf(3, 2),
            sequenceOf(3, 1, 2, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 5)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 4),
            sequenceOf(1, 2, 4, 5)
        )
    }

    @Test
    fun containsAllSL_meansTheSequenceMustContainAllTheGivenElements() {
        constraint<Sequence<Int>> {
            containsAll { sequenceOf(2, 3) }
        }.allMatch(
            sequenceOf(2, 3),
            sequenceOf(3, 2),
            sequenceOf(3, 1, 2, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 5)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 4),
            sequenceOf(1, 2, 4, 5)
        )
    }

    @Test
    fun isPartOf_meansAllTheElementsOfTheSequenceMustExistInTheGivenElements() {
        constraint<Sequence<Int>> {
            isPartOf(1, 2, 3, 4, 5)
        }.allMatch(
            emptySequence(),
            sequenceOf(3),
            sequenceOf(2, 3),
            sequenceOf(3, 2),
            sequenceOf(3, 1, 2, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 4, 3, 5)
        ).allFail(
            sequenceOf(6),
            sequenceOf(6, 1),
            sequenceOf(7, 3, 9),
            sequenceOf(4, 9, 1, 2)
        )
    }

    @Test
    fun isPartOfS_meansAllTheElementsOfTheSequenceMustExistInTheGivenElements() {
        constraint<Sequence<Int>> {
            isPartOf(sequenceOf(1, 2, 3, 4, 5))
        }.allMatch(
            emptySequence(),
            sequenceOf(3),
            sequenceOf(2, 3),
            sequenceOf(3, 2),
            sequenceOf(3, 1, 2, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 4, 3, 5)
        ).allFail(
            sequenceOf(6),
            sequenceOf(6, 1),
            sequenceOf(7, 3, 9),
            sequenceOf(4, 9, 1, 2)
        )
    }

    @Test
    fun isPartOfSL_meansAllTheElementsOfTheSequenceMustExistInTheGivenElements() {
        constraint<Sequence<Int>> {
            isPartOf { sequenceOf(1, 2, 3, 4, 5) }
        }.allMatch(
            emptySequence(),
            sequenceOf(3),
            sequenceOf(2, 3),
            sequenceOf(3, 2),
            sequenceOf(3, 1, 2, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 4, 3, 5)
        ).allFail(
            sequenceOf(6),
            sequenceOf(6, 1),
            sequenceOf(7, 3, 9),
            sequenceOf(4, 9, 1, 2)
        )
    }

    @Test
    fun allTrue_meansAllTheBooleansInTheSequenceAreTrue() {
        constraint<Sequence<Boolean>> {
            allTrue()
        }.allMatch(
            emptySequence(),
            sequenceOf(true),
            sequenceOf(true, true),
            sequenceOf(true, true, true)
        ).allFail(
            sequenceOf(false),
            sequenceOf(false, false),
            sequenceOf(false, true),
            sequenceOf(false, false, false),
            sequenceOf(true, false, false),
            sequenceOf(false, true, false),
            sequenceOf(false, false, true),
            sequenceOf(false, true, true)
        )
    }

    @Test
    fun anyTrue_meansAtLeastOneOfTheBooleansInTheSequenceIsTrue() {
        constraint<Sequence<Boolean>> {
            anyTrue()
        }.allMatch(
            sequenceOf(true),
            sequenceOf(true, true),
            sequenceOf(true, false),
            sequenceOf(false, true),
            sequenceOf(true, true, true),
            sequenceOf(true, false, false),
            sequenceOf(false, true, false),
            sequenceOf(false, false, true),
            sequenceOf(false, true, true)
        ).allFail(
            emptySequence(),
            sequenceOf(false),
            sequenceOf(false, false),
            sequenceOf(false, false, false)
        )
    }

    @Test
    fun anyFalse_meansAtLeastOneOfTheBooleansInTheSequenceIsFalse() {
        constraint<Sequence<Boolean>> {
            anyFalse()
        }.allMatch(
            sequenceOf(false),
            sequenceOf(false, false),
            sequenceOf(false, true),
            sequenceOf(true, false),
            sequenceOf(false, false, false),
            sequenceOf(false, true, true),
            sequenceOf(true, false, true),
            sequenceOf(true, true, false),
            sequenceOf(true, false, false)
        ).allFail(
            emptySequence(),
            sequenceOf(true),
            sequenceOf(true, true),
            sequenceOf(true, true, true)
        )
    }

    @Test
    fun allFalse_meansAllOfTheBooleansInTheSequenceAreFalse() {
        constraint<Sequence<Boolean>> {
            allFalse()
        }.allMatch(
            emptySequence(),
            sequenceOf(false),
            sequenceOf(false, false),
            sequenceOf(false, false, false)
        ).allFail(
            sequenceOf(true),
            sequenceOf(true, true),
            sequenceOf(true, false),
            sequenceOf(true, true, true),
            sequenceOf(false, true, true),
            sequenceOf(true, false, true),
            sequenceOf(true, true, false),
            sequenceOf(true, false, false)
        )
    }

    @Test
    fun contentEquals_meansAllTheElementsOfTheSequenceMustExistInTheGivenElements() {

        constraint<Sequence<Int>> {
            contentEquals(
                ignoreDuplicates = false,
                ignoreOrder = false,
                other = sequenceOf(1, 2, 3, 4, 5)
            )
        }.allMatch(
            sequenceOf(1, 2, 3, 4, 5)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 4, 5),
            sequenceOf(1, 2, 3, 4, 4),
            sequenceOf(1, 2, 3, 5, 4),
            sequenceOf(1, 2, 3, 5, 4, 5),
            sequenceOf(5, 4, 3, 2, 1)
        )

        constraint<Sequence<Int>> {
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = false,
                other = sequenceOf(1, 2, 3, 4, 5)
            )
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = false,
                other = sequenceOf(1, 2, 3, 3, 4, 5, 5, 5)
            )
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = false,
                other = sequenceOf(1, 2, 1, 3, 1, 3, 1, 4, 1, 5)
            )
        }.allMatch(
            sequenceOf(1, 2, 3, 4, 5),
            sequenceOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            sequenceOf(1, 2, 2, 3, 4, 4, 5)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 5, 6),
            sequenceOf(1, 2, 3, 4, 4),
            sequenceOf(1, 2, 3, 5, 4),
            sequenceOf(5, 4, 3, 2, 1)
        )

        constraint<Sequence<Int>> {
            contentEquals(
                ignoreDuplicates = false,
                ignoreOrder = true,
                other = sequenceOf(1, 2, 3, 4, 5)
            )
        }.allMatch(
            sequenceOf(1, 2, 3, 4, 5),
            sequenceOf(2, 1, 5, 4, 3),
            sequenceOf(1, 2, 3, 5, 4),
            sequenceOf(5, 4, 3, 2, 1)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 4, 5),
            sequenceOf(1, 2, 3, 4, 4),
            sequenceOf(1, 2, 3, 5, 4, 5)
        )

        constraint<Sequence<Int>> {
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = true,
                other = sequenceOf(1, 2, 3, 4, 5)
            )
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = true,
                other = sequenceOf(3, 2, 1, 3, 3, 4, 5, 5, 5)
            )
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = true,
                other = sequenceOf(2, 1, 3, 1, 3, 1, 4, 1, 5)
            )
        }.allMatch(
            sequenceOf(1, 2, 3, 4, 5),
            sequenceOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            sequenceOf(1, 2, 2, 3, 4, 4, 5),
            sequenceOf(5, 4, 3, 2, 1),
            sequenceOf(3, 2, 2, 3, 1, 4, 4, 5, 5, 5, 5),
            sequenceOf(5, 2, 2, 3, 1, 4, 4, 5)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 5, 6),
            sequenceOf(1, 2, 3, 4, 4)
        )
    }

    @Test
    fun contentEqualsL_meansAllTheElementsOfTheSequenceMustExistInTheGivenElements() {

        constraint<Sequence<Int>> {
            contentEquals(
                ignoreDuplicates = false,
                ignoreOrder = false
            ) { sequenceOf(1, 2, 3, 4, 5) }
        }.allMatch(
            sequenceOf(1, 2, 3, 4, 5)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 4, 5),
            sequenceOf(1, 2, 3, 4, 4),
            sequenceOf(1, 2, 3, 5, 4),
            sequenceOf(1, 2, 3, 5, 4, 5),
            sequenceOf(5, 4, 3, 2, 1)
        )

        constraint<Sequence<Int>> {
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = false
            ) { sequenceOf(1, 2, 3, 4, 5) }
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = false
            ) { sequenceOf(1, 2, 3, 3, 4, 5, 5, 5) }
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = false
            ) { sequenceOf(1, 2, 1, 3, 1, 3, 1, 4, 1, 5) }
        }.allMatch(
            sequenceOf(1, 2, 3, 4, 5),
            sequenceOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            sequenceOf(1, 2, 2, 3, 4, 4, 5)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 5, 6),
            sequenceOf(1, 2, 3, 4, 4),
            sequenceOf(1, 2, 3, 5, 4),
            sequenceOf(5, 4, 3, 2, 1)
        )

        constraint<Sequence<Int>> {
            contentEquals(
                ignoreDuplicates = false,
                ignoreOrder = true
            ) { sequenceOf(1, 2, 3, 4, 5) }
        }.allMatch(
            sequenceOf(1, 2, 3, 4, 5),
            sequenceOf(2, 1, 5, 4, 3),
            sequenceOf(1, 2, 3, 5, 4),
            sequenceOf(5, 4, 3, 2, 1)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 4, 5),
            sequenceOf(1, 2, 3, 4, 4),
            sequenceOf(1, 2, 3, 5, 4, 5)
        )

        constraint<Sequence<Int>> {
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = true
            ) { sequenceOf(1, 2, 3, 4, 5) }
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = true
            ) { sequenceOf(3, 2, 1, 3, 3, 4, 5, 5, 5) }
            contentEquals(
                ignoreDuplicates = true,
                ignoreOrder = true
            ) { sequenceOf(2, 1, 3, 1, 3, 1, 4, 1, 5) }
        }.allMatch(
            sequenceOf(1, 2, 3, 4, 5),
            sequenceOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            sequenceOf(1, 2, 2, 3, 4, 4, 5),
            sequenceOf(5, 4, 3, 2, 1),
            sequenceOf(3, 2, 2, 3, 1, 4, 4, 5, 5, 5, 5),
            sequenceOf(5, 2, 2, 3, 1, 4, 4, 5)
        ).allFail(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 5, 6),
            sequenceOf(1, 2, 3, 4, 4)
        )
    }

    @Test
    fun contentNotEquals_meansAllTheElementsOfTheSequenceMustNotEqualTheGivenElements() {

        constraint<Sequence<Int>> {
            contentNotEquals(
                ignoreDuplicates = false,
                ignoreOrder = false,
                other = sequenceOf(1, 2, 3, 4, 5)
            )
        }.allMatch(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 4, 5),
            sequenceOf(1, 2, 3, 4, 4),
            sequenceOf(1, 2, 3, 5, 4),
            sequenceOf(1, 2, 3, 5, 4, 5),
            sequenceOf(5, 4, 3, 2, 1)
        ).allFail(
            sequenceOf(1, 2, 3, 4, 5)
        )

        constraint<Sequence<Int>> {
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = false,
                other = sequenceOf(1, 2, 3, 4, 5)
            )
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = false,
                other = sequenceOf(1, 2, 3, 3, 4, 5, 5, 5)
            )
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = false,
                other = sequenceOf(1, 2, 1, 3, 1, 3, 1, 4, 1, 5)
            )
        }.allMatch(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 5, 6),
            sequenceOf(1, 2, 3, 4, 4),
            sequenceOf(1, 2, 3, 5, 4),
            sequenceOf(5, 4, 3, 2, 1)
        ).allFail(
            sequenceOf(1, 2, 3, 4, 5),
            sequenceOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            sequenceOf(1, 2, 2, 3, 4, 4, 5)
        )

        constraint<Sequence<Int>> {
            contentNotEquals(
                ignoreDuplicates = false,
                ignoreOrder = true,
                other = sequenceOf(1, 2, 3, 4, 5)
            )
        }.allMatch(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 4, 5),
            sequenceOf(1, 2, 3, 4, 4),
            sequenceOf(1, 2, 3, 5, 4, 5)
        ).allFail(
            sequenceOf(1, 2, 3, 4, 5),
            sequenceOf(2, 1, 5, 4, 3),
            sequenceOf(1, 2, 3, 5, 4),
            sequenceOf(5, 4, 3, 2, 1)
        )

        constraint<Sequence<Int>> {
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = true,
                other = sequenceOf(1, 2, 3, 4, 5)
            )
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = true,
                other = sequenceOf(3, 2, 1, 3, 3, 4, 5, 5, 5)
            )
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = true,
                other = sequenceOf(2, 1, 3, 1, 3, 1, 4, 1, 5)
            )
        }.allMatch(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 5, 6),
            sequenceOf(1, 2, 3, 4, 4)
        ).allFail(
            sequenceOf(1, 2, 3, 4, 5),
            sequenceOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            sequenceOf(1, 2, 2, 3, 4, 4, 5),
            sequenceOf(5, 4, 3, 2, 1),
            sequenceOf(3, 2, 2, 3, 1, 4, 4, 5, 5, 5, 5),
            sequenceOf(5, 2, 2, 3, 1, 4, 4, 5)
        )
    }

    @Test
    fun contentNotEqualsL_meansAllTheElementsOfTheSequenceMustNotEqualTheGivenElements() {

        constraint<Sequence<Int>> {
            contentNotEquals(
                ignoreDuplicates = false,
                ignoreOrder = false
            ) { sequenceOf(1, 2, 3, 4, 5) }
        }.allMatch(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 4, 5),
            sequenceOf(1, 2, 3, 4, 4),
            sequenceOf(1, 2, 3, 5, 4),
            sequenceOf(1, 2, 3, 5, 4, 5),
            sequenceOf(5, 4, 3, 2, 1)
        ).allFail(
            sequenceOf(1, 2, 3, 4, 5)
        )

        constraint<Sequence<Int>> {
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = false
            ) { sequenceOf(1, 2, 3, 4, 5) }
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = false
            ) { sequenceOf(1, 2, 3, 3, 4, 5, 5, 5) }
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = false
            ) { sequenceOf(1, 2, 1, 3, 1, 3, 1, 4, 1, 5) }
        }.allMatch(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 5, 6),
            sequenceOf(1, 2, 3, 4, 4),
            sequenceOf(1, 2, 3, 5, 4),
            sequenceOf(5, 4, 3, 2, 1)
        ).allFail(
            sequenceOf(1, 2, 3, 4, 5),
            sequenceOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            sequenceOf(1, 2, 2, 3, 4, 4, 5)
        )

        constraint<Sequence<Int>> {
            contentNotEquals(
                ignoreDuplicates = false,
                ignoreOrder = true
            ) { sequenceOf(1, 2, 3, 4, 5) }
        }.allMatch(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 4, 5),
            sequenceOf(1, 2, 3, 4, 4),
            sequenceOf(1, 2, 3, 5, 4, 5)
        ).allFail(
            sequenceOf(1, 2, 3, 4, 5),
            sequenceOf(2, 1, 5, 4, 3),
            sequenceOf(1, 2, 3, 5, 4),
            sequenceOf(5, 4, 3, 2, 1)
        )

        constraint<Sequence<Int>> {
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = true
            ) { sequenceOf(1, 2, 3, 4, 5) }
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = true
            ) { sequenceOf(3, 2, 1, 3, 3, 4, 5, 5, 5) }
            contentNotEquals(
                ignoreDuplicates = true,
                ignoreOrder = true
            ) { sequenceOf(2, 1, 3, 1, 3, 1, 4, 1, 5) }
        }.allMatch(
            emptySequence(),
            sequenceOf(1),
            sequenceOf(1, 2),
            sequenceOf(1, 2, 3, 4),
            sequenceOf(3, 3, 2, 4),
            sequenceOf(1, 2, 3, 4, 5, 6),
            sequenceOf(1, 2, 3, 4, 4)
        ).allFail(
            sequenceOf(1, 2, 3, 4, 5),
            sequenceOf(1, 2, 2, 3, 4, 4, 5, 5, 5, 5),
            sequenceOf(1, 2, 2, 3, 4, 4, 5),
            sequenceOf(5, 4, 3, 2, 1),
            sequenceOf(3, 2, 2, 3, 1, 4, 4, 5, 5, 5, 5),
            sequenceOf(5, 2, 2, 3, 1, 4, 4, 5)
        )
    }
}
