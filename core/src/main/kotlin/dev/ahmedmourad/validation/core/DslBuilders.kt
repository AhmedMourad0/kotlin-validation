package dev.ahmedmourad.validation.core

import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KProperty0

@DslMarker
@Target(CLASS)
@Retention(BINARY)
private annotation class ValidationDslMarker

@Target(FUNCTION)
@Retention(BINARY)
private annotation class Param

@Target(VALUE_PARAMETER, TYPE)
@Retention(BINARY)
private annotation class ParamName

@Target(TYPE_PARAMETER)
@Retention(BINARY)
private annotation class ParamType

@Target(TYPE_PARAMETER)
@Retention(BINARY)
private annotation class InclusionType

@ValidationDslMarker
interface Constrains<T : Any> {
    val constraints: ConstraintsDescriptor<T>
}

@ValidationDslMarker
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

@ValidationDslMarker
class ConstraintBuilder<T : Any> internal constructor(private val violation: String) {

    private val includedConstraints: MutableList<IncludedConstraints<T, *, *>> = mutableListOf()
    private val validations: MutableList<Validation<T>> = mutableListOf()
    private val params: MutableList<Parameter<T, *>> = mutableListOf()

    internal fun add(validation: Validation<T>) {
        validations.add(validation)
    }

    @Param
    fun <@ParamType P> param(@ParamName name: String, get: (T) -> P) {
        params.add(Parameter(name, get))
    }

    @Param
    fun <T1 : Any, @InclusionType @ParamType C : Constrains<T1>> include(
        @ParamName param: String,
        property: (T) -> T1?,
        constrainer: (T) -> C
    ) {
        includedConstraints.add(IncludedConstraints(param, property, constrainer))
    }

    @Param
    fun <T1 : Any, @InclusionType @ParamType C : Constrains<T1>> include(
        @ParamName param: String,
        property: KProperty0<T1?>,
        constrainer: (T) -> C
    ) {
        includedConstraints.add(IncludedConstraints(param, { property.get() }, constrainer))
    }

    inline fun <DT : Any> on(
        crossinline property: (T) -> DT,
        crossinline validator: Validator<DT>.() -> Unit
    ) = validation {
        Validator<DT>().apply(validator).validateAll(property.invoke(it))
    }

    inline fun <DT : Any> on(
        property: KProperty0<DT>,
        crossinline validator: Validator<DT>.() -> Unit
    ) = validation {
        Validator<DT>().apply(validator).validateAll(property.get())
    }

    fun <DT : Any> on(property: (T) -> DT?): PropertyNullableValidator<T, DT> {
        return PropertyNullableValidator(property)
    }

    fun <DT : Any> on(property: KProperty0<DT?>): PropertyNullableValidator<T, DT> {
        return PropertyNullableValidator { property.get() }
    }

    infix fun <DT : Any> PropertyNullableValidator<T, DT>.ifExists(
        validations: Validator<DT>.() -> Unit
    ) = this@ConstraintBuilder.validation {
        val item = this@ifExists.get(it)
        if (item != null) {
            Validator<DT>().apply(validations).validateAll(item)
        } else {
            true
        }
    }

    infix fun <DT : Any> PropertyNullableValidator<T, DT>.mustExist(
        validations: Validator<DT>.() -> Unit
    ) = this@ConstraintBuilder.validation {
        val item = this@mustExist.get(it)
        if (item != null) {
            Validator<DT>().apply(validations).validateAll(item)
        } else {
            false
        }
    }

    internal fun build(): Constraint<T> = Constraint(
        violation,
        includedConstraints,
        validations,
        params
    )
}

fun <T : Any> ConstraintBuilder<T>.validation(validate: (T) -> Boolean) {
    add(Validation(validate))
}

@ValidationDslMarker
class Validator<DT> {

    private val validations: MutableList<Validation<DT>> = mutableListOf()

    internal fun add(validation: Validation<DT>) {
        validations.add(validation)
    }

    inline fun <DT1 : Any> on(
        crossinline property: (DT) -> DT1,
        crossinline validator: Validator<DT1>.() -> Unit
    ) = validation {
        Validator<DT1>().apply(validator).validateAll(property.invoke(it))
    }

    inline fun <DT1 : Any> on(
        property: KProperty0<DT1>,
        crossinline validator: Validator<DT1>.() -> Unit
    ) = validation {
        Validator<DT1>().apply(validator).validateAll(property.get())
    }

    fun <DT1 : Any> on(property: (DT) -> DT1?): PropertyNullableValidator<DT, DT1> {
        return PropertyNullableValidator(property)
    }

    fun <DT1 : Any> on(property: KProperty0<DT1?>): PropertyNullableValidator<DT, DT1> {
        return PropertyNullableValidator { property.get() }
    }

    infix fun <DT1 : Any> PropertyNullableValidator<DT, DT1>.ifExists(
        validations: Validator<DT1>.() -> Unit
    ) = this@Validator.validation {
        val item = this@ifExists.get(it)
        if (item != null) {
            Validator<DT1>().apply(validations).validateAll(item)
        } else {
            true
        }
    }

    infix fun <DT1 : Any> PropertyNullableValidator<DT, DT1>.mustExist(
        validations: Validator<DT1>.() -> Unit
    ) = this@Validator.validation {
        val item = this@mustExist.get(it)
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

@ValidationDslMarker
class PropertyNullableValidator<T, DT : Any> internal constructor(
    internal val get: (T) -> DT?
)

@Suppress("unused")
// The receiver is needed to ensure dsl integrity
// This lives outside of the interface to prevent overriding it
fun <T : Any> Constrains<T>.describe(
    description: ConstraintsBuilder<T>.() -> Unit
): Lazy<ConstraintsDescriptor<T>> {
    return lazy { ConstraintsBuilder<T>().apply(description).build() }
}
