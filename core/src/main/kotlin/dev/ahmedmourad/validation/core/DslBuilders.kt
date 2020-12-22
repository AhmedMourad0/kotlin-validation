package dev.ahmedmourad.validation.core

import kotlin.reflect.KProperty1

@DslMarker
annotation class ValidationMarker

@ValidationMarker
interface Constrains<T : Any> {
    val constraints: ConstraintsDescriptor<T>
}

@ValidationMarker
class ConstraintsBuilder<T : Any> internal constructor() {

    private val constraints: MutableList<Constraint<T>> = mutableListOf()

    fun constraint(
        violation: String,
        description: ConstraintBuilder<T>.() -> Unit
    ) {
        this.add(ConstraintBuilder<T>(violation).apply(description).build())
    }

    private fun add(constraint: Constraint<T>) {
        constraints.add(constraint)
    }

    internal fun build(): ConstraintsDescriptor<T> = ConstraintsDescriptor(constraints)
}

@ValidationMarker
class ConstraintBuilder<T : Any> internal constructor(private val violation: String) {

    private val validations: MutableList<Validation<T>> = mutableListOf()
    private val params: MutableList<Parameter<T, *>> = mutableListOf()
    private val validators: MutableList<Validator<T, *>> = mutableListOf()

    fun <P> param(name: String, get: T.() -> P) {
        this.add(Parameter(name, get))
    }

    fun script(validate: T.() -> Boolean) {
        this.add(Validation(validate))
    }

    fun <DT> on(
        property: KProperty1<T, DT>,
        validator: ValidatorBuilder<T, DT>.() -> Unit
    ) {
        this.add(ValidatorBuilder(property).apply(validator).build())
    }

    private fun add(validation: Validation<T>) {
        validations.add(validation)
    }

    private fun <P> add(param: Parameter<T, P>) {
        params.add(param)
    }

    private fun <DT> add(validator: Validator<T, DT>) {
        validators.add(validator)
    }

    internal fun build(): Constraint<T> = Constraint(violation, validations, params, validators)
}

@ValidationMarker
class ValidatorBuilder<T : Any, DT> internal constructor(
    private val property: KProperty1<T, DT>
) {

    private val validations: MutableList<Validation<DT>> = mutableListOf()

    fun validation(validate: DT.() -> Boolean) {
        add(Validation(validate))
    }

    private fun add(validation: Validation<DT>) {
        validations.add(validation)
    }

    internal fun build(): Validator<T, DT> = Validator(property, validations)
}

@Suppress("unused")
// The receiver is needed to ensure dsl integrity
// This lives outside of the interface to prevent overriding it
fun <T : Any> Constrains<T>.describe(
    description: ConstraintsBuilder<T>.() -> Unit
): Lazy<ConstraintsDescriptor<T>> {
    return lazy { ConstraintsBuilder<T>().apply(description).build() }
}
