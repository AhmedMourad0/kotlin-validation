package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint

//TODO: forAll, forAny, forNone
inline fun <DT : CharSequence> Constraint<DT>.minLength(
    crossinline min: (DT) -> Int
) = validation {
    it.length >= min(it)
}

fun <DT : CharSequence> Constraint<DT>.minLength(min: Int) = minLength { min }

inline fun <DT : CharSequence> Constraint<DT>.maxLength(
    crossinline max: (DT) -> Int
) = validation {
    it.length <= max(it)
}

fun <DT : CharSequence> Constraint<DT>.maxLength(max: Int) = maxLength { max }

inline fun <DT : CharSequence> Constraint<DT>.lengthLessThan(
    crossinline maxExclusive: (DT) -> Int
) = validation {
    it.length < maxExclusive(it)
}

fun <DT : CharSequence> Constraint<DT>.lengthLessThan(maxExclusive: Int) = lengthLessThan { maxExclusive }

inline fun <DT : CharSequence> Constraint<DT>.lengthLargerThan(
    crossinline minExclusive: (DT) -> Int
) = validation {
    it.length > minExclusive(it)
}

fun <DT : CharSequence> Constraint<DT>.lengthLargerThan(minExclusive: Int) = lengthLargerThan { minExclusive }

inline fun <DT : CharSequence> Constraint<DT>.lengthIn(
    crossinline range: (DT) -> IntRange
) = validation {
    it.length in range(it)
}

fun <DT : CharSequence> Constraint<DT>.lengthIn(range: IntRange) = lengthIn { range }

fun <DT : CharSequence> Constraint<DT>.lengthIn(min: Int, max: Int) = lengthIn(min..max)

inline fun <DT : CharSequence> Constraint<DT>.lengthNotIn(
    crossinline range: (DT) -> IntRange
) = validation {
    it.length !in range(it)
}

fun <DT : CharSequence> Constraint<DT>.lengthNotIn(range: IntRange) = lengthNotIn { range }

fun <DT : CharSequence> Constraint<DT>.lengthNotIn(min: Int, max: Int) = lengthNotIn(min..max)

inline fun <DT : CharSequence> Constraint<DT>.lengthEqualTo(
    crossinline value: (DT) -> Int
) = validation {
    it.length == value(it)
}

fun <DT : CharSequence> Constraint<DT>.lengthEqualTo(value: Int) = lengthEqualTo { value }

inline fun <DT : CharSequence> Constraint<DT>.lengthNotEqualTo(
    crossinline value: (DT) -> Int
) = validation {
    it.length != value(it)
}

fun <DT : CharSequence> Constraint<DT>.lengthNotEqualTo(value: Int) = lengthNotEqualTo { value }

inline fun <DT : CharSequence> Constraint<DT>.contains(
    ignoreCase: Boolean = false,
    crossinline portion: (DT) -> CharSequence
) = validation {
    it.contains(portion(it), ignoreCase)
}

fun <DT : CharSequence> Constraint<DT>.contains(
    portion: CharSequence,
    ignoreCase: Boolean = false
) = contains(ignoreCase) { portion }

inline fun <DT : CharSequence> Constraint<DT>.containsChar(
    ignoreCase: Boolean = false,
    crossinline portion: (DT) -> Char
) = validation {
    it.contains(portion(it), ignoreCase)
}

fun <DT : CharSequence> Constraint<DT>.containsChar(
    portion: Char,
    ignoreCase: Boolean = false
) = containsChar(ignoreCase) { portion }

fun <DT : CharSequence> Constraint<DT>.contains(regex: Regex) = validation {
    it.contains(regex)
}

//TODO: startsWithAnyOf, doesNotStartWith
inline fun <DT : CharSequence> Constraint<DT>.startsWith(
    ignoreCase: Boolean = false,
    crossinline prefix: (DT) -> CharSequence
) = validation {
    it.startsWith(prefix(it), ignoreCase)
}

fun <DT : CharSequence> Constraint<DT>.startsWith(
    prefix: CharSequence,
    ignoreCase: Boolean = false
) = startsWith(ignoreCase) { prefix }

inline fun <DT : CharSequence> Constraint<DT>.startsWithChar(
    ignoreCase: Boolean = false,
    crossinline prefix: (DT) -> Char
) = validation {
    it.startsWith(prefix(it), ignoreCase)
}

fun <DT : CharSequence> Constraint<DT>.startsWithChar(
    prefix: Char,
    ignoreCase: Boolean = false
) = startsWithChar(ignoreCase) { prefix }


inline fun <DT : CharSequence> Constraint<DT>.endsWith(
    ignoreCase: Boolean = false,
    crossinline suffix: (DT) -> CharSequence
) = validation {
    it.endsWith(suffix(it), ignoreCase)
}

fun <DT : CharSequence> Constraint<DT>.endsWith(
    suffix: CharSequence,
    ignoreCase: Boolean = false
) = endsWith(ignoreCase) { suffix }

//TODO: @OverloadResolutionByLambdaReturnType
inline fun <DT : CharSequence> Constraint<DT>.endsWithChar(
    ignoreCase: Boolean = false,
    crossinline suffix: (DT) -> Char
) = validation {
    it.endsWith(suffix(it), ignoreCase)
}

fun <DT : CharSequence> Constraint<DT>.endsWithChar(
    suffix: Char,
    ignoreCase: Boolean = false
) = endsWithChar(ignoreCase) { suffix }

fun <DT : CharSequence> Constraint<DT>.matches(regex: Regex) = validation {
    it.matches(regex)
}

fun <DT : CharSequence> Constraint<DT>.isEmpty() = validation { it.isEmpty() }

fun <DT : CharSequence> Constraint<DT>.isNotEmpty() = validation { it.isNotEmpty() }

fun <DT : CharSequence> Constraint<DT>.isBlank() = validation { it.isBlank() }

fun <DT : CharSequence> Constraint<DT>.isNotBlank() = validation { it.isNotBlank() }

inline fun <DT : CharSequence> Constraint<DT>.hasSurrogatePairAt(
    crossinline index: (DT) -> Int
) = validation {
    it.hasSurrogatePairAt(index(it))
}

fun <DT : CharSequence> Constraint<DT>.hasSurrogatePairAt(index: Int) = hasSurrogatePairAt { index }

fun <DT : CharSequence> Constraint<DT>.regionMatches(
    thisOffset: Int,
    other: DT,
    otherOffset: Int,
    length: Int,
    ignoreCase: Boolean
) = validation {
    it.regionMatches(thisOffset, other, otherOffset, length, ignoreCase)
}
