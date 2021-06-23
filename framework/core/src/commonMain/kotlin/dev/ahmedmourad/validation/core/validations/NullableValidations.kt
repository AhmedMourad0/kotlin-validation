package dev.ahmedmourad.validation.core.validations

import dev.ahmedmourad.validation.core.Constraint
import dev.ahmedmourad.validation.core.ScopedConstraintBuilder

fun <DT : Any> Constraint<DT?>.ifExists(
    validations: Constraint<DT>.() -> Unit
) = this@ifExists.validation {
    if (subject != null) {
        ScopedConstraintBuilder<DT>().apply(validations).validateAll(subject)
    } else {
        true
    }
}

fun <DT : Any> Constraint<DT?>.mustExist(
    validations: Constraint<DT>.() -> Unit
) = this@mustExist.validation {
    if (subject != null) {
        ScopedConstraintBuilder<DT>().apply(validations).validateAll(subject)
    } else {
        false
    }
}

fun <DT : Any> Constraint<DT?>.exists() = validation {
    subject != null
}

fun <DT : Any> Constraint<DT?>.doesNotExist() = validation {
    subject == null
}
