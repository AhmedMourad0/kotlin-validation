package dev.ahmedmourad.validation.core

class ConstraintsDescriptor<T : Any> internal constructor(
    private val values: List<Constraint<T>>
) : List<Constraint<T>> by values

data class Constraint<T : Any> internal constructor(
    val violation: String,
    val includedConstraints: List<IncludedConstraints<T, *, *>>,
    val validations: List<Validation<T>>,
    val params: List<Parameter<T, *>>
)

data class IncludedConstraints<T : Any, DT : Any, C : Constrains<DT>>(
    val param: String,
    val constrained: (T) -> DT?,
    val constrainer: (T) -> C
) {

    inline fun isValid(
        item: T,
        crossinline isValid: C.(DT) -> Boolean
    ): Boolean {
        return constrained(item)?.let {
            constrainer(item).isValid(it)
        } ?: true
    }

    inline fun <V : Any> findViolations(
        item: T,
        crossinline validate: C.(DT) -> Case<List<V>, DT>
    ): List<V> {
        return constrained(item)?.let {
            constrainer(item).validate(it).swap().orElse { emptyList() }
        }.orEmpty()
    }
}

data class Parameter<T : Any, P> internal constructor(
    val name: String,
    val get: (T) -> P
)

data class Validation<DT> internal constructor(val validate: (DT) -> Boolean)
