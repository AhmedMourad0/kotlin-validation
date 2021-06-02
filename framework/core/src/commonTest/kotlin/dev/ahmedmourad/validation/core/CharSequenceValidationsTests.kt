package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.utils.allFail
import dev.ahmedmourad.validation.core.utils.allMatch
import dev.ahmedmourad.validation.core.utils.validator
import dev.ahmedmourad.validation.core.validations.*
import kotlin.test.Test

class CharSequenceValidationsTests {

    @Test
    fun minLength_meansTheCharSequenceMustHaveAtLeastTheGivenNumberOfCharacters() {
        validator<CharSequence> {
            minLength(4)
        }.allMatch(
            "1234",
            "12345",
            "123456",
            "1234567",
            "12345678"
        ).allFail(
            "",
            "1",
            "12",
            "123"
        )
    }

    @Test
    fun minLengthL_meansTheCharSequenceMustHaveAtLeastTheGivenNumberOfCharacters() {
        validator<CharSequence> {
            minLength { 4 }
        }.allMatch(
            "1234",
            "12345",
            "123456",
            "1234567",
            "12345678"
        ).allFail(
            "",
            "1",
            "12",
            "123"
        )
    }

    @Test
    fun maxLength_meansTheCharSequenceMustHaveAtMostTheGivenNumberOfCharacters() {
        validator<CharSequence> {
            maxLength(4)
        }.allMatch(
            "",
            "1",
            "12",
            "123",
            "1234"
        ).allFail(
            "12345",
            "123456",
            "1234567",
            "12345678"
        )
    }

    @Test
    fun maxLengthL_meansTheCharSequenceMustHaveAtMostTheGivenNumberOfCharacters() {
        validator<CharSequence> {
            maxLength { 4 }
        }.allMatch(
            "",
            "1",
            "12",
            "123",
            "1234"
        ).allFail(
            "12345",
            "123456",
            "1234567",
            "12345678"
        )
    }

    @Test
    fun lengthLessThan_meansTheCharSequenceMustHaveLessThanTheGivenNumberOfCharacters() {
        validator<CharSequence> {
            lengthLessThan(4)
        }.allMatch(
            "",
            "1",
            "12",
            "123"
        ).allFail(
            "1234",
            "12345",
            "123456",
            "1234567",
            "12345678"
        )
    }

    @Test
    fun lengthLessThanL_meansTheCharSequenceMustHaveLessThanTheGivenNumberOfCharacters() {
        validator<CharSequence> {
            lengthLessThan { 4 }
        }.allMatch(
            "",
            "1",
            "12",
            "123"
        ).allFail(
            "1234",
            "12345",
            "123456",
            "1234567",
            "12345678"
        )
    }

    @Test
    fun lengthLargerThan_meansTheCharSequenceMustHaveMoreThanTheGivenNumberOfCharacters() {
        validator<CharSequence> {
            lengthLargerThan(4)
        }.allMatch(
            "12345",
            "123456",
            "1234567",
            "12345678"
        ).allFail(
            "",
            "1",
            "12",
            "123",
            "1234"
        )
    }

    @Test
    fun lengthLargerThanL_meansTheCharSequenceMustHaveMoreThanTheGivenNumberOfCharacters() {
        validator<CharSequence> {
            lengthLargerThan { 4 }
        }.allMatch(
            "12345",
            "123456",
            "1234567",
            "12345678"
        ).allFail(
            "",
            "1",
            "12",
            "123",
            "1234"
        )
    }

    @Test
    fun lengthIn_meansTheCharSequenceMustHaveTheGivenRangeOfCharacters() {
        validator<CharSequence> {
            lengthIn(3, 5)
        }.allMatch(
            "123",
            "1234",
            "12345"
        ).allFail(
            "",
            "1",
            "12",
            "123456",
            "1234567",
            "12345678"
        )
    }

    @Test
    fun lengthInR_meansTheCharSequenceMustHaveTheGivenRangeOfCharacters() {
        validator<CharSequence> {
            lengthIn(3..5)
        }.allMatch(
            "123",
            "1234",
            "12345"
        ).allFail(
            "",
            "1",
            "12",
            "123456",
            "1234567",
            "12345678"
        )
    }

    @Test
    fun lengthInRL_meansTheCharSequenceMustHaveTheGivenRangeOfCharacters() {
        validator<CharSequence> {
            lengthIn { 3..5 }
        }.allMatch(
            "123",
            "1234",
            "12345"
        ).allFail(
            "",
            "1",
            "12",
            "123456",
            "1234567",
            "12345678"
        )
    }

    @Test
    fun lengthNotIn_meansTheCharSequenceMustNotHaveTheGivenRangeOfCharacters() {
        validator<CharSequence> {
            lengthNotIn(3, 5)
        }.allMatch(
            "",
            "1",
            "12",
            "123456",
            "1234567",
            "12345678"
        ).allFail(
            "123",
            "1234",
            "12345"
        )
    }

    @Test
    fun lengthNotInR_meansTheCharSequenceMustNotHaveTheGivenRangeOfCharacters() {
        validator<CharSequence> {
            lengthNotIn(3..5)
        }.allMatch(
            "",
            "1",
            "12",
            "123456",
            "1234567",
            "12345678"
        ).allFail(
            "123",
            "1234",
            "12345"
        )
    }

    @Test
    fun lengthNotInRL_meansTheCharSequenceMustNotHaveTheGivenRangeOfCharacters() {
        validator<CharSequence> {
            lengthNotIn { 3..5 }
        }.allMatch(
            "",
            "1",
            "12",
            "123456",
            "1234567",
            "12345678"
        ).allFail(
            "123",
            "1234",
            "12345"
        )
    }

    @Test
    fun lengthEqualTo_meansTheCharSequenceMustHaveTheGivenNumberOfCharacters() {
        validator<CharSequence> {
            lengthEqualTo(3)
        }.allMatch(
            "123"
        ).allFail(
            "",
            "1",
            "12",
            "1234",
            "12345",
            "123456"
        )
    }

    @Test
    fun lengthEqualToL_meansTheCharSequenceMustHaveTheGivenNumberOfCharacters() {
        validator<CharSequence> {
            lengthEqualTo { 3 }
        }.allMatch(
            "123"
        ).allFail(
            "",
            "1",
            "12",
            "1234",
            "12345",
            "123456"
        )
    }

    @Test
    fun lengthNotEqualTo_meansTheCharSequenceMustNotHaveTheGivenNumberOfCharacters() {
        validator<CharSequence> {
            lengthNotEqualTo(3)
        }.allMatch(
            "",
            "1",
            "12",
            "1234",
            "12345",
            "123456"
        ).allFail(
            "123"
        )
    }

    @Test
    fun lengthNotEqualToL_meansTheCharSequenceMustNotHaveTheGivenNumberOfCharacters() {
        validator<CharSequence> {
            lengthNotEqualTo { 3 }
        }.allMatch(
            "",
            "1",
            "12",
            "1234",
            "12345",
            "123456"
        ).allFail(
            "123"
        )
    }

    @Test
    fun contains_meansTheCharSequenceContainsTheGivenCharacterSequence() {

        validator<CharSequence> {
            contains("aB", true)
        }.allMatch(
            "aB",
            "aB1",
            "1aB",
            "1aB1",
            "1aB11",
            "1aB111",
            "Ab",
            "Ab1",
            "1Ab",
            "1Ab1",
            "1Ab11",
            "1Ab111"
        ).allFail(
            "",
            "a",
            "B",
            "Ba",
            "1Ba",
            "1a1B",
            "A",
            "b",
            "bA",
            "1bA",
            "1A1b"
        )

        validator<CharSequence> {
            contains("aB", false)
        }.allMatch(
            "aB",
            "aB1",
            "1aB",
            "1aB1",
            "1aB11",
            "1aB111"
        ).allFail(
            "",
            "a",
            "B",
            "Ba",
            "1Ba",
            "1a1B",
            "Ab",
            "ab",
            "1Ab",
            "1ab1",
            "1AB11",
            "1Ab111"
        )
    }

