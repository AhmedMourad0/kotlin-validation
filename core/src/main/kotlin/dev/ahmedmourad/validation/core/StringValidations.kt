package dev.ahmedmourad.validation.core

fun Validator<String>.isNumber() = validation {
    it.toLongOrNull() != null
}

fun Validator<String>.isPositiveInteger(orZero: Boolean = false) = validation { validated ->
    if (orZero) {
        validated.toLongOrNull()?.let { it >= 0 } ?: false
    } else {
        validated.toLongOrNull()?.let { it > 0 } ?: false
    }
}

fun Validator<String>.isNegativeInteger(orZero: Boolean = false) = validation { validated ->
    if (validated.startsWith('-')) {
        if (orZero) {
            validated.drop(1).toLongOrNull()?.let { it <= 0 } ?: false
        } else {
            validated.drop(1).toLongOrNull()?.let { it < 0 } ?: false
        }
    } else {
        false
    }
}

fun Validator<String>.isPositiveDecimal(orZero: Boolean = false) = validation { validated ->
    if (orZero) {
        validated.toDoubleOrNull()?.let { it >= 0.0 } ?: false
    } else {
        validated.toDoubleOrNull()?.let { it > 0.0 } ?: false
    }
}

fun Validator<String>.isNegativeDecimal(orZero: Boolean = false) = validation { validated ->
    if (orZero) {
        validated.toDoubleOrNull()?.let { it <= 0.0 } ?: false
    } else {
        validated.toDoubleOrNull()?.let { it < 0.0 } ?: false
    }
}

fun Validator<String>.isZero() = validation {
    (it.toIntOrNull() ?: 1) == 0
}

fun Validator<String>.isNotZero() = validation {
    (it.toLongOrNull() ?: 0L) != 0L
}
