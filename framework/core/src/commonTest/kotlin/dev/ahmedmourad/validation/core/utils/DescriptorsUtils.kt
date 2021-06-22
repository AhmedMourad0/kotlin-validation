package dev.ahmedmourad.validation.core.utils

import dev.ahmedmourad.validation.core.Validator
import dev.ahmedmourad.validation.core.IncludedValidatorDescriptor

data class IncludedValidatorTestWrapper<I, T : Any, DT : Any, C : Validator<DT>>(
    val validator: IncludedValidatorDescriptor<T, DT, C>,
    val tester: IncludedValidatorDescriptor<T, DT, C>.(I) -> Boolean
)

fun <I, T : Any, DT : Any, C : Validator<DT>> IncludedValidatorDescriptor<T, DT, C>.with(
    tester: IncludedValidatorDescriptor<T, DT, C>.(I) -> Boolean
): IncludedValidatorTestWrapper<I, T, DT, C> {
    return IncludedValidatorTestWrapper(this, tester)
}

fun <I, T : Any, DT : Any, C : Validator<DT>> IncludedValidatorTestWrapper<I, T, DT, C>.allMatch(
    vararg items: I
): IncludedValidatorTestWrapper<I, T, DT, C> {
    val illegalValues = items.filterNot { item ->
        this.tester(this.validator, item)
    }
    if (illegalValues.isNotEmpty()) {
        throw AssertionError(
            "Constraints do not match for items: {\n\t${illegalValues.joinToString(",\n\t")}\n}"
        )
    }
    return this
}

fun <I, T : Any, DT : Any, C : Validator<DT>> IncludedValidatorTestWrapper<I, T, DT, C>.allFail(
    vararg items: I
): IncludedValidatorTestWrapper<I, T, DT, C> {
    val illegalValues = items.filter { item ->
        this.tester(this.validator, item)
    }
    if (illegalValues.isNotEmpty()) {
        throw AssertionError(
            "Constraints do not fail for items: {\n\t${illegalValues.joinToString(",\n\t")}\n}"
        )
    }
    return this
}
