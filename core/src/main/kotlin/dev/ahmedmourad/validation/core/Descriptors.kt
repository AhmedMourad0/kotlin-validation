package dev.ahmedmourad.validation.core

import kotlin.reflect.KProperty1

class ConstraintsDescriptor<T : Any> internal constructor(
    private val values: List<Constraint<T>>
) : List<Constraint<T>> by values

data class Constraint<T : Any> internal constructor(
    val violation: String,
    val validations: List<Validation<T>>,
    val params: List<Parameter<T, *>>,
    val validators: List<Validator<T, *>>
)

data class Parameter<T : Any, P> internal constructor(
    val name: String,
    val get: T.() -> P
)

data class Validation<X> internal constructor(val validate: X.() -> Boolean)

data class Validator<T, DT> internal constructor(
    val property: KProperty1<T, DT>,
    val validations: List<Validation<DT>>
)
