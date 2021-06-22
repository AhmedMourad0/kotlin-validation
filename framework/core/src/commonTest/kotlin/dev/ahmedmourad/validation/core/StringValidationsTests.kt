package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.utils.allFail
import dev.ahmedmourad.validation.core.utils.allMatch
import dev.ahmedmourad.validation.core.utils.constraint
import dev.ahmedmourad.validation.core.validations.*
import kotlin.test.Test

class StringValidationsTests {

    @Test
    fun isNumber_meansThisStringQualifiesAsANumber() {
        constraint<String> {
            isNumber()
        }.allMatch(
            "1475",
            "0",
            "-0",
            "0000",
            "0.45",
            "34.355",
            "-0.45",
            "-178",
            "0.0",
            "1.0",
            Long.MAX_VALUE.toString(),
            Long.MIN_VALUE.toString(),
            Double.MAX_VALUE.toString(),
            Double.MIN_VALUE.toString(),
            Double.POSITIVE_INFINITY.toString(),
            Double.NEGATIVE_INFINITY.toString(),
            Double.NaN.toString()
        ).allFail(
            "",
            "a",
            "Ahmed",
            "Mourad"
        )
    }

    @Test
    fun isInteger_meansThisStringQualifiesAsAnInteger() {
        constraint<String> {
            isInteger()
        }.allMatch(
            "1475",
            "0",
            "-0",
            "0000",
            "-178",
            Long.MAX_VALUE.toString(),
            Long.MIN_VALUE.toString()
        ).allFail(
            "",
            "a",
            "Ahmed",
            "Mourad",
            "0.45",
            "34.355",
            "-0.45",
            "0.0",
            "1.0",
            Double.MAX_VALUE.toString(),
            Double.MIN_VALUE.toString(),
            Double.POSITIVE_INFINITY.toString(),
            Double.NEGATIVE_INFINITY.toString(),
            Double.NaN.toString()
        )
    }

    @Test
    fun isPositiveInteger_meansThisStringQualifiesAsAPositiveInteger() {

        constraint<String> {
            isPositiveInteger(true)
        }.allMatch(
            "1475",
            "242",
            "53",
            "2",
            "0",
            "-0",
            "0000",
            Long.MAX_VALUE.toString()
        ).allFail(
            "",
            "a",
            "Ahmed",
            "Mourad",
            "0.45",
            "34.355",
            "-0.45",
            "0.0",
            "1.0",
            Double.MAX_VALUE.toString(),
            Double.MIN_VALUE.toString(),
            Double.POSITIVE_INFINITY.toString(),
            Double.NEGATIVE_INFINITY.toString(),
            Double.NaN.toString(),
            Long.MIN_VALUE.toString(),
            "-178"
        )

        constraint<String> {
            isPositiveInteger(false)
        }.allMatch(
            "1475",
            "242",
            "53",
            "2",
            Long.MAX_VALUE.toString()
        ).allFail(
            "",
            "0",
            "-0",
            "0000",
            "a",
            "Ahmed",
            "Mourad",
            "0.45",
            "34.355",
            "-0.45",
            "0.0",
            "1.0",
            Double.MAX_VALUE.toString(),
            Double.MIN_VALUE.toString(),
            Double.POSITIVE_INFINITY.toString(),
            Double.NEGATIVE_INFINITY.toString(),
            Double.NaN.toString(),
            Long.MIN_VALUE.toString(),
            "-178"
        )
    }

    @Test
    fun isNegativeInteger_meansThisStringQualifiesAsANegativeInteger() {

        constraint<String> {
            isNegativeInteger(true)
        }.allMatch(
            "-1475",
            "-242",
            "-53",
            "-2",
            "0",
            "-0",
            "0000",
            Long.MIN_VALUE.toString()
        ).allFail(
            "",
            "a",
            "Ahmed",
            "Mourad",
            "0.45",
            "34.355",
            "-0.45",
            "0.0",
            "1.0",
            Double.MAX_VALUE.toString(),
            Double.MIN_VALUE.toString(),
            Double.POSITIVE_INFINITY.toString(),
            Double.NEGATIVE_INFINITY.toString(),
            Double.NaN.toString(),
            Long.MAX_VALUE.toString(),
            "1475",
            "242",
            "53",
            "2"
        )

        constraint<String> {
            isNegativeInteger(false)
        }.allMatch(
            "-1475",
            "-242",
            "-53",
            "-2",
            Long.MIN_VALUE.toString()
        ).allFail(
            "",
            "0",
            "-0",
            "0000",
            "a",
            "Ahmed",
            "Mourad",
            "0.45",
            "34.355",
            "-0.45",
            "0.0",
            "1.0",
            Double.MAX_VALUE.toString(),
            Double.MIN_VALUE.toString(),
            Double.POSITIVE_INFINITY.toString(),
            Double.NEGATIVE_INFINITY.toString(),
            Double.NaN.toString(),
            Long.MAX_VALUE.toString(),
            "1475",
            "242",
            "53",
            "2"
        )
    }

