package dev.ahmedmourad.validation.core

fun <DT : Any> Validator<DT?>.ifExists(
    validations: Validator<DT>.() -> Unit
) = this@ifExists.validation {
    if (this@validation != null) {
        Validator<DT>().apply(validations).validate(this@validation)
    } else {
        true
    }
}

fun <DT : Any> Validator<DT?>.mustExist(
    validations: Validator<DT>.() -> Unit
) = this@mustExist.validation {
    if (this@validation != null) {
        Validator<DT>().apply(validations).validate(this@validation)
    } else {
        false
    }
}
