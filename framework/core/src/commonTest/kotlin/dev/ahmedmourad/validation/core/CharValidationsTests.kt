package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.utils.allFail
import dev.ahmedmourad.validation.core.utils.allMatch
import dev.ahmedmourad.validation.core.utils.constraint
import dev.ahmedmourad.validation.core.validations.*
import kotlin.test.Test

class CharValidationsTests {

    @Test
    fun isDefined_meansThisCharacterIsDefinedInUnicode() {
        constraint<Char> {
            isDefined()
        }.allMatch(
            '1',
            '2',
            'a',
            'b',
            '@'
        ).allFail(
            '\uffff',
            '\uffef',
            '\ufffe'
        )
    }

    @Test
    fun isLetter_meansThisCharacterIsALetter() {
        constraint<Char> {
            isLetter()
        }.allMatch(
            'a',
            'b',
            'c',
            'A',
            'B',
            'C'
        ).allFail(
            '1',
            '2',
            '3',
            '@',
            '$',
            ' ',
            '\n'
        )
    }

    @Test
    fun isLetterOrDigit_meansThisCharacterIsALetterOrADigit() {
        constraint<Char> {
            isLetterOrDigit()
        }.allMatch(
            'a',
            'b',
            'c',
            'A',
            'B',
            'C',
            '1',
            '2',
            '3'
        ).allFail(
            '@',
            '$',
            ' ',
            '\n'
        )
    }

    @Test
    fun isDigit_meansThisCharacterIsADigit() {
        constraint<Char> {
            isDigit()
        }.allMatch(
            '1',
            '2',
            '3'
        ).allFail(
            '@',
            'a',
            'b',
            'c',
            'A',
            'B',
            'C',
            '$',
            ' ',
            '\n'
        )
    }

    @Test
    fun isISOControl_meansThisCharacterIsAnISOControlCharacter() {
        constraint<Char> {
            isISOControl()
        }.allMatch(
            '\u0000',
            '\u000E',
            '\u0009'
        ).allFail(
            '1',
            'a'
        )
    }

    @Test
    fun isWhitespace_meansThisCharacterIsWhitespace() {
        constraint<Char> {
            isWhitespace()
        }.allMatch(
            ' ',
            '\n',
            '\t'
        ).allFail(
            '_',
            'a',
            'b',
            'A',
            'B',
            '%',
            '^',
            '&',
            '1',
            '2'
        )
    }

    @Test
    fun isUpperCase_meansThisCharacterIsUpperCase() {
        constraint<Char> {
            isUpperCase()
        }.allMatch(
            'A',
            'B',
            'C'
        ).allFail(
            ' ',
            '\n',
            '\t',
            '_',
            'a',
            'b',
            'c',
            '%',
            '^',
            '&',
            '1',
            '2'
        )
    }

    @Test
    fun isLowerCase_meansThisCharacterIsLowerCase() {
        constraint<Char> {
            isLowerCase()
        }.allMatch(
            'a',
            'b',
            'c'
        ).allFail(
            ' ',
            '\n',
            '\t',
            '_',
            'A',
            'B',
            'C',
            '%',
            '^',
            '&',
            '1',
            '2'
        )
    }

    @Test
    fun isLowerCase_meansThisCharacterIsTitleCase() {
        constraint<Char> {
            isTitleCase()
        }.allMatch(
            'ǅ',
            'ǈ',
            'ǋ',
            'ǲ',
            '\u01f2'
        ).allFail(
            'a',
            'b',
            'c',
            ' ',
            '\n',
            '\t',
            '_',
            'A',
            'B',
            'C',
            '%',
            '^',
            '&',
            '1',
            '2'
        )
    }

    @Test
    fun isHighSurrogate_meansThisCharacterIsUnicodeHighSurrogateCharacter() {
        constraint<Char> {
            isHighSurrogate()
        }.allMatch(
            '\ud8b4',
            '\uD800',
            '\uD8EE',
            '\uD8CC',
            '\uDBFF'
        ).allFail(
            'a',
            'b',
            'c',
            ' ',
            '\n',
            '\t',
            '_',
            'A',
            'B',
            'C',
            '%',
            '^',
            '&',
            '1',
            '2'
        )
    }

    @Test
    fun isLowSurrogate_meansThisCharacterIsUnicodeLowSurrogateCharacter() {
        constraint<Char> {
            isLowSurrogate()
        }.allMatch(
            '\uDC00',
            '\uDEFF',
            '\uDCEF',
            '\uDEFC',
            '\uDFFF'
        ).allFail(
            'a',
            'b',
            'c',
            ' ',
            '\n',
            '\t',
            '_',
            'A',
            'B',
            'C',
            '%',
            '^',
            '&',
            '1',
            '2'
        )
    }

    @Test
    fun isEqualTo_meansThisCharacterIsEqualToTheGivenCharacter() {

        constraint<Char> {
            isEqualTo(false, 'a')
        }.allMatch(
            'a'
        ).allFail(
            'A',
            'b',
            'B',
            ' ',
            '\n',
            '\t',
            '_',
            '%',
            '^',
            '&',
            '1',
            '2'
        )

        constraint<Char> {
            isEqualTo(true, 'a')
        }.allMatch(
            'a',
            'A'
        ).allFail(
            'b',
            'B',
            ' ',
            '\n',
            '\t',
            '_',
            '%',
            '^',
            '&',
            '1',
            '2'
        )
    }

    @Test
    fun isEqualToL_meansThisCharacterIsEqualToTheGivenCharacter() {

        constraint<Char> {
            isEqualTo(false) { 'a' }
        }.allMatch(
            'a'
        ).allFail(
            'A',
            'b',
            'B',
            ' ',
            '\n',
            '\t',
            '_',
            '%',
            '^',
            '&',
            '1',
            '2'
        )

        constraint<Char> {
            isEqualTo(true) { 'a' }
        }.allMatch(
            'a',
            'A'
        ).allFail(
            'b',
            'B',
            ' ',
            '\n',
            '\t',
            '_',
            '%',
            '^',
            '&',
            '1',
            '2'
        )
    }

    @Test
    fun isNotEqualTo_meansThisCharacterIsNotEqualToTheGivenCharacter() {

        constraint<Char> {
            isNotEqualTo(false, 'a')
        }.allMatch(
            'A',
            'b',
            'B',
            ' ',
            '\n',
            '\t',
            '_',
            '%',
            '^',
            '&',
            '1',
            '2'
        ).allFail(
            'a'
        )

        constraint<Char> {
            isNotEqualTo(true, 'a')
        }.allMatch(
            'b',
            'B',
            ' ',
            '\n',
            '\t',
            '_',
            '%',
            '^',
            '&',
            '1',
            '2'
        ).allFail(
            'a',
            'A'
        )
    }

    @Test
    fun isNotEqualToL_meansThisCharacterIsNotEqualToTheGivenCharacter() {

        constraint<Char> {
            isNotEqualTo(false) { 'a' }
        }.allMatch(
            'A',
            'b',
            'B',
            ' ',
            '\n',
            '\t',
            '_',
            '%',
            '^',
            '&',
            '1',
            '2'
        ).allFail(
            'a'
        )

        constraint<Char> {
            isNotEqualTo(true) { 'a' }
        }.allMatch(
            'b',
            'B',
            ' ',
            '\n',
            '\t',
            '_',
            '%',
            '^',
            '&',
            '1',
            '2'
        ).allFail(
            'a',
            'A'
        )
    }
}
