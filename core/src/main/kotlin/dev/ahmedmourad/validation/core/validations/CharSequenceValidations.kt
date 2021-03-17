package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Validator
import dev.ahmedmourad.validation.core.validation

inline fun <DT : CharSequence> Validator<DT>.minLength(
    crossinline min: (DT) -> Int
) = validation {
    it.length >= min(it)
}

fun <DT : CharSequence> Validator<DT>.minLength(min: Int) = minLength { min }

inline fun <DT : CharSequence> Validator<DT>.maxLength(
    crossinline max: (DT) -> Int
) = validation {
    it.length <= max(it)
}

fun <DT : CharSequence> Validator<DT>.maxLength(max: Int) = maxLength { max }

inline fun <DT : CharSequence> Validator<DT>.lengthLessThan(
    crossinline maxExclusive: (DT) -> Int
) = validation {
    it.length < maxExclusive(it)
}

fun <DT : CharSequence> Validator<DT>.lengthLessThan(maxExclusive: Int) = lengthLessThan { maxExclusive }

inline fun <DT : CharSequence> Validator<DT>.lengthLargerThan(
    crossinline minExclusive: (DT) -> Int
) = validation {
    it.length > minExclusive(it)
}

fun <DT : CharSequence> Validator<DT>.lengthLargerThan(minExclusive: Int) = lengthLargerThan { minExclusive }

inline fun <DT : CharSequence> Validator<DT>.lengthIn(
    crossinline range: (DT) -> IntRange
) = validation {
    it.length in range(it)
}

fun <DT : CharSequence> Validator<DT>.lengthIn(range: IntRange) = lengthIn { range }

fun <DT : CharSequence> Validator<DT>.lengthIn(min: Int, max: Int) = lengthIn(min..max)

inline fun <DT : CharSequence> Validator<DT>.lengthNotIn(
    crossinline range: (DT) -> IntRange
) = validation {
    it.length !in range(it)
}

fun <DT : CharSequence> Validator<DT>.lengthNotIn(range: IntRange) = lengthNotIn { range }

fun <DT : CharSequence> Validator<DT>.lengthNotIn(min: Int, max: Int) = lengthNotIn(min..max)

inline fun <DT : CharSequence> Validator<DT>.lengthEqualTo(
    crossinline value: (DT) -> Int
) = validation {
    it.length == value(it)
}

fun <DT : CharSequence> Validator<DT>.lengthEqualTo(value: Int) = lengthEqualTo { value }

inline fun <DT : CharSequence> Validator<DT>.lengthNotEqualTo(
    crossinline value: (DT) -> Int
) = validation {
    it.length != value(it)
}

fun <DT : CharSequence> Validator<DT>.lengthNotEqualTo(value: Int) = lengthNotEqualTo { value }

inline fun <DT : CharSequence> Validator<DT>.contains(
    ignoreCase: Boolean = false,
    crossinline portion: (DT) -> CharSequence
) = validation {
    it.contains(portion(it), ignoreCase)
}

fun <DT : CharSequence> Validator<DT>.contains(
    portion: CharSequence,
    ignoreCase: Boolean = false
) = contains(ignoreCase) { portion }

inline fun <DT : CharSequence> Validator<DT>.containsChar(
    ignoreCase: Boolean = false,
    crossinline portion: (DT) -> Char
) = validation {
    it.contains(portion(it), ignoreCase)
}

fun <DT : CharSequence> Validator<DT>.containsChar(
    portion: Char,
    ignoreCase: Boolean = false
) = containsChar(ignoreCase) { portion }

fun <DT : CharSequence> Validator<DT>.contains(regex: Regex) = validation {
    it.contains(regex)
}

inline fun <DT : CharSequence> Validator<DT>.startsWith(
    ignoreCase: Boolean = false,
    crossinline prefix: (DT) -> CharSequence
) = validation {
    it.startsWith(prefix(it), ignoreCase)
}

fun <DT : CharSequence> Validator<DT>.startsWith(
    prefix: CharSequence,
    ignoreCase: Boolean = false
) = startsWith(ignoreCase) { prefix }

inline fun <DT : CharSequence> Validator<DT>.startsWithChar(
    ignoreCase: Boolean = false,
    crossinline prefix: (DT) -> Char
) = validation {
    it.startsWith(prefix(it), ignoreCase)
}

fun <DT : CharSequence> Validator<DT>.startsWithChar(
    prefix: Char,
    ignoreCase: Boolean = false
) = startsWithChar(ignoreCase) { prefix }


inline fun <DT : CharSequence> Validator<DT>.endsWith(
    ignoreCase: Boolean = false,
    crossinline suffix: (DT) -> CharSequence
) = validation {
    it.endsWith(suffix(it), ignoreCase)
}

fun <DT : CharSequence> Validator<DT>.endsWith(
    suffix: CharSequence,
    ignoreCase: Boolean = false
) = endsWith(ignoreCase) { suffix }

//TODO: @OverloadResolutionByLambdaReturnType
inline fun <DT : CharSequence> Validator<DT>.endsWithChar(
    ignoreCase: Boolean = false,
    crossinline suffix: (DT) -> Char
) = validation {
    it.endsWith(suffix(it), ignoreCase)
}

fun <DT : CharSequence> Validator<DT>.endsWithChar(
    suffix: Char,
    ignoreCase: Boolean = false
) = endsWithChar(ignoreCase) { suffix }

fun <DT : CharSequence> Validator<DT>.matches(regex: Regex) = validation {
    it.matches(regex)
}

fun <DT : CharSequence> Validator<DT>.isEmpty() = validation { it.isEmpty() }

fun <DT : CharSequence> Validator<DT>.isNotEmpty() = validation { it.isNotEmpty() }

fun <DT : CharSequence> Validator<DT>.isBlank() = validation { it.isBlank() }

fun <DT : CharSequence> Validator<DT>.isNotBlank() = validation { it.isNotBlank() }

inline fun <DT : CharSequence> Validator<DT>.hasSurrogatePairAt(
    crossinline index: (DT) -> Int
) = validation {
    it.hasSurrogatePairAt(index(it))
}

fun <DT : CharSequence> Validator<DT>.hasSurrogatePairAt(index: Int) = hasSurrogatePairAt { index }

fun <DT : CharSequence> Validator<DT>.regionMatches(
    thisOffset: Int,
    other: DT,
    otherOffset: Int,
    length: Int,
    ignoreCase: Boolean
) = validation {
    it.regionMatches(thisOffset, other, otherOffset, length, ignoreCase)
}