    @Test
    fun isPositiveNumber_meansThisStringQualifiesAsAPositiveNumber() {

        constraint<String> {
            isPositiveNumber(true)
        }.allMatch(
            "1475",
            "242",
            "53",
            "2",
            "0",
            "-0",
            "0000",
            "0.45",
            "34.355",
            "0.0",
            "1.0",
            Long.MAX_VALUE.toString(),
            Double.MAX_VALUE.toString(),
            Double.MIN_VALUE.toString(),
            Double.POSITIVE_INFINITY.toString()
        ).allFail(
            "",
            "a",
            "Ahmed",
            "Mourad",
            "-0.45",
            "-34.355",
            "-178",
            "-1475",
            "-242",
            "-53",
            "-2",
            Long.MIN_VALUE.toString(),
            Double.NEGATIVE_INFINITY.toString(),
            Double.NaN.toString()
        )

        constraint<String> {
            isPositiveNumber(false)
        }.allMatch(
            "1475",
            "242",
            "53",
            "2",
            "0.45",
            "34.355",
            "1.0",
            Long.MAX_VALUE.toString(),
            Double.MAX_VALUE.toString(),
            Double.MIN_VALUE.toString(),
            Double.POSITIVE_INFINITY.toString()
        ).allFail(
            "",
            "a",
            "Ahmed",
            "Mourad",
            "0",
            "-0",
            "0000",
            "0.0",
            "-0.45",
            "-34.355",
            "-178",
            "-1475",
            "-242",
            "-53",
            "-2",
            Long.MIN_VALUE.toString(),
            Double.NEGATIVE_INFINITY.toString(),
            Double.NaN.toString()
        )
    }

    @Test
    fun isNegativeNumber_meansThisStringQualifiesAsANegativeNumber() {

        constraint<String> {
            isNegativeNumber(true)
        }.allMatch(
            "-1475",
            "-242",
            "-53",
            "-2",
            "0",
            "-0",
            "0000",
            "-0.45",
            "-34.355",
            "0.0",
            "-1.0",
            Long.MIN_VALUE.toString(),
            Double.NEGATIVE_INFINITY.toString()
        ).allFail(
            "",
            "a",
            "Ahmed",
            "Mourad",
            "0.45",
            "34.355",
            "178",
            "1475",
            "242",
            "53",
            "2",
            "1.0",
            Long.MAX_VALUE.toString(),
            Double.MAX_VALUE.toString(),
            Double.MIN_VALUE.toString(),
            Double.POSITIVE_INFINITY.toString(),
            Double.NaN.toString()
        )

        constraint<String> {
            isNegativeNumber(false)
        }.allMatch(
            "-1475",
            "-242",
            "-53",
            "-2",
            "-0.45",
            "-34.355",
            "-1.0",
            Long.MIN_VALUE.toString(),
            Double.NEGATIVE_INFINITY.toString()
        ).allFail(
            "",
            "a",
            "Ahmed",
            "Mourad",
            "0.45",
            "34.355",
            "178",
            "1475",
            "242",
            "53",
            "2",
            "1.0",
            "0",
            "-0",
            "0000",
            "0.0",
            Long.MAX_VALUE.toString(),
            Double.MAX_VALUE.toString(),
            Double.MIN_VALUE.toString(),
            Double.POSITIVE_INFINITY.toString(),
            Double.NaN.toString()
        )
    }

    @Test
    fun isZero_meansThisStringQualifiesAsZero() {
        constraint<String> {
            isZero()
        }.allMatch(
            "0",
            "-0",
            "0000",
            "0.0"
        ).allFail(
            "",
            "-1475",
            "-242",
            "-53",
            "-2",
            "-0.45",
            "-34.355",
            "a",
            "Ahmed",
            "Mourad",
            "0.45",
            "34.355",
            "178",
            "1475",
            "242",
            "53",
            "2",
            "1.0",
            "-1.0",
            Long.MAX_VALUE.toString(),
            Double.MAX_VALUE.toString(),
            Double.MIN_VALUE.toString(),
            Double.POSITIVE_INFINITY.toString(),
            Double.NaN.toString(),
            Long.MIN_VALUE.toString(),
            Double.NEGATIVE_INFINITY.toString()
        )
    }

