package dev.ahmedmourad.validation.core

import org.jetbrains.annotations.TestOnly

@TestOnly
fun <DT> validator(validations: Validator<DT>.() -> Unit): Validator<DT> {
    return Validator<DT>().apply(validations)
}

@TestOnly
fun <DT> Validator<DT>.allMatch(vararg items: DT): Validator<DT> {
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

@TestOnly
fun <DT> Validator<DT>.allFail(vararg items: DT): Validator<DT> {
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