    @Test
    fun containsL_meansTheCharSequenceContainsTheGivenCharacterSequence() {

        validator<CharSequence> {
            contains(true) { "aB" }
        }.allMatch(
            "aB",
            "aB1",
            "1aB",
            "1aB1",
            "1aB11",
            "1aB111",
            "Ab",
            "Ab1",
            "1Ab",
            "1Ab1",
            "1Ab11",
            "1Ab111"
        ).allFail(
            "",
            "a",
            "B",
            "Ba",
            "1Ba",
            "1a1B",
            "A",
            "b",
            "bA",
            "1bA",
            "1A1b"
        )

        validator<CharSequence> {
            contains(false) { "aB" }
        }.allMatch(
            "aB",
            "aB1",
            "1aB",
            "1aB1",
            "1aB11",
            "1aB111"
        ).allFail(
            "",
            "a",
            "B",
            "Ba",
            "1Ba",
            "1a1B",
            "Ab",
            "ab",
            "1Ab",
            "1ab1",
            "1AB11",
            "1Ab111"
        )
    }

    @Test
    fun containsChar_meansTheCharSequenceContainsTheGivenCharacter() {

        validator<CharSequence> {
            containsChar('b', true)
        }.allMatch(
            "b",
            "B",
            "1B",
            "1b1",
            "11B",
            "11b1",
            "11b11",
            "11B111"
        ).allFail(
            "",
            "1",
            "1",
            "11",
            "1111"
        )

        validator<CharSequence> {
            containsChar('b', false)
        }.allMatch(
            "b",
            "1b1",
            "11b1",
            "11bB11"
        ).allFail(
            "",
            "1",
            "1",
            "11",
            "1111",
            "B",
            "1B",
            "11B",
            "11B111"
        )
    }

    @Test
    fun containsCharL_meansTheCharSequenceContainsTheGivenCharacter() {

        validator<CharSequence> {
            containsChar(true) { 'b' }
        }.allMatch(
            "b",
            "B",
            "1B",
            "1b1",
            "11B",
            "11b1",
            "11b11",
            "11B111"
        ).allFail(
            "",
            "1",
            "1",
            "11",
            "1111"
        )

        validator<CharSequence> {
            containsChar(false) { 'b' }
        }.allMatch(
            "b",
            "1b1",
            "11b1",
            "11bB11"
        ).allFail(
            "",
            "1",
            "1",
            "11",
            "1111",
            "B",
            "1B",
            "11B",
            "11B111"
        )
    }

    @Test
    fun containsR_meansAPortionOrTheWholeCharacterSequenceMatchesTheGivenRegex() {
        validator<CharSequence> {
            contains(Regex("\\s+"))
        }.allMatch(
            " ",
            "  ",
            "   ",
            "1 ",
            "1  ",
            " 1 ",
            "  1"
        ).allFail(
            "",
            "1",
            "11",
            "111"
        )
    }

    @Test
    fun startsWith_meansTheCharSequenceStartsWithTheGivenCharacterSequence() {

        validator<CharSequence> {
            startsWith("aB", true)
        }.allMatch(
            "aB",
            "ab",
            "aB1",
            "ab1"
        ).allFail(
            "",
            "a",
            "B",
            "Ba",
            "1Ba",
            "1a1B",
            "1aB",
            "1aB1"
        )

        validator<CharSequence> {
            startsWith("aB", false)
        }.allMatch(
            "aB",
            "aB1",
            "aB11"
        ).allFail(
            "",
            "a",
            "ab",
            "Ab",
            "ab",
            "AB",
            "1aB",
            "1aB1"
        )
    }

