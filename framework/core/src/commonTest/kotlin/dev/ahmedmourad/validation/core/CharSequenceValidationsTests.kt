package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.utils.allFail
import dev.ahmedmourad.validation.core.utils.allMatch
import dev.ahmedmourad.validation.core.utils.constraint
import dev.ahmedmourad.validation.core.validations.*
import kotlin.test.Test

class CharSequenceValidationsTests {

    @Test
    fun minLength_meansTheCharSequenceMustHaveAtLeastTheGivenNumberOfCharacters() {
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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

        constraint<CharSequence> {
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

        constraint<CharSequence> {
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

        constraint<CharSequence> {
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

        constraint<CharSequence> {
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

        constraint<CharSequence> {
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

        constraint<CharSequence> {
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

        constraint<CharSequence> {
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

        constraint<CharSequence> {
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
        constraint<CharSequence> {
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

        constraint<CharSequence> {
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

        constraint<CharSequence> {
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

        constraint<CharSequence> {
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

        constraint<CharSequence> {
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

        constraint<CharSequence> {
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

        constraint<CharSequence> {
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

        constraint<CharSequence> {
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

        constraint<CharSequence> {
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

        constraint<CharSequence> {
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

        constraint<CharSequence> {
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

        constraint<CharSequence> {
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

        constraint<CharSequence> {
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

        constraint<CharSequence> {
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

        constraint<CharSequence> {
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

        constraint<CharSequence> {
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

        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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

        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
        constraint<CharSequence> {
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
