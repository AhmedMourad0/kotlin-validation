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
private annotation class Meta

@Target(VALUE_PARAMETER, TYPE)
@Retention(BINARY)
private annotation class MetaName

@Target(TYPE_PARAMETER)
@Retention(BINARY)
private annotation class MetaType

@Target(TYPE_PARAMETER)
@Retention(BINARY)
private annotation class InclusionType

@ValidationDslMarker
interface Validator<T : Any> {
    val constraints: ValidatorDescriptor<T>
}

@ValidationDslMarker
class ConstraintsBuilder<T : Any> internal constructor() {

    private val constraints: MutableList<ConstraintDescriptor<T>> = mutableListOf()

    fun constraint(
        violation: String,
        description: ConstraintBuilder<T>.() -> Unit
    ) {
        constraints.add(ConstraintBuilder<T>(violation).apply(description).build())
    }

    internal fun build(): ValidatorDescriptor<T> = ValidatorDescriptor(constraints)
}

@ValidationDslMarker
interface Constraint<DT> {

    fun validation(validate: (DT) -> Boolean)

    fun <DT1> on(property: (DT) -> DT1, constraint: Constraint<DT1>.() -> Unit)
    fun <DT1> on(property: KProperty0<DT1>, constraint: Constraint<DT1>.() -> Unit)
    fun <DT1 : Any> on(property: (DT) -> DT1?): NullablePropertyScopedConstraint<DT, DT1>
    fun <DT1 : Any> on(property: KProperty0<DT1?>): NullablePropertyScopedConstraint<DT, DT1>

    infix fun <DT1 : Any> NullablePropertyScopedConstraint<DT, DT1>.ifExists(
        validations: Constraint<DT1>.() -> Unit
    )

    infix fun <DT1 : Any> NullablePropertyScopedConstraint<DT, DT1>.mustExist(
        validations: Constraint<DT1>.() -> Unit
    )
}

@ValidationDslMarker
class ConstraintBuilder<T : Any> internal constructor(
    private val violation: String
) : Constraint<T> {

    private val includedValidator: MutableList<IncludedValidatorDescriptor<T, *, *>> = mutableListOf()
    private val validations: MutableList<ValidationDescriptor<T>> = mutableListOf()
    private val metadata: MutableList<MetadataDescriptor<T, *>> = mutableListOf()

    override fun validation(validate: (T) -> Boolean) {
        validations.add(ValidationDescriptor(validate))
    }

    @Meta
    fun <@MetaType P> meta(@MetaName name: String, get: (T) -> P) {
        metadata.add(MetadataDescriptor(name, get))
    }

    //TODO: combine both parameters of validator into a single object
    @Meta
    fun <T1 : Any, @InclusionType @MetaType C : Validator<T1>> include(
        @MetaName meta: String,
        property: (T) -> T1?,
        validator: (T, T1) -> C
    ) {
        includedValidator.add(IncludedValidatorDescriptor(meta, property, validator))
    }

    @Meta
    fun <T1 : Any, @InclusionType @MetaType C : Validator<T1>> include(
        @MetaName meta: String,
        property: KProperty0<T1?>,
        validator: (T, T1) -> C
    ) {
        include(meta, { property.get() }, validator)
    }

    override fun <DT> on(
        property: (T) -> DT,
        constraint: Constraint<DT>.() -> Unit
    ) = validation {
        ScopedConstraintBuilder<DT>().apply(constraint).validateAll(property.invoke(it))
    }

    override fun <DT> on(
        property: KProperty0<DT>,
        constraint: Constraint<DT>.() -> Unit
    ) = validation {
        ScopedConstraintBuilder<DT>().apply(constraint).validateAll(property.get())
    }

    override fun <DT : Any> on(property: (T) -> DT?): NullablePropertyScopedConstraint<T, DT> {
        return NullablePropertyScopedConstraint(property)
    }

    override fun <DT : Any> on(property: KProperty0<DT?>): NullablePropertyScopedConstraint<T, DT> {
        return on { property.get() }
    }

    override infix fun <DT : Any> NullablePropertyScopedConstraint<T, DT>.ifExists(
        validations: Constraint<DT>.() -> Unit
    ) = this@ConstraintBuilder.validation {
        val item = this@ifExists.get(it)
        if (item != null) {
            ScopedConstraintBuilder<DT>().apply(validations).validateAll(item)
        } else {
            true
        }
    }

    override infix fun <DT : Any> NullablePropertyScopedConstraint<T, DT>.mustExist(
        validations: Constraint<DT>.() -> Unit
    ) = this@ConstraintBuilder.validation {
        val item = this@mustExist.get(it)
        if (item != null) {
            ScopedConstraintBuilder<DT>().apply(validations).validateAll(item)
        } else {
            false
        }
    }

    internal fun build(): ConstraintDescriptor<T> = ConstraintDescriptor(
        violation,
        includedValidator,
        validations,
        metadata
    )
}

@ValidationDslMarker
class ScopedConstraintBuilder<DT> : Constraint<DT> {

    private val validations: MutableList<ValidationDescriptor<DT>> = mutableListOf()

    override fun validation(validate: (DT) -> Boolean) {
        validations.add(ValidationDescriptor(validate))
    }

    override fun <DT1> on(
        property: (DT) -> DT1,
        constraint: Constraint<DT1>.() -> Unit
    ) = validation {
        ScopedConstraintBuilder<DT1>().apply(constraint).validateAll(property.invoke(it))
    }

    override fun <DT1> on(
        property: KProperty0<DT1>,
        constraint: Constraint<DT1>.() -> Unit
    ) = validation {
        ScopedConstraintBuilder<DT1>().apply(constraint).validateAll(property.get())
    }

    override fun <DT1 : Any> on(property: (DT) -> DT1?): NullablePropertyScopedConstraint<DT, DT1> {
        return NullablePropertyScopedConstraint(property)
    }

    override fun <DT1 : Any> on(property: KProperty0<DT1?>): NullablePropertyScopedConstraint<DT, DT1> {
        return on { property.get() }
    }

    override infix fun <DT1 : Any> NullablePropertyScopedConstraint<DT, DT1>.ifExists(
        validations: Constraint<DT1>.() -> Unit
    ) = this@ScopedConstraintBuilder.validation {
        val item = this@ifExists.get(it)
        if (item != null) {
            ScopedConstraintBuilder<DT1>().apply(validations).validateAll(item)
        } else {
            true
        }
    }

    override infix fun <DT1 : Any> NullablePropertyScopedConstraint<DT, DT1>.mustExist(
        validations: Constraint<DT1>.() -> Unit
    ) = this@ScopedConstraintBuilder.validation {
        val item = this@mustExist.get(it)
        if (item != null) {
            ScopedConstraintBuilder<DT1>().apply(validations).validateAll(item)
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
class NullablePropertyScopedConstraint<T, DT : Any> internal constructor(
    private val get: (T) -> DT?
) {
    internal fun get(owner: T)  = get.invoke(owner)
}

@Suppress("unused")
// The receiver is needed to ensure dsl integrity
// This lives outside of the interface to prevent overriding it
fun <T : Any> Validator<T>.describe(
    description: ConstraintsBuilder<T>.() -> Unit
): Lazy<ValidatorDescriptor<T>> {
    return lazy { ConstraintsBuilder<T>().apply(description).build() }
}
