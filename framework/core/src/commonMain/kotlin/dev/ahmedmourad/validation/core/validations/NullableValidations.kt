package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint
import dev.ahmedmourad.validation.core.ScopedConstraintBuilder

fun <DT : Any> Constraint<DT?>.ifExists(
    validations: Constraint<DT>.() -> Unit
) = this@ifExists.validation {
    if (it != null) {
        ScopedConstraintBuilder<DT>().apply(validations).validateAll(it)
    } else {
        true
    }
}

fun <DT : Any> Constraint<DT?>.mustExist(
    validations: Constraint<DT>.() -> Unit
) = this@mustExist.validation {
    if (it != null) {
        ScopedConstraintBuilder<DT>().apply(validations).validateAll(it)
    } else {
        false
    }
}

fun <DT : Any> Constraint<DT?>.exists() = validation {
    it != null
}

fun <DT : Any> Constraint<DT?>.doesNotExist() = validation {
    it == null
}