    @Test
    fun startsWithL_meansTheCharSequenceStartsWithTheGivenCharacterSequence() {

        validator<CharSequence> {
            startsWith(true) { "aB" }
        }.allMatch(
            "aB",
            "ab",
            "aB1",
            "ab1"
        ).allFail(
            "",
            "a",
            "B",
            "Ba",
            "1Ba",
            "1a1B",
            "1aB",
            "1aB1"
        )

        validator<CharSequence> {
            startsWith(false) { "aB" }
        }.allMatch(
            "aB",
            "aB1",
            "aB11"
        ).allFail(
            "",
            "a",
            "ab",
            "Ab",
            "ab",
            "AB",
            "1aB",
            "1aB1"
        )
    }

    @Test
    fun startsWithChar_meansTheCharSequenceStartsWithTheGivenCharacter() {

        validator<CharSequence> {
            startsWithChar('b', true)
        }.allMatch(
            "b",
            "B",
            "b1",
            "B1",
            "b11"
        ).allFail(
            "",
            "1",
            "1b",
            "1B",
            "1b1"
        )

        validator<CharSequence> {
            startsWithChar('b', false)
        }.allMatch(
            "b",
            "b1",
            "b11"
        ).allFail(
            "",
            "B",
            "B1",
            "1",
            "1b",
            "1b1"
        )
    }

    @Test
    fun startsWithCharL_meansTheCharSequenceStartsWithTheGivenCharacter() {

        validator<CharSequence> {
            startsWithChar(true) { 'b' }
        }.allMatch(
            "b",
            "B",
            "b1",
            "B1",
            "b11"
        ).allFail(
            "",
            "1",
            "1b",
            "1B",
            "1b1"
        )

        validator<CharSequence> {
            startsWithChar(false) { 'b' }
        }.allMatch(
            "b",
            "b1",
            "b11"
        ).allFail(
            "",
            "B",
            "B1",
            "1",
            "1b",
            "1b1"
        )
    }

    @Test
    fun endsWith_meansTheCharSequenceEndsWithTheGivenCharacterSequence() {

        validator<CharSequence> {
            endsWith("aB", true)
        }.allMatch(
            "aB",
            "ab",
            "1aB",
            "1ab"
        ).allFail(
            "",
            "a",
            "B",
            "Ba",
            "Ba1",
            "a1B1",
            "aB1",
            "1aB1"
        )

        validator<CharSequence> {
            endsWith("aB", false)
        }.allMatch(
            "aB",
            "1aB",
            "11aB"
        ).allFail(
            "",
            "a",
            "ab",
            "Ab",
            "ab",
            "AB",
            "aB1",
            "1aB1"
        )
    }

    @Test
    fun endsWithL_meansTheCharSequenceEndsWithTheGivenCharacterSequence() {

        validator<CharSequence> {
            endsWith(true) { "aB" }
        }.allMatch(
            "aB",
            "ab",
            "1aB",
            "1ab"
        ).allFail(
            "",
            "a",
            "B",
            "Ba",
            "Ba1",
            "a1B1",
            "aB1",
            "1aB1"
        )

        validator<CharSequence> {
            endsWith(false) { "aB" }
        }.allMatch(
            "aB",
            "1aB",
            "11aB"
        ).allFail(
            "",
            "a",
            "ab",
            "Ab",
            "ab",
            "AB",
            "aB1",
            "1aB1"
        )
    }

    @Test
    fun endsWithChar_meansTheCharSequenceEndsWithTheGivenCharacter() {

        validator<CharSequence> {
            endsWithChar('b', true)
        }.allMatch(
            "b",
            "B",
            "1b",
            "1B",
            "11b"
        ).allFail(
            "",
            "1",
            "b1",
            "B1",
            "1b1"
        )

        validator<CharSequence> {
            endsWithChar('b', false)
        }.allMatch(
            "b",
            "1b",
            "11b"
        ).allFail(
            "",
            "B",
            "1B",
            "1",
            "b1",
            "1b1"
        )
    }

