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
interface Validator<DT> {

    fun validation(validate: (DT) -> Boolean)

    fun <DT1> on(property: (DT) -> DT1, validator: Validator<DT1>.() -> Unit)
    fun <DT1> on(property: KProperty0<DT1>, validator: Validator<DT1>.() -> Unit)
    fun <DT1 : Any> on(property: (DT) -> DT1?): PropertyNullableValidator<DT, DT1>
    fun <DT1 : Any> on(property: KProperty0<DT1?>): PropertyNullableValidator<DT, DT1>

    infix fun <DT1 : Any> PropertyNullableValidator<DT, DT1>.ifExists(
        validations: Validator<DT1>.() -> Unit
    )

    infix fun <DT1 : Any> PropertyNullableValidator<DT, DT1>.mustExist(
        validations: Validator<DT1>.() -> Unit
    )
}

@ValidationDslMarker
class ConstraintBuilder<T : Any> internal constructor(
    private val violation: String
) : Validator<T> {

    private val includedConstraints: MutableList<IncludedConstraints<T, *, *>> = mutableListOf()
    private val validations: MutableList<Validation<T>> = mutableListOf()
    private val params: MutableList<Parameter<T, *>> = mutableListOf()

    override fun validation(validate: (T) -> Boolean) {
        validations.add(Validation(validate))
    }

    @Param
    fun <@ParamType P> param(@ParamName name: String, get: (T) -> P) {
        params.add(Parameter(name, get))
    }

    //TODO: combine both parameters of constrainer into a single object
    @Param
    fun <T1 : Any, @InclusionType @ParamType C : Constrains<T1>> include(
        @ParamName param: String,
        property: (T) -> T1?,
        constrainer: (T, T1) -> C
    ) {
        includedConstraints.add(IncludedConstraints(param, property, constrainer))
    }

    @Param
    fun <T1 : Any, @InclusionType @ParamType C : Constrains<T1>> include(
        @ParamName param: String,
        property: KProperty0<T1?>,
        constrainer: (T, T1) -> C
    ) {
        include(param, { property.get() }, constrainer)
    }

    override fun <DT> on(
        property: (T) -> DT,
        validator: Validator<DT>.() -> Unit
    ) = validation {
        ValidatorImpl<DT>().apply(validator).validateAll(property.invoke(it))
    }

    override fun <DT> on(
        property: KProperty0<DT>,
        validator: Validator<DT>.() -> Unit
    ) = validation {
        ValidatorImpl<DT>().apply(validator).validateAll(property.get())
    }

    override fun <DT : Any> on(property: (T) -> DT?): PropertyNullableValidator<T, DT> {
        return PropertyNullableValidator(property)
    }

    override fun <DT : Any> on(property: KProperty0<DT?>): PropertyNullableValidator<T, DT> {
        return on { property.get() }
    }

    override infix fun <DT : Any> PropertyNullableValidator<T, DT>.ifExists(
        validations: Validator<DT>.() -> Unit
    ) = this@ConstraintBuilder.validation {
        val item = this@ifExists.get(it)
        if (item != null) {
            ValidatorImpl<DT>().apply(validations).validateAll(item)
        } else {
            true
        }
    }

    override infix fun <DT : Any> PropertyNullableValidator<T, DT>.mustExist(
        validations: Validator<DT>.() -> Unit
    ) = this@ConstraintBuilder.validation {
        val item = this@mustExist.get(it)
        if (item != null) {
            ValidatorImpl<DT>().apply(validations).validateAll(item)
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

@ValidationDslMarker
class ValidatorImpl<DT> : Validator<DT> {

    private val validations: MutableList<Validation<DT>> = mutableListOf()

    override fun validation(validate: (DT) -> Boolean) {
        validations.add(Validation(validate))
    }

    override fun <DT1> on(
        property: (DT) -> DT1,
        validator: Validator<DT1>.() -> Unit
    ) = validation {
        ValidatorImpl<DT1>().apply(validator).validateAll(property.invoke(it))
    }

    override fun <DT1> on(
        property: KProperty0<DT1>,
        validator: Validator<DT1>.() -> Unit
    ) = validation {
        ValidatorImpl<DT1>().apply(validator).validateAll(property.get())
    }

    override fun <DT1 : Any> on(property: (DT) -> DT1?): PropertyNullableValidator<DT, DT1> {
        return PropertyNullableValidator(property)
    }

    override fun <DT1 : Any> on(property: KProperty0<DT1?>): PropertyNullableValidator<DT, DT1> {
        return on { property.get() }
    }

    override infix fun <DT1 : Any> PropertyNullableValidator<DT, DT1>.ifExists(
        validations: Validator<DT1>.() -> Unit
    ) = this@ValidatorImpl.validation {
        val item = this@ifExists.get(it)
        if (item != null) {
            ValidatorImpl<DT1>().apply(validations).validateAll(item)
        } else {
            true
        }
    }

    override infix fun <DT1 : Any> PropertyNullableValidator<DT, DT1>.mustExist(
        validations: Validator<DT1>.() -> Unit
    ) = this@ValidatorImpl.validation {
        val item = this@mustExist.get(it)
        if (item != null) {
            ValidatorImpl<DT1>().apply(validations).validateAll(item)
        } else {
            false
        }
    }

    fun validateAll(item: DT): Boolean {
        return validations.all { it.validate(item) }
    }

    fun validateAny(item: DT): Boolean {
        return validations.any { it.validate(item) }
    }

    fun validateNone(item: DT): Boolean {
        return validations.none { it.validate(item) }
    }
}

@ValidationDslMarker
class PropertyNullableValidator<T, DT : Any> internal constructor(
    private val get: (T) -> DT?
) {
    internal fun get(owner: T)  = get.invoke(owner)
}

@Suppress("unused")
// The receiver is needed to ensure dsl integrity
// This lives outside of the interface to prevent overriding it
fun <T : Any> Constrains<T>.describe(
    description: ConstraintsBuilder<T>.() -> Unit
): Lazy<ConstraintsDescriptor<T>> {
    return lazy { ConstraintsBuilder<T>().apply(description).build() }
}
