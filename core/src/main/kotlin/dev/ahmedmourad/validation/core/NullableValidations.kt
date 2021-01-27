package dev.ahmedmourad.validation.core

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