    @Test
    fun endsWithCharL_meansTheCharSequenceEndsWithTheGivenCharacter() {

        validator<CharSequence> {
            endsWithChar(true) { 'b' }
        }.allMatch(
            "b",
            "B",
            "1b",
            "1B",
            "11b"
        ).allFail(
            "",
            "1",
            "b1",
            "B1",
            "1b1"
        )

        validator<CharSequence> {
            endsWithChar(false) { 'b' }
        }.allMatch(
            "b",
            "1b",
            "11b"
        ).allFail(
            "",
            "B",
            "1B",
            "1",
            "b1",
            "1b1"
        )
    }

    @Test
    fun matches_meansTheCharSequenceMatchesTheGivenRegex() {
        validator<CharSequence> {
            matches(Regex("\\s+"))
        }.allMatch(
            " ",
            "  ",
            "   "
        ).allFail(
            "",
            "1",
            "1 ",
            " 1",
            " 1 "
        )
    }

    @Test
    fun isEmpty_meansTheCharSequenceIsEmpty() {
        validator<CharSequence> {
            isEmpty()
        }.allMatch(
            ""
        ).allFail(
            " ",
            "1",
            "11",
            "111"
        )
    }

    @Test
    fun isNotEmpty_meansTheCharSequenceIsNotEmpty() {
        validator<CharSequence> {
            isNotEmpty()
        }.allMatch(
            " ",
            "1",
            "11",
            "111"
        ).allFail(
            ""
        )
    }

    @Test
    fun isBlank_meansTheCharSequenceIsEmptyOrBlank() {
        validator<CharSequence> {
            isBlank()
        }.allMatch(
            "",
            " ",
            "  ",
            "   "
        ).allFail(
            "1",
            "1 ",
            "11",
            "111"
        )
    }

    @Test
    fun isNotBlank_meansTheCharSequenceIsNeitherEmptyNorBlank() {
        validator<CharSequence> {
            isNotBlank()
        }.allMatch(
            "1",
            "1 ",
            "11",
            "111"
        ).allFail(
            "",
            " ",
            "  ",
            "   "
        )
    }

    @Test
    fun regionMatches_meansTheSpecifiedRangeInThisCharSequenceEqualsTheSpecifiedRangeInTheGivenCharSequence() {
        validator<CharSequence> {
            regionMatches(
                1,
                "aaaBbbcccddd",
                2,
                3,
                true
            )
        }.allMatch(
            "aaBbbccc",
            "aABbbccc",
            "aAbbbccc"
        ).allFail(
            "",
            "aBb",
            "aaaBbc",
            "aBbccc"
        )

        validator<CharSequence> {
            regionMatches(
                1,
                "aaaBbbcccddd",
                2,
                3,
                false
            )
        }.allMatch(
            "aaBbbccc"
        ).allFail(
            "",
            "aBb",
            "aaaBbc",
            "aBbccc",
            "aABbbccc",
            "aAbbbccc",
            "aAbb"
        )
    }

    @Test
    fun hasSurrogatePairAt_meansTheSpecifiedRangeInThisCharSequenceEqualsTheSpecifiedRangeInTheGivenCharSequence() {
        validator<CharSequence> {
            hasSurrogatePairAt(2)
        }.allMatch(
            "11\uD83C\uDF091111"
        ).allFail(
            "",
            "11",
            "111",
            "111\uD83C\uDF091",
            "\uD83C\uDF0911111"
        )
    }

    @Test
    fun hasSurrogatePairAtL_meansTheSpecifiedRangeInThisCharSequenceEqualsTheSpecifiedRangeInTheGivenCharSequence() {
        validator<CharSequence> {
            hasSurrogatePairAt { 2 }
        }.allMatch(
            "11\uD83C\uDF091111"
        ).allFail(
            "",
            "11",
            "111",
            "111\uD83C\uDF091",
            "\uD83C\uDF0911111"
        )
    }
}
