package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Validator
import dev.ahmedmourad.validation.core.validation

fun <DT : Any> Validator<DT?>.ifExists(
    validations: Validator<DT>.() -> Unit
) = this@ifExists.validation {
    if (it != null) {
        Validator<DT>().apply(validations).validateAll(it)
    } else {
        true
    }
}

fun <DT : Any> Validator<DT?>.mustExist(
    validations: Validator<DT>.() -> Unit
) = this@mustExist.validation {
    if (it != null) {
        Validator<DT>().apply(validations).validateAll(it)
    } else {
        false
    }
}
