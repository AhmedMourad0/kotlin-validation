package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint

//TODO: forAll, forAny, forNone
inline fun <DT : CharSequence> Constraint<DT>.minLength(
    crossinline min: (DT) -> Int
) = validation {
    subject.length >= min(subject)
}

fun <DT : CharSequence> Constraint<DT>.minLength(min: Int) = minLength { min }

inline fun <DT : CharSequence> Constraint<DT>.maxLength(
    crossinline max: (DT) -> Int
) = validation {
    subject.length <= max(subject)
}

fun <DT : CharSequence> Constraint<DT>.maxLength(max: Int) = maxLength { max }

inline fun <DT : CharSequence> Constraint<DT>.lengthLessThan(
    crossinline maxExclusive: (DT) -> Int
) = validation {
    subject.length < maxExclusive(subject)
}

fun <DT : CharSequence> Constraint<DT>.lengthLessThan(maxExclusive: Int) = lengthLessThan { maxExclusive }

inline fun <DT : CharSequence> Constraint<DT>.lengthLargerThan(
    crossinline minExclusive: (DT) -> Int
) = validation {
    subject.length > minExclusive(subject)
}

fun <DT : CharSequence> Constraint<DT>.lengthLargerThan(minExclusive: Int) = lengthLargerThan { minExclusive }

inline fun <DT : CharSequence> Constraint<DT>.lengthIn(
    crossinline range: (DT) -> IntRange
) = validation {
    subject.length in range(subject)
}

fun <DT : CharSequence> Constraint<DT>.lengthIn(range: IntRange) = lengthIn { range }

fun <DT : CharSequence> Constraint<DT>.lengthIn(min: Int, max: Int) = lengthIn(min..max)

inline fun <DT : CharSequence> Constraint<DT>.lengthNotIn(
    crossinline range: (DT) -> IntRange
) = validation {
    subject.length !in range(subject)
}

fun <DT : CharSequence> Constraint<DT>.lengthNotIn(range: IntRange) = lengthNotIn { range }

fun <DT : CharSequence> Constraint<DT>.lengthNotIn(min: Int, max: Int) = lengthNotIn(min..max)

inline fun <DT : CharSequence> Constraint<DT>.lengthEqualTo(
    crossinline value: (DT) -> Int
) = validation {
    subject.length == value(subject)
}

fun <DT : CharSequence> Constraint<DT>.lengthEqualTo(value: Int) = lengthEqualTo { value }

inline fun <DT : CharSequence> Constraint<DT>.lengthNotEqualTo(
    crossinline value: (DT) -> Int
) = validation {
    subject.length != value(subject)
}

fun <DT : CharSequence> Constraint<DT>.lengthNotEqualTo(value: Int) = lengthNotEqualTo { value }

inline fun <DT : CharSequence> Constraint<DT>.contains(
    ignoreCase: Boolean = false,
    crossinline portion: (DT) -> CharSequence
) = validation {
    subject.contains(portion(subject), ignoreCase)
}

fun <DT : CharSequence> Constraint<DT>.contains(
    portion: CharSequence,
    ignoreCase: Boolean = false
) = contains(ignoreCase) { portion }

inline fun <DT : CharSequence> Constraint<DT>.containsChar(
    ignoreCase: Boolean = false,
    crossinline portion: (DT) -> Char
) = validation {
    subject.contains(portion(subject), ignoreCase)
}

fun <DT : CharSequence> Constraint<DT>.containsChar(
    portion: Char,
    ignoreCase: Boolean = false
) = containsChar(ignoreCase) { portion }

fun <DT : CharSequence> Constraint<DT>.contains(regex: Regex) = validation {
    subject.contains(regex)
}

//TODO: startsWithAnyOf, doesNotStartWith
inline fun <DT : CharSequence> Constraint<DT>.startsWith(
    ignoreCase: Boolean = false,
    crossinline prefix: (DT) -> CharSequence
) = validation {
    subject.startsWith(prefix(subject), ignoreCase)
}

fun <DT : CharSequence> Constraint<DT>.startsWith(
    prefix: CharSequence,
    ignoreCase: Boolean = false
) = startsWith(ignoreCase) { prefix }

inline fun <DT : CharSequence> Constraint<DT>.startsWithChar(
    ignoreCase: Boolean = false,
    crossinline prefix: (DT) -> Char
) = validation {
    subject.startsWith(prefix(subject), ignoreCase)
}

fun <DT : CharSequence> Constraint<DT>.startsWithChar(
    prefix: Char,
    ignoreCase: Boolean = false
) = startsWithChar(ignoreCase) { prefix }


inline fun <DT : CharSequence> Constraint<DT>.endsWith(
    ignoreCase: Boolean = false,
    crossinline suffix: (DT) -> CharSequence
) = validation {
    subject.endsWith(suffix(subject), ignoreCase)
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
    subject.endsWith(suffix(subject), ignoreCase)
}

fun <DT : CharSequence> Constraint<DT>.endsWithChar(
    suffix: Char,
    ignoreCase: Boolean = false
) = endsWithChar(ignoreCase) { suffix }

fun <DT : CharSequence> Constraint<DT>.matches(regex: Regex) = validation {
    subject.matches(regex)
}

fun <DT : CharSequence> Constraint<DT>.isEmpty() = validation { subject.isEmpty() }

fun <DT : CharSequence> Constraint<DT>.isNotEmpty() = validation { subject.isNotEmpty() }

fun <DT : CharSequence> Constraint<DT>.isBlank() = validation { subject.isBlank() }

fun <DT : CharSequence> Constraint<DT>.isNotBlank() = validation { subject.isNotBlank() }

inline fun <DT : CharSequence> Constraint<DT>.hasSurrogatePairAt(
    crossinline index: (DT) -> Int
) = validation {
    subject.hasSurrogatePairAt(index(subject))
}

fun <DT : CharSequence> Constraint<DT>.hasSurrogatePairAt(index: Int) = hasSurrogatePairAt { index }

fun <DT : CharSequence> Constraint<DT>.regionMatches(
    thisOffset: Int,
    other: DT,
    otherOffset: Int,
    length: Int,
    ignoreCase: Boolean
) = validation {
    subject.regionMatches(thisOffset, other, otherOffset, length, ignoreCase)
}
