package dev.ahmedmourad.validation.core.utils

import dev.ahmedmourad.validation.core.Validator
import dev.ahmedmourad.validation.core.ValidatorImpl

fun <DT> validator(validations: Validator<DT>.() -> Unit): ValidatorImpl<DT> {
    return ValidatorImpl<DT>().apply(validations)
}

fun <DT> ValidatorImpl<DT>.allMatch(vararg items: DT): ValidatorImpl<DT> {
    val failed = items.filterNot {
        this.validateAll(it)
    }
    if (failed.isNotEmpty()) {
        throw AssertionError(
            "Validations do not match for items: {\n\t${failed.joinToString(",\n\t")}\n}"
        )
    }
    return this
}

fun <DT> ValidatorImpl<DT>.allFail(vararg items: DT): ValidatorImpl<DT> {
    val matching = items.filter {
        this.validateAll(it)
    }
    if (matching.isNotEmpty()) {
        throw AssertionError(
            "Validations do not fail for items: {\n\t${matching.joinToString(",\n\t")}\n}"
        )
    }
    return this
}
