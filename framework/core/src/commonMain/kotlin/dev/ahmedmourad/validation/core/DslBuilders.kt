package dev.ahmedmourad.validation.core

import kotlin.annotation.AnnotationRetention.*
import kotlin.annotation.AnnotationTarget.*
import kotlin.experimental.ExperimentalTypeInference
import kotlin.jvm.JvmName
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
        validations: ConstraintBuilder<T>.() -> Unit
    ) {
        constraints.add(ConstraintBuilder<T>(violation).apply(validations).build())
    }

    fun <X> evaluate(evaluate: SubjectHolder<T>.() -> X): Lazy<T, X> {
        return Lazy(evaluate)
    }

    internal fun build(): ValidatorDescriptor<T> = ValidatorDescriptor(constraints)
}

@ValidationDslMarker
interface Constraint<DT> {

    fun validation(validate: SubjectHolder<DT>.() -> Boolean)

    fun <DT1> on(target: SubjectHolder<DT>.() -> DT1, validations: Constraint<DT1>.() -> Unit)
    fun <DT1> on(target: Lazy<DT, DT1>, validations: Constraint<DT1>.() -> Unit)
    fun <DT1> on(target: KProperty0<DT1>, validations: Constraint<DT1>.() -> Unit)
    fun <DT1> on(target: KProperty1<DT, DT1>, validations: Constraint<DT1>.() -> Unit)
    fun <DT1 : Any> on(target: SubjectHolder<DT>.() -> DT1?): NullablePropertyScopedConstraint<DT, DT1>
    fun <DT1 : Any> on(target: Lazy<DT, DT1?>): NullablePropertyScopedConstraint<DT, DT1>
    fun <DT1 : Any> on(target: KProperty0<DT1?>): NullablePropertyScopedConstraint<DT, DT1>
    fun <DT1 : Any> on(target: KProperty1<DT, DT1?>): NullablePropertyScopedConstraint<DT, DT1>

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
    fun <@MetaType P> meta(@MetaName name: String, value: SubjectHolder<T>.() -> P) {
        metadata.add(MetadataDescriptor(name, value))
    }

    @Meta
    fun <@MetaType P> meta(@MetaName name: String, value: Lazy<T, P>) {
        meta(name) { value.get() }
    }

    @JvmName("include")
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

    @JvmName("include1")
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

    @JvmName("include2")
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
        target: SubjectHolder<T>.() -> DT,
        validations: Constraint<DT>.() -> Unit
    ) = validation {
        ScopedConstraintBuilder<DT>()
            .apply(validations)
            .matchesAll(target.invoke(this))
    }

    override fun <DT> on(
        target: Lazy<T, DT>,
        validations: Constraint<DT>.() -> Unit
    ) {
        on({ target.get() }, validations)
    }

    override fun <DT> on(
        target: KProperty0<DT>,
        validations: Constraint<DT>.() -> Unit
    ) = validation {
        ScopedConstraintBuilder<DT>()
            .apply(validations)
            .matchesAll(target.get())
    }

    override fun <DT> on(
        target: KProperty1<T, DT>,
        validations: Constraint<DT>.() -> Unit
    ) = validation {
        ScopedConstraintBuilder<DT>()
            .apply(validations)
            .matchesAll(target.get(subject))
    }

    override fun <DT : Any> on(
        target: SubjectHolder<T>.() -> DT?
    ): NullablePropertyScopedConstraint<T, DT> {
        return NullablePropertyScopedConstraint(target)
    }

    override fun <DT1 : Any> on(
        target: Lazy<T, DT1?>
    ): NullablePropertyScopedConstraint<T, DT1> {
        return on { target.get() }
    }

    override fun <DT : Any> on(
        target: KProperty0<DT?>
    ): NullablePropertyScopedConstraint<T, DT> {
        return on { target.get() }
    }

    override fun <DT : Any> on(
        target: KProperty1<T, DT?>
    ): NullablePropertyScopedConstraint<T, DT> {
        return on { target.get(subject) }
    }

    override infix fun <DT : Any> NullablePropertyScopedConstraint<T, DT>.ifExists(
        validations: Constraint<DT>.() -> Unit
    ) = this@ConstraintBuilder.validation {
        val item = this@ifExists.get(this)
        if (item != null) {
            ScopedConstraintBuilder<DT>()
                .apply(validations)
                .matchesAll(item)
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
                .matchesAll(item)
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
        target: SubjectHolder<DT>.() -> DT1,
        validations: Constraint<DT1>.() -> Unit
    ) = validation {
        ScopedConstraintBuilder<DT1>()
            .apply(validations)
            .matchesAll(target.invoke(this))
    }

    override fun <DT1> on(
        target: Lazy<DT, DT1>,
        validations: Constraint<DT1>.() -> Unit
    ) {
        on({ target.get() }, validations)
    }

    override fun <DT1> on(
        target: KProperty0<DT1>,
        validations: Constraint<DT1>.() -> Unit
    ) = validation {
        ScopedConstraintBuilder<DT1>()
            .apply(validations)
            .matchesAll(target.get())
    }

    override fun <DT1> on(
        target: KProperty1<DT, DT1>,
        validations: Constraint<DT1>.() -> Unit
    ) = validation {
        ScopedConstraintBuilder<DT1>()
            .apply(validations)
            .matchesAll(target.get(subject))
    }

    override fun <DT1 : Any> on(
        target: SubjectHolder<DT>.() -> DT1?
    ): NullablePropertyScopedConstraint<DT, DT1> {
        return NullablePropertyScopedConstraint(target)
    }

    override fun <DT1 : Any> on(
        target: Lazy<DT, DT1?>
    ): NullablePropertyScopedConstraint<DT, DT1> {
        return on { target.get() }
    }

    override fun <DT1 : Any> on(
        target: KProperty0<DT1?>
    ): NullablePropertyScopedConstraint<DT, DT1> {
        return on { target.get() }
    }

    override fun <DT1 : Any> on(
        target: KProperty1<DT, DT1?>
    ): NullablePropertyScopedConstraint<DT, DT1> {
        return on { target.get(subject) }
    }

    override infix fun <DT1 : Any> NullablePropertyScopedConstraint<DT, DT1>.ifExists(
        validations: Constraint<DT1>.() -> Unit
    ) = this@ScopedConstraintBuilder.validation {
        val item = this@ifExists.get(this)
        if (item != null) {
            ScopedConstraintBuilder<DT1>()
                .apply(validations)
                .matchesAll(item)
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
                .matchesAll(item)
        } else {
            false
        }
    }

    fun ScopedConstraintBuilder<DT>.asValidation() = this@ScopedConstraintBuilder.validation {
        this@asValidation.matchesAll(subject)
    }

    fun matchesAll(item: DT): Boolean {
        return validations.all { it.validate(SubjectHolder(item)) }
    }

    fun matchesAny(item: DT): Boolean {
        return validations.any { it.validate(SubjectHolder(item)) }
    }

    fun matchesNone(item: DT): Boolean {
        return validations.none { it.validate(SubjectHolder(item)) }
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
    constraints: ConstraintsBuilder<T>.() -> Unit
): kotlin.Lazy<ValidatorDescriptor<T>> {
    return lazy { ConstraintsBuilder<T>().apply(constraints).build() }
}

//TODO: this exposes too many functions
fun <T> validator(
    validations: ScopedConstraintBuilder<T>.() -> Unit
): ScopedConstraintBuilder<T> {
    return ScopedConstraintBuilder<T>().apply(validations)
}
