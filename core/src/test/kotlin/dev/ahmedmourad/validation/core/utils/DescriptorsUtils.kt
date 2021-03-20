package dev.ahmedmourad.validation.core.utils

import dev.ahmedmourad.validation.core.Constrains
import dev.ahmedmourad.validation.core.IncludedConstraints
import org.jetbrains.annotations.TestOnly

data class IncludedConstraintsTestWrapper<I, T : Any, DT : Any, C : Constrains<DT>> @TestOnly constructor(
    val constraints: IncludedConstraints<T, DT, C>,
    val tester: IncludedConstraints<T, DT, C>.(I) -> Boolean
)

@TestOnly
fun <I, T : Any, DT : Any, C : Constrains<DT>> IncludedConstraints<T, DT, C>.with(
    tester: IncludedConstraints<T, DT, C>.(I) -> Boolean
): IncludedConstraintsTestWrapper<I, T, DT, C> {
    return IncludedConstraintsTestWrapper(this, tester)
}

@TestOnly
fun <I, T : Any, DT : Any, C : Constrains<DT>> IncludedConstraintsTestWrapper<I, T, DT, C>.allMatch(
    vararg items: I
): IncludedConstraintsTestWrapper<I, T, DT, C> {
    val illegalValues = items.filterNot { item ->
        this.tester(this.constraints, item)
    }
    if (illegalValues.isNotEmpty()) {
        throw AssertionError(
            "Constraints do not match for items: {\n\t${illegalValues.joinToString(",\n\t")}\n}"
        )
    }
    return this
}

@TestOnly
fun <I, T : Any, DT : Any, C : Constrains<DT>> IncludedConstraintsTestWrapper<I, T, DT, C>.allFail(
    vararg items: I
): IncludedConstraintsTestWrapper<I, T, DT, C> {
    val illegalValues = items.filter { item ->
        this.tester(this.constraints, item)
    }
    if (illegalValues.isNotEmpty()) {
        throw AssertionError(
            "Constraints do not fail for items: {\n\t${illegalValues.joinToString(",\n\t")}\n}"
        )
    }
    return this
}