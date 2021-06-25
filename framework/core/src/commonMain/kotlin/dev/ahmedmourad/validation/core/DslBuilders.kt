package dev.ahmedmourad.validation.core

import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.*
import kotlin.experimental.ExperimentalTypeInference
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1

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

    fun <X> evaluate(evaluate: SubjectHolder<T>.() -> X): Lazy<T, X> {
        return Lazy(evaluate)
    }

    internal fun build(): ValidatorDescriptor<T> = ValidatorDescriptor(constraints)
}

@ValidationDslMarker
interface Constraint<DT> {

    fun validation(validate: SubjectHolder<DT>.() -> Boolean)

    fun <DT1> on(property: SubjectHolder<DT>.() -> DT1, constraint: Constraint<DT1>.() -> Unit)
    fun <DT1> on(property: KProperty0<DT1>, constraint: Constraint<DT1>.() -> Unit)
    fun <DT1> on(property: KProperty1<DT, DT1>, constraint: Constraint<DT1>.() -> Unit)
    fun <DT1 : Any> on(property: SubjectHolder<DT>.() -> DT1?): NullablePropertyScopedConstraint<DT, DT1>
    fun <DT1 : Any> on(property: KProperty0<DT1?>): NullablePropertyScopedConstraint<DT, DT1>
    fun <DT1 : Any> on(property: KProperty1<DT, DT1?>): NullablePropertyScopedConstraint<DT, DT1>

    infix fun <DT1 : Any> NullablePropertyScopedConstraint<DT, DT1>.ifExists(
        validations: Constraint<DT1>.() -> Unit
    )

    infix fun <DT1 : Any> NullablePropertyScopedConstraint<DT, DT1>.mustExist(
        validations: Constraint<DT1>.() -> Unit
    )

    fun <X> evaluate(evaluate: SubjectHolder<DT>.() -> X): Lazy<DT, X> {
        return Lazy(evaluate)
    }
}

@OptIn(ExperimentalTypeInference::class)
@ValidationDslMarker
class ConstraintBuilder<T : Any> internal constructor(
    private val violation: String
) : Constraint<T> {

    private val includedValidators: MutableList<IncludedValidatorDescriptor<T, *, *>> = mutableListOf()
    private val validations: MutableList<ValidationDescriptor<T>> = mutableListOf()
    private val metadata: MutableList<MetadataDescriptor<T, *>> = mutableListOf()

    override fun validation(validate: SubjectHolder<T>.() -> Boolean) {
        validations.add(ValidationDescriptor(validate))
    }

    @Meta
    fun <@MetaType P> meta(@MetaName name: String, get: SubjectHolder<T>.() -> P) {
        metadata.add(MetadataDescriptor(name, get))
    }

    @Meta
    @OverloadResolutionByLambdaReturnType
    fun <T1 : Any, @InclusionType @MetaType C : Validator<T1>> include(
        @MetaName meta: String,
        binding: SubjectHolder<T>.() -> Pair<T1?, C>
    ) {
        includedValidators.add(IncludedValidatorDescriptor(
            meta,
            binding
        ))
    }

    @Meta
    @OverloadResolutionByLambdaReturnType
    fun <T1 : Any, @InclusionType @MetaType C : Validator<T1>> include(
        @MetaName meta: String,
        binding: SubjectHolder<T>.() -> Pair<KProperty0<T1?>, C>
    ) {
        include(meta) {
            val (property, target) = binding.invoke(this)
            property.invoke() to target
        }
    }

    @Meta
    @OverloadResolutionByLambdaReturnType
    fun <T1 : Any, @InclusionType @MetaType C : Validator<T1>> include(
        @MetaName meta: String,
        binding: SubjectHolder<T>.() -> Pair<KProperty1<T, T1?>, C>
    ) {
        include(meta) {
            val (property, target) = binding.invoke(this)
            property.invoke(subject) to target
        }
    }

    override fun <DT> on(
        property: SubjectHolder<T>.() -> DT,
        constraint: Constraint<DT>.() -> Unit
    ) = validation {
        ScopedConstraintBuilder<DT>()
            .apply(constraint)
            .validateAll(property.invoke(this))
    }

    override fun <DT> on(
        property: KProperty0<DT>,
        constraint: Constraint<DT>.() -> Unit
    ) = validation {
        ScopedConstraintBuilder<DT>()
            .apply(constraint)
            .validateAll(property.get())
    }

    override fun <DT> on(
        property: KProperty1<T, DT>,
        constraint: Constraint<DT>.() -> Unit
    ) = validation {
        ScopedConstraintBuilder<DT>()
            .apply(constraint)
            .validateAll(property.get(subject))
    }

    override fun <DT : Any> on(
        property: SubjectHolder<T>.() -> DT?
    ): NullablePropertyScopedConstraint<T, DT> {
        return NullablePropertyScopedConstraint(property)
    }

    override fun <DT : Any> on(
        property: KProperty0<DT?>
    ): NullablePropertyScopedConstraint<T, DT> {
        return on { property.get() }
    }

    override fun <DT : Any> on(
        property: KProperty1<T, DT?>
    ): NullablePropertyScopedConstraint<T, DT> {
        return on { property.get(subject) }
    }

    override infix fun <DT : Any> NullablePropertyScopedConstraint<T, DT>.ifExists(
        validations: Constraint<DT>.() -> Unit
    ) = this@ConstraintBuilder.validation {
        val item = this@ifExists.get(this)
        if (item != null) {
            ScopedConstraintBuilder<DT>()
                .apply(validations)
                .validateAll(item)
        } else {
            true
        }
    }

    override infix fun <DT : Any> NullablePropertyScopedConstraint<T, DT>.mustExist(
        validations: Constraint<DT>.() -> Unit
    ) = this@ConstraintBuilder.validation {
        val item = this@mustExist.get(this)
        if (item != null) {
            ScopedConstraintBuilder<DT>()
                .apply(validations)
                .validateAll(item)
        } else {
            false
        }
    }

    internal fun build(): ConstraintDescriptor<T> = ConstraintDescriptor(
        violation,
        includedValidators,
        validations,
        metadata
    )
}

@ValidationDslMarker
class ScopedConstraintBuilder<DT> : Constraint<DT> {

    private val validations: MutableList<ValidationDescriptor<DT>> =
        mutableListOf()

    override fun validation(validate: SubjectHolder<DT>.() -> Boolean) {
        validations.add(ValidationDescriptor(validate))
    }

    override fun <DT1> on(
        property: SubjectHolder<DT>.() -> DT1,
        constraint: Constraint<DT1>.() -> Unit
    ) = validation {
        ScopedConstraintBuilder<DT1>()
            .apply(constraint)
            .validateAll(property.invoke(this))
    }

    override fun <DT1> on(
        property: KProperty0<DT1>,
        constraint: Constraint<DT1>.() -> Unit
    ) = validation {
        ScopedConstraintBuilder<DT1>()
            .apply(constraint)
            .validateAll(property.get())
    }

    override fun <DT1> on(
        property: KProperty1<DT, DT1>,
        constraint: Constraint<DT1>.() -> Unit
    ) = validation {
        ScopedConstraintBuilder<DT1>()
            .apply(constraint)
            .validateAll(property.get(subject))
    }

    override fun <DT1 : Any> on(
        property: SubjectHolder<DT>.() -> DT1?
    ): NullablePropertyScopedConstraint<DT, DT1> {
        return NullablePropertyScopedConstraint(property)
    }

    override fun <DT1 : Any> on(
        property: KProperty0<DT1?>
    ): NullablePropertyScopedConstraint<DT, DT1> {
        return on { property.get() }
    }

    override fun <DT1 : Any> on(
        property: KProperty1<DT, DT1?>
    ): NullablePropertyScopedConstraint<DT, DT1> {
        return on { property.get(subject) }
    }

    override infix fun <DT1 : Any> NullablePropertyScopedConstraint<DT, DT1>.ifExists(
        validations: Constraint<DT1>.() -> Unit
    ) = this@ScopedConstraintBuilder.validation {
        val item = this@ifExists.get(this)
        if (item != null) {
            ScopedConstraintBuilder<DT1>()
                .apply(validations)
                .validateAll(item)
        } else {
            true
        }
    }

    override infix fun <DT1 : Any> NullablePropertyScopedConstraint<DT, DT1>.mustExist(
        validations: Constraint<DT1>.() -> Unit
    ) = this@ScopedConstraintBuilder.validation {
        val item = this@mustExist.get(this)
        if (item != null) {
            ScopedConstraintBuilder<DT1>()
                .apply(validations)
                .validateAll(item)
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
    private val get: SubjectHolder<T>.() -> DT?
) {
    internal fun get(holder: SubjectHolder<T>)  = get.invoke(holder)
}

@ValidationDslMarker
class Lazy<DT, X>(
    private val evaluate: SubjectHolder<DT>.() -> X
) {

    private var isInitialized = false
    private var item: X? = null

    @Suppress("UNCHECKED_CAST")
    internal fun get(holder: SubjectHolder<DT>): X {
        return if (isInitialized) {
            item as X
        } else {
            this@Lazy.evaluate(holder).also {
                item = it
            }
        }
    }
}

@ValidationDslMarker
class SubjectHolder<DT>(val subject: DT) {
    fun <X> Lazy<DT, X>.get(): X {
        return this.get(this@SubjectHolder)
    }
}

@ValidationDslMarker
class InclusionMetadata<T : Any, T1 : Any>(
    val subject: T,
    val target: T1
)

@Suppress("unused")
// The receiver is needed to ensure dsl integrity
// This lives outside of the interface to prevent overriding it
fun <T : Any> Validator<T>.describe(
    description: ConstraintsBuilder<T>.() -> Unit
): kotlin.Lazy<ValidatorDescriptor<T>> {
    return lazy { ConstraintsBuilder<T>().apply(description).build() }
}
