package dev.ahmedmourad.validation.core

import dev.ahmedmourad.validation.core.utils.allFail
import dev.ahmedmourad.validation.core.utils.allMatch
import dev.ahmedmourad.validation.core.utils.validator
import dev.ahmedmourad.validation.core.validations.*
import kotlin.test.Test

class CharValidationsTests {

    @Test
    fun isDefined_meansThisCharacterIsDefinedInUnicode() {
        validator<Char> {
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
        validator<Char> {
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
        validator<Char> {
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
        validator<Char> {
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
    fun isIdentifierIgnorable_meansThisCharacterIsIgnorableInUnicodeIdentifiers() {
        validator<Char> {
            isIdentifierIgnorable()
        }.allMatch(
            '\u0000',
            '\u000E'
        ).allFail(
            '1',
            '2',
            'a',
            'b',
            'A',
            'B',
            '@',
            '$',
            ' ',
            '\n'
        )
    }

    @Test
    fun isISOControl_meansThisCharacterIsAnISOControlCharacter() {
        validator<Char> {
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
    fun isJavaIdentifierPart_meansThisCharacterMayBePartOfAJavaIdentifier() {
        validator<Char> {
            isJavaIdentifierPart()
        }.allMatch(
            'a',
            'b',
            'A',
            'B',
            '$',
            '_',
            '1',
            '2'
        ).allFail(
            '%',
            '^',
            '&',
            '(',
            '>'
        )
    }

    @Test
    fun isJavaIdentifierStart_meansThisCharacterMayBeTheStartOfAJavaIdentifier() {
        validator<Char> {
            isJavaIdentifierStart()
        }.allMatch(
            '$',
            '_',
            'a',
            'b',
            'A',
            'B'
        ).allFail(
            '%',
            '^',
            '&',
            '(',
            '>',
            '1',
            '2'
        )
    }

    @Test
    fun isWhitespace_meansThisCharacterIsWhitespace() {
        validator<Char> {
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
        validator<Char> {
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
        validator<Char> {
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
        validator<Char> {
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
        validator<Char> {
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
        validator<Char> {
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

        validator<Char> {
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

        validator<Char> {
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

        validator<Char> {
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

        validator<Char> {
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

        validator<Char> {
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

        validator<Char> {
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

        validator<Char> {
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

        validator<Char> {
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
