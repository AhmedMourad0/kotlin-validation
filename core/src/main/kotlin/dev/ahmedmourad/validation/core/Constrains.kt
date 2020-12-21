package dev.ahmedmourad.validation.core

import kotlin.reflect.KProperty1

@DslMarker
annotation class ValidationMarker

@ValidationMarker
interface Constrains<T : Any> {
    val constraints: ConstraintsDescriptor<T>
}

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
data class Validator<T : Any, DT> internal constructor(
    val property: KProperty1<T, DT>,
    val validations: List<Validation<DT>>
)

@ValidationMarker
class ConstraintsBuilder<T : Any> internal constructor() {
    private val constraints: MutableList<Constraint<T>> = mutableListOf()
    internal fun add(constraint: Constraint<T>) {
        constraints.add(constraint)
    }
    internal fun build(): ConstraintsDescriptor<T> = ConstraintsDescriptor(constraints)
}

@ValidationMarker
class ConstraintBuilder<T : Any> internal constructor(private val violation: String) {
    private val validations: MutableList<Validation<T>> = mutableListOf()
    private val params: MutableList<Parameter<T, *>> = mutableListOf()
    private val validators: MutableList<Validator<T, *>> = mutableListOf()
    internal fun add(validation: Validation<T>) {
        validations.add(validation)
    }
    internal fun <P> add(param: Parameter<T, P>) {
        params.add(param)
    }
    internal fun <DT> add(validator: Validator<T, DT>) {
        validators.add(validator)
    }
    internal fun build(): Constraint<T> = Constraint(violation, validations, params, validators)
}

@ValidationMarker
class ValidatorBuilder<T : Any, DT> internal constructor(
    private val property: KProperty1<T, DT>
) {
    private val validations: MutableList<Validation<DT>> = mutableListOf()
    internal fun add(validation: Validation<DT>) {
        validations.add(validation)
    }
    internal fun build(): Validator<T, DT> = Validator(property, validations)
}

@Suppress("unused")
// The receiver is needed to ensure dsl integrity
fun <T : Any> Constrains<T>.describe(
    description: ConstraintsBuilder<T>.() -> Unit
): Lazy<ConstraintsDescriptor<T>> {
    return lazy { ConstraintsBuilder<T>().apply(description).build() }
}

fun <T : Any> ConstraintsBuilder<T>.constraint(
    violation: String,
    description: ConstraintBuilder<T>.() -> Unit
) {
    this.add(ConstraintBuilder<T>(violation).apply(description).build())
}

fun <T : Any, P> ConstraintBuilder<T>.param(name: String, get: T.() -> P) {
    this.add(Parameter(name, get))
}

fun <T : Any> ConstraintBuilder<T>.script(validate: T.() -> Boolean) {
    this.add(Validation(validate))
}

fun <T : Any, DT> ConstraintBuilder<T>.on(
    property: KProperty1<T, DT>,
    validator: ValidatorBuilder<T, DT>.() -> Unit
) {
    this.add(ValidatorBuilder(property).apply(validator).build())
}

fun <T : Any, DT> ValidatorBuilder<T, DT>.validation(validate: DT.() -> Boolean) {
    add(Validation(validate))
}

fun <T : Any> ValidatorBuilder<T, String>.minLength(min: Int) = validation {
    length >= min
}

sealed class Case<out V : Any, out T : Any> {
    data class Illegal<V : Any>(val v: V) : Case<V, Nothing>()
    data class Legal<T : Any>(val v: T) : Case<Nothing, T>()
}

fun <T : Any> T.legal(): Case<Nothing, T> {
    return Case.Legal(this)
}

fun <V : Any> V.illegal(): Case<V, Nothing> {
    return Case.Illegal(this)
}