    @Test
    fun isNotZero_meansThisStringDoesNotQualifyAsZero() {
        constraint<String> {
            isNotZero()
        }.allMatch(
            "",
            "-1475",
            "-242",
            "-53",
            "-2",
            "-0.45",
            "-34.355",
            "a",
            "Ahmed",
            "Mourad",
            "0.45",
            "34.355",
            "178",
            "1475",
            "242",
            "53",
            "2",
            "1.0",
            "-1.0",
            Long.MAX_VALUE.toString(),
            Double.MAX_VALUE.toString(),
            Double.MIN_VALUE.toString(),
            Double.POSITIVE_INFINITY.toString(),
            Double.NaN.toString(),
            Long.MIN_VALUE.toString(),
            Double.NEGATIVE_INFINITY.toString()
        ).allFail(
            "0",
            "-0",
            "0000",
            "0.0"
        )
    }

    @Test
    fun isEqualTo_meansThisStringEqualsTheGivenString() {

        constraint<String> {
            isEqualTo("Ahmed Mourad", false)
        }.allMatch(
            "Ahmed Mourad"
        ).allFail(
            "",
            "Ahmed",
            "Mourad",
            "asd",
            "Ahmed Mourad ",
            " Ahmed Mourad",
            "AhmedMourad",
            "ahmedmourad"
        )

        constraint<String> {
            isEqualTo("Ahmed Mourad", true)
        }.allMatch(
            "Ahmed Mourad",
            "ahmed mourad",
            "AHMED MOURAD",
            "AhMed MouRAd",
            "AHMed MOuRad"
        ).allFail(
            "",
            "Ahmed",
            "Mourad",
            "asd",
            "Ahmed Mourad ",
            " Ahmed Mourad",
            "AhmedMourad",
            "ahmedmourad",
            "AHMEDMOURAD"
        )
    }

    @Test
    fun isEqualToL_meansThisStringEqualsTheGivenString() {

        constraint<String> {
            isEqualTo(false) { "Ahmed Mourad" }
        }.allMatch(
            "Ahmed Mourad"
        ).allFail(
            "",
            "Ahmed",
            "Mourad",
            "asd",
            "Ahmed Mourad ",
            " Ahmed Mourad",
            "AhmedMourad",
            "ahmedmourad"
        )

        constraint<String> {
            isEqualTo(true) { "Ahmed Mourad" }
        }.allMatch(
            "Ahmed Mourad",
            "ahmed mourad",
            "AHMED MOURAD",
            "AhMed MouRAd",
            "AHMed MOuRad"
        ).allFail(
            "",
            "Ahmed",
            "Mourad",
            "asd",
            "Ahmed Mourad ",
            " Ahmed Mourad",
            "AhmedMourad",
            "ahmedmourad",
            "AHMEDMOURAD"
        )
    }

    @Test
    fun isNotEqualTo_meansThisStringDoesNotEqualTheGivenString() {

        constraint<String> {
            isNotEqualTo("Ahmed Mourad", false)
        }.allMatch(
            "",
            "Ahmed",
            "Mourad",
            "asd",
            "Ahmed Mourad ",
            " Ahmed Mourad",
            "AhmedMourad",
            "ahmedmourad"
        ).allFail(
            "Ahmed Mourad"
        )

        constraint<String> {
            isNotEqualTo("Ahmed Mourad", true)
        }.allMatch(
            "",
            "Ahmed",
            "Mourad",
            "asd",
            "Ahmed Mourad ",
            " Ahmed Mourad",
            "AhmedMourad",
            "ahmedmourad",
            "AHMEDMOURAD"
        ).allFail(
            "Ahmed Mourad",
            "ahmed mourad",
            "AHMED MOURAD",
            "AhMed MouRAd",
            "AHMed MOuRad"
        )
    }

    @Test
    fun isNotEqualToL_meansThisStringDoesNotEqualTheGivenString() {

        constraint<String> {
            isNotEqualTo(false) { "Ahmed Mourad" }
        }.allMatch(
            "",
            "Ahmed",
            "Mourad",
            "asd",
            "Ahmed Mourad ",
            " Ahmed Mourad",
            "AhmedMourad",
            "ahmedmourad"
        ).allFail(
            "Ahmed Mourad"
        )

        constraint<String> {
            isNotEqualTo(true) { "Ahmed Mourad" }
        }.allMatch(
            "",
            "Ahmed",
            "Mourad",
            "asd",
            "Ahmed Mourad ",
            " Ahmed Mourad",
            "AhmedMourad",
            "ahmedmourad",
            "AHMEDMOURAD"
        ).allFail(
            "Ahmed Mourad",
            "ahmed mourad",
            "AHMED MOURAD",
            "AhMed MouRAd",
            "AHMed MOuRad"
        )
    }
}
