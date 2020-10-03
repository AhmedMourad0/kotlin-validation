package dev.ahmedmourad.validation.core

import kotlin.reflect.KProperty1

@DslMarker
annotation class ValidationMarker

@ValidationMarker
interface Constrains<T : Any> {
    val constraints: ConstraintsDescriptor<T>
}

data class ConstraintsDescriptor<T : Any> internal constructor(
    private val constraints: List<Constraint<T>>
)
internal data class Constraint<T : Any>(
    private val violation: String,
    private val validations: List<Validation<*>>,
    private val params: List<Parameter<T, *>>,
    private val validators: List<Validator<T, *>>
)
internal data class Parameter<T : Any, P>(private val name: String, private val get: T.() -> P)
internal data class Validation<X>(private val validate: X.() -> Boolean)
internal data class Validator<T : Any, DT>(
    private val property: KProperty1<T, DT>,
    private val validations: List<Validation<DT>>
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
    private val validations: MutableList<Validation<*>> = mutableListOf()
    private val params: MutableList<Parameter<T, *>> = mutableListOf()
    private val validators: MutableList<Validator<T, *>> = mutableListOf()
    internal fun <X> add(validation: Validation<X>) {
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
fun <T : Any> Constrains<T>.constraints(description: ConstraintsBuilder<T>.() -> Unit): ConstraintsDescriptor<T> {
    return ConstraintsBuilder<T>().apply(description).build()
}

fun <T : Any> ConstraintsBuilder<T>.constraint(violation: String, description: ConstraintBuilder<T>.() -> Unit) {
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
