package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.utils.allFail
import dev.ahmedmourad.validation.core.utils.allMatch
import dev.ahmedmourad.validation.core.utils.constraint
import dev.ahmedmourad.validation.core.validations.*
import kotlin.test.Test

class MapValidationsTests {

    @Test
    fun isEmpty_meansTheMapIsEmpty() {
        constraint<Map<Int, Unit>> {
            isEmpty()
        }.allMatch(
            emptyMap()
        ).allFail(
            mapOf(1 to Unit),
            mapOf(1 to Unit, 2 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit)
        )
    }

    @Test
    fun isNotEmpty_meansTheMapIsNotEmpty() {
        constraint<Map<Int, Unit>> {
            isNotEmpty()
        }.allMatch(
            mapOf(1 to Unit),
            mapOf(1 to Unit, 2 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit)
        ).allFail(
            emptyMap()
        )
    }

    @Test
    fun minSize_meansTheMapMustHaveAtLeastTheGivenNumberOfElements() {
        constraint<Map<Int, Unit>> {
            minSize(4)
        }.allMatch(
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit, 8 to Unit)
        ).allFail(
            emptyMap(),
            mapOf(1 to Unit),
            mapOf(1 to Unit, 2 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit)
        )
    }

    @Test
    fun minSizeL_meansTheMapMustHaveAtLeastTheGivenNumberOfElements() {
        constraint<Map<Int, Unit>> {
            minSize { 4 }
        }.allMatch(
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit, 8 to Unit)
        ).allFail(
            emptyMap(),
            mapOf(1 to Unit),
            mapOf(1 to Unit, 2 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit)
        )
    }

    @Test
    fun maxSize_meansTheMapMustHaveAtMostTheGivenNumberOfElements() {
        constraint<Map<Int, Unit>> {
            maxSize(4)
        }.allMatch(
            emptyMap(),
            mapOf(1 to Unit),
            mapOf(1 to Unit, 2 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit)
        ).allFail(
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit, 8 to Unit)
        )
    }

    @Test
    fun maxSizeL_meansTheMapMustHaveAtMostTheGivenNumberOfElements() {
        constraint<Map<Int, Unit>> {
            maxSize { 4 }
        }.allMatch(
            emptyMap(),
            mapOf(1 to Unit),
            mapOf(1 to Unit, 2 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit)
        ).allFail(
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit, 8 to Unit)
        )
    }

    @Test
    fun sizeLessThan_meansTheMapMustHaveLessThanTheGivenNumberOfElements() {
        constraint<Map<Int, Unit>> {
            sizeLessThan(4)
        }.allMatch(
            emptyMap(),
            mapOf(1 to Unit),
            mapOf(1 to Unit, 2 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit)
        ).allFail(
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit, 8 to Unit)
        )
    }

    @Test
    fun sizeLessThanL_meansTheMapMustHaveLessThanTheGivenNumberOfElements() {
        constraint<Map<Int, Unit>> {
            sizeLessThan { 4 }
        }.allMatch(
            emptyMap(),
            mapOf(1 to Unit),
            mapOf(1 to Unit, 2 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit)
        ).allFail(
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit, 8 to Unit)
        )
    }

    @Test
    fun sizeLargerThan_meansTheMapMustHaveMoreThanTheGivenNumberOfElements() {
        constraint<Map<Int, Unit>> {
            sizeLargerThan(4)
        }.allMatch(
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit, 8 to Unit)
        ).allFail(
            emptyMap(),
            mapOf(1 to Unit),
            mapOf(1 to Unit, 2 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit)
        )
    }

    @Test
    fun sizeLargerThanL_meansTheMapMustHaveMoreThanTheGivenNumberOfElements() {
        constraint<Map<Int, Unit>> {
            sizeLargerThan { 4 }
        }.allMatch(
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit, 8 to Unit)
        ).allFail(
            emptyMap(),
            mapOf(1 to Unit),
            mapOf(1 to Unit, 2 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit)
        )
    }

    @Test
    fun sizeIn_meansTheMapMustHaveTheGivenRangeOfElements() {
        constraint<Map<Int, Unit>> {
            sizeIn(3, 5)
        }.allMatch(
            mapOf(1 to Unit, 2 to Unit, 3 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit)
        ).allFail(
            emptyMap(),
            mapOf(1 to Unit),
            mapOf(1 to Unit, 2 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit, 8 to Unit)
        )
    }

    @Test
    fun sizeInR_meansTheMapMustHaveTheGivenRangeOfElements() {
        constraint<Map<Int, Unit>> {
            sizeIn(3..5)
        }.allMatch(
            mapOf(1 to Unit, 2 to Unit, 3 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit)
        ).allFail(
            emptyMap(),
            mapOf(1 to Unit),
            mapOf(1 to Unit, 2 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit, 8 to Unit)
        )
    }

    @Test
    fun sizeInRL_meansTheMapMustHaveTheGivenRangeOfElements() {
        constraint<Map<Int, Unit>> {
            sizeIn { 3..5 }
        }.allMatch(
            mapOf(1 to Unit, 2 to Unit, 3 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit)
        ).allFail(
            emptyMap(),
            mapOf(1 to Unit),
            mapOf(1 to Unit, 2 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit, 8 to Unit)
        )
    }

    @Test
    fun sizeNotIn_meansTheMapMustNotHaveTheGivenRangeOfElements() {
        constraint<Map<Int, Unit>> {
            sizeNotIn(3, 5)
        }.allMatch(
            emptyMap(),
            mapOf(1 to Unit),
            mapOf(1 to Unit, 2 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit, 8 to Unit)
        ).allFail(
            mapOf(1 to Unit, 2 to Unit, 3 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit)
        )
    }

    @Test
    fun sizeNotInR_meansTheMapMustNotHaveTheGivenRangeOfElements() {
        constraint<Map<Int, Unit>> {
            sizeNotIn(3..5)
        }.allMatch(
            emptyMap(),
            mapOf(1 to Unit),
            mapOf(1 to Unit, 2 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit, 8 to Unit)
        ).allFail(
            mapOf(1 to Unit, 2 to Unit, 3 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit)
        )
    }

    @Test
    fun sizeNotInRL_meansTheMapMustNotHaveTheGivenRangeOfElements() {
        constraint<Map<Int, Unit>> {
            sizeNotIn { 3..5 }
        }.allMatch(
            emptyMap(),
            mapOf(1 to Unit),
            mapOf(1 to Unit, 2 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit, 6 to Unit, 7 to Unit, 8 to Unit)
        ).allFail(
            mapOf(1 to Unit, 2 to Unit, 3 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit)
        )
    }

    @Test
    fun sizeEqualTo_meansTheMapMustHaveTheGivenNumberOfElements() {
        constraint<Map<Int, Unit>> {
            sizeEqualTo(3)
        }.allMatch(
            mapOf(1 to Unit, 2 to Unit, 3 to Unit)
        ).allFail(
            emptyMap(),
            mapOf(1 to Unit),
            mapOf(1 to Unit, 2 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit)
        )
    }

    @Test
    fun sizeEqualToL_meansTheMapMustHaveTheGivenNumberOfElements() {
        constraint<Map<Int, Unit>> {
            sizeEqualTo { 3 }
        }.allMatch(
            mapOf(1 to Unit, 2 to Unit, 3 to Unit)
        ).allFail(
            emptyMap(),
            mapOf(1 to Unit),
            mapOf(1 to Unit, 2 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit)
        )
    }

    @Test
    fun sizeNotEqualTo_meansTheMapMustNotHaveTheGivenNumberOfElements() {
        constraint<Map<Int, Unit>> {
            sizeNotEqualTo(3)
        }.allMatch(
            emptyMap(),
            mapOf(1 to Unit),
            mapOf(1 to Unit, 2 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit)
        ).allFail(
            mapOf(1 to Unit, 2 to Unit, 3 to Unit)
        )
    }

    @Test
    fun sizeNotEqualToL_meansTheMapMustNotHaveTheGivenNumberOfElements() {
        constraint<Map<Int, Unit>> {
            sizeNotEqualTo { 3 }
        }.allMatch(
            emptyMap(),
            mapOf(1 to Unit),
            mapOf(1 to Unit, 2 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit)
        ).allFail(
            mapOf(1 to Unit, 2 to Unit, 3 to Unit)
        )
    }

    @Test
    fun keys_meansTheGivenValidationsMatchAllTheKeysSetOfTheMap() {
        constraint<Map<Int, Unit>> {
            keys {
                forAll {
                    inValues(1, 3, 4, 5)
                }
            }
        }.allMatch(
            emptyMap(),
            mapOf(1 to Unit),
            mapOf(1 to Unit, 3 to Unit),
            mapOf(1 to Unit, 3 to Unit, 4 to Unit),
            mapOf(1 to Unit, 3 to Unit, 4 to Unit, 5 to Unit)
        ).allFail(
            mapOf(2 to Unit),
            mapOf(1 to Unit, 2 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit),
            mapOf(1 to Unit, 2 to Unit, 3 to Unit, 4 to Unit, 5 to Unit)
        )
    }

    @Test
    fun values_meansTheGivenValidationsMatchAllTheValuesCollectionOfTheMap() {
        constraint<Map<Int, Int?>> {
            values {
                forAll {
                    inValues(null, 1, 3, 4, 5)
                }
            }
        }.allMatch(
            emptyMap(),
            mapOf(1 to 1),
            mapOf(1 to 1, 2 to null),
            mapOf(1 to 1, 2 to null, 3 to 3),
            mapOf(1 to 1, 2 to null, 3 to 3, 4 to 4),
            mapOf(1 to 1, 2 to null, 3 to 3, 4 to 4, 5 to 5)
        ).allFail(
            mapOf(2 to 2),
            mapOf(1 to 1, 2 to 2),
            mapOf(1 to 1, 2 to 2, 3 to 3),
            mapOf(1 to 1, 2 to 2, 3 to 3, 4 to 4),
            mapOf(1 to 1, 2 to 2, 3 to 3, 4 to 4, 5 to 5)
        )
    }

    @Test
    fun entries_meansTheGivenValidationsMatchAllTheEntriesSetOfTheMap() {
        constraint<Map<Int, Int?>> {
            entries {
                forAll {
                    validation { subject.key == subject.value }
                }
            }
        }.allMatch(
            emptyMap(),
            mapOf(1 to 1),
            mapOf(1 to 1, 2 to 2),
            mapOf(1 to 1, 2 to 2, 3 to 3),
            mapOf(1 to 1, 2 to 2, 3 to 3, 4 to 4),
            mapOf(1 to 1, 2 to 2, 3 to 3, 4 to 4, 5 to 5)
        ).allFail(
            mapOf(1 to null),
            mapOf(1 to null, 2 to null),
            mapOf(1 to null, 2 to null, 3 to null),
            mapOf(1 to null, 2 to null, 3 to null, 4 to null),
            mapOf(1 to null, 2 to null, 3 to null, 4 to null, 5 to null)
        )
    }

    @Test
    fun key_meansTheGivenValidationsMatchTheKeyOfTheMapEntry() {
        constraint<Map.Entry<Int, Unit>> {
            key {
                max(3)
            }
        }.allMatch(
            mapOf(-1 to Unit).entries.first(),
            mapOf(1 to Unit).entries.first(),
            mapOf(2 to Unit).entries.first(),
            mapOf(3 to Unit).entries.first()
        ).allFail(
            mapOf(4 to Unit).entries.first(),
            mapOf(5 to Unit).entries.first(),
            mapOf(6 to Unit).entries.first()
        )
    }

    @Test
    fun value_meansTheGivenValidationsMatchTheValueOfTheMapEntry() {
        constraint<Map.Entry<Unit, Int>> {
            value {
                max(3)
            }
        }.allMatch(
            mapOf(Unit to -1).entries.first(),
            mapOf(Unit to 1).entries.first(),
            mapOf(Unit to 2).entries.first(),
            mapOf(Unit to 3).entries.first()
        ).allFail(
            mapOf(Unit to 4).entries.first(),
            mapOf(Unit to 5).entries.first(),
            mapOf(Unit to 6).entries.first()
        )
    }
}
