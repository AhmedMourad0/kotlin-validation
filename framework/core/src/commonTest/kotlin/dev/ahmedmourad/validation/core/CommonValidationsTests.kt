package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.utils.allFail
import dev.ahmedmourad.validation.core.utils.allMatch
import dev.ahmedmourad.validation.core.utils.constraint
import dev.ahmedmourad.validation.core.validations.*
import kotlin.test.Test

class CommonValidationsTests {

    @Test
    fun isEqualTo_meansThisObjectIsEqualToTheGivenObject() {
        constraint<Int> {
            isEqualTo(5)
        }.allMatch(
            5
        ).allFail(
            1,
            2,
            3,
            4,
            6,
            7
        )
    }

    @Test
    fun isEqualToL_meansThisObjectIsEqualToTheGivenObject() {
        constraint<Int> {
            isEqualTo<Int> { 5 }
        }.allMatch(
            5
        ).allFail(
            1,
            2,
            3,
            4,
            6,
            7
        )
    }

    @Test
    fun isNotEqualTo_meansThisObjectIsNotEqualToTheGivenObject() {
        constraint<Int> {
            isNotEqualTo(5)
        }.allMatch(
            1,
            2,
            3,
            4,
            6,
            7
        ).allFail(
            5
        )
    }

    @Test
    fun isNotEqualToL_meansThisObjectIsNotEqualToTheGivenObject() {
        constraint<Int> {
            isNotEqualTo<Int> { 5 }
        }.allMatch(
            1,
            2,
            3,
            4,
            6,
            7
        ).allFail(
            5
        )
    }

    @Test
    fun inValues_meansThisObjectEqualsAnyOfTheGivenObjects() {
        constraint<Int> {
            inValues(3, 4, 5)
        }.allMatch(
            3,
            4,
            5
        ).allFail(
            1,
            2,
            6,
            7
        )
    }

    @Test
    fun inValuesL_meansThisObjectEqualsAnyOfTheGivenObjects() {
        constraint<Int> {
            inValues(listOf(3, 4, 5))
        }.allMatch(
            3,
            4,
            5
        ).allFail(
            1,
            2,
            6,
            7
        )
    }

    @Test
    fun inValuesLL_meansThisObjectEqualsAnyOfTheGivenObjects() {
        constraint<Int> {
            inValues { (listOf(3, 4, 5)) }
        }.allMatch(
            3,
            4,
            5
        ).allFail(
            1,
            2,
            6,
            7
        )
    }

    @Test
    fun notInValues_meansThisObjectDoesNotEqualAnyOfTheGivenObjects() {
        constraint<Int> {
            notInValues(3, 4, 5)
        }.allMatch(
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
    fun notInValuesL_meansThisObjectDoesNotEqualAnyOfTheGivenObjects() {
        constraint<Int> {
            notInValues(listOf(3, 4, 5))
        }.allMatch(
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
    fun notInValuesLL_meansThisObjectDoesNotEqualAnyOfTheGivenObjects() {
        constraint<Int> {
            notInValues { (listOf(3, 4, 5)) }
        }.allMatch(
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
    fun anyOf_meansTheGivenObjectMatchesAtLeastOneOfTheValidations() {
        constraint<Int> {
            anyOf {
                isEqualTo(1)
                isEqualTo(2)
                isEqualTo(3)
                isEqualTo(4)
            }
        }.allMatch(
            1,
            2,
            3,
            4
        ).allFail(
            0,
            5,
            6,
            7
        )
    }

    @Test
    fun allOf_meansTheGivenObjectMatchesAllTheValidations() {
        constraint<Int> {
            allOf {
                min(3)
                max(5)
            }
        }.allMatch(
            3,
            4,
            5
        ).allFail(
            0,
            1,
            2,
            6,
            7
        )
    }

    @Test
    fun noneOf_meansTheGivenObjectMatchesNoneOfTheValidations() {
        constraint<String> {
            noneOf {
                startsWith("a")
                endsWith("z")
                inValues("Ahmed", "Mourad")
            }
        }.allMatch(
            "Something",
            "That's",
            "Valid"
        ).allFail(
            "a",
            "anchor",
            "Ahmed",
            "Mourad",
            "Zebraz"
        )
    }
}
