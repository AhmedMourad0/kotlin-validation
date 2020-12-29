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
        constraints.add(ConstraintBuilder<T>(violation).apply(description).build())
    }

    internal fun build(): ConstraintsDescriptor<T> = ConstraintsDescriptor(constraints)
}

@ValidationMarker
class ConstraintBuilder<T : Any> internal constructor(private val violation: String) {

    private val validations: MutableList<Validation<T>> = mutableListOf()
    private val params: MutableList<Parameter<T, *>> = mutableListOf()

    fun <P> param(name: String, get: T.() -> P) {
        params.add(Parameter(name, get))
    }

    fun script(validate: T.() -> Boolean) {
        validations.add(Validation(validate))
    }

    fun <DT : Any> on(
        property: KProperty1<T, DT>,
        validator: ValidatorBuilder<DT>.() -> Unit
    ) = script {
        ValidatorBuilder<DT>().apply(validator).validate(property.get(this))
    }

    fun <DT : Any> on(property: KProperty1<T, DT?>): NullableValidatorBuilder<T, DT> {
        return NullableValidatorBuilder(property)
    }

    infix fun <DT : Any> NullableValidatorBuilder<T, DT>.ifExists(
        validations: ValidatorBuilder<DT>.() -> Unit
    ) = this@ConstraintBuilder.script {
        val item = this@ifExists.property.get(this)
        if (item != null) {
            ValidatorBuilder<DT>().apply(validations).validate(item)
        } else {
            true
        }
    }

    internal fun build(): Constraint<T> = Constraint(violation, validations, params)
}

@ValidationMarker
class ValidatorBuilder<DT : Any> internal constructor() {

    private val validations: MutableList<Validation<DT>> = mutableListOf()

    fun validation(validate: DT.() -> Boolean) {
        validations.add(Validation(validate))
    }

    fun <DT1 : Any> on(
        property: KProperty1<DT, DT1>,
        validator: ValidatorBuilder<DT1>.() -> Unit
    ) = validation {
        ValidatorBuilder<DT1>().apply(validator).validate(property.get(this))
    }

    fun <DT1 : Any> on(property: KProperty1<DT, DT1?>): NullableValidatorBuilder<DT, DT1> {
        return NullableValidatorBuilder(property)
    }

    infix fun <DT1 : Any> NullableValidatorBuilder<DT, DT1>.ifExists(
        validations: ValidatorBuilder<DT1>.() -> Unit
    ) = this@ValidatorBuilder.validation {
        val item = this@ifExists.property.get(this)
        if (item != null) {
            ValidatorBuilder<DT1>().apply(validations).validate(item)
        } else {
            true
        }
    }

    internal fun validate(item: DT): Boolean {
        return validations.all { it.validate(item) }
    }
}

@ValidationMarker
class NullableValidatorBuilder<T : Any, DT : Any> internal constructor(
    internal val property: KProperty1<T, DT?>
)

@Suppress("unused")
// The receiver is needed to ensure dsl integrity
// This lives outside of the interface to prevent overriding it
fun <T : Any> Constrains<T>.describe(
    description: ConstraintsBuilder<T>.() -> Unit
): Lazy<ConstraintsDescriptor<T>> {
    return lazy { ConstraintsBuilder<T>().apply(description).build() }
}
