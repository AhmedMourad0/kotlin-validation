package dev.ahmedmourad.validation.core.utils

import dev.ahmedmourad.validation.core.Constraint
import dev.ahmedmourad.validation.core.ScopedConstraintBuilder

fun <DT> constraint(constraint: Constraint<DT>.() -> Unit): ScopedConstraintBuilder<DT> {
    return ScopedConstraintBuilder<DT>().apply(constraint)
}

fun <DT> ScopedConstraintBuilder<DT>.allMatch(vararg items: DT): ScopedConstraintBuilder<DT> {
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

fun <DT> ScopedConstraintBuilder<DT>.allFail(vararg items: DT): ScopedConstraintBuilder<DT> {
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
