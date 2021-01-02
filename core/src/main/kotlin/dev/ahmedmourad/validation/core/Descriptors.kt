package dev.ahmedmourad.validation.core

class ConstraintsDescriptor<T : Any> internal constructor(
    private val values: List<Constraint<T>>
) : List<Constraint<T>> by values

data class Constraint<T : Any> internal constructor(
    val violation: String,
    val validations: List<Validation<T>>,
    val params: List<Parameter<T, *>>
)

data class Parameter<T : Any, P> internal constructor(
    val name: String,
    val get: T.() -> P
)

data class Validation<DT> internal constructor(val validate: DT.() -> Boolean)
