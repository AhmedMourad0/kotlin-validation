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

    internal fun add(validation: Validation<T>) {
        validations.add(validation)
    }

    fun <P> param(name: String, get: T.() -> P) {
        params.add(Parameter(name, get))
    }

    fun <DT : Any> on(
        property: KProperty1<T, DT>,
        validator: Validator<DT>.() -> Unit
    ) = validation {
        Validator<DT>().apply(validator).validateAll(property.get(it))
    }

    fun <DT : Any> on(property: KProperty1<T, DT?>): PropertyNullableValidator<T, DT> {
        return PropertyNullableValidator(property)
    }

    infix fun <DT : Any> PropertyNullableValidator<T, DT>.ifExists(
        validations: Validator<DT>.() -> Unit
    ) = this@ConstraintBuilder.validation {
        val item = this@ifExists.property.get(it)
        if (item != null) {
            Validator<DT>().apply(validations).validateAll(item)
        } else {
            true
        }
    }

    infix fun <DT : Any> PropertyNullableValidator<T, DT>.mustExist(
        validations: Validator<DT>.() -> Unit
    ) = this@ConstraintBuilder.validation {
        val item = this@mustExist.property.get(it)
        if (item != null) {
            Validator<DT>().apply(validations).validateAll(item)
        } else {
            false
        }
    }

    internal fun build(): Constraint<T> = Constraint(violation, validations, params)
}

fun <T : Any> ConstraintBuilder<T>.validation(validate: (T) -> Boolean) {
    add(Validation(validate))
}

@ValidationMarker
class Validator<DT> {

    private val validations: MutableList<Validation<DT>> = mutableListOf()

    internal fun add(validation: Validation<DT>) {
        validations.add(validation)
    }

    fun <DT1 : Any> on(
        property: KProperty1<DT, DT1>,
        validator: Validator<DT1>.() -> Unit
    ) = validation {
        Validator<DT1>().apply(validator).validateAll(property.get(it))
    }

    fun <DT1 : Any> on(property: KProperty1<DT, DT1?>): PropertyNullableValidator<DT, DT1> {
        return PropertyNullableValidator(property)
    }


    infix fun <DT1 : Any> PropertyNullableValidator<DT, DT1>.ifExists(
        validations: Validator<DT1>.() -> Unit
    ) = this@Validator.validation {
        val item = this@ifExists.property.get(it)
        if (item != null) {
            Validator<DT1>().apply(validations).validateAll(item)
        } else {
            true
        }
    }

    infix fun <DT1 : Any> PropertyNullableValidator<DT, DT1>.mustExist(
        validations: Validator<DT1>.() -> Unit
    ) = this@Validator.validation {
        val item = this@mustExist.property.get(it)
        if (item != null) {
            Validator<DT1>().apply(validations).validateAll(item)
        } else {
            false
        }
    }

    inline fun <reified DT1 : DT> ifIs(
        crossinline validator: Validator<DT1>.() -> Unit
    ) = validation {
        if (it is DT1) {
            Validator<DT1>().apply { validator() }.validateNone(it)
        } else {
            true
        }
    }

    inline fun <reified DT1 : DT> mustBeA(
        crossinline validator: Validator<DT1>.() -> Unit
    ) = validation {
        if (it is DT1) {
            Validator<DT1>().apply { validator() }.validateNone(it)
        } else {
            false
        }
    }

    fun validateNone(item: DT): Boolean {
        return validations.none { it.validate(item) }
    }

    fun validateAny(item: DT): Boolean {
        return validations.any { it.validate(item) }
    }

    fun validateAll(item: DT): Boolean {
        return validations.all { it.validate(item) }
    }
}

fun <DT> Validator<DT>.validation(validate: (DT) -> Boolean) {
    add(Validation(validate))
}

@ValidationMarker
class PropertyNullableValidator<T, DT : Any> internal constructor(
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
