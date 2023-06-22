package dev.ahmedmourad.validation.core

@RequiresOptIn(level = RequiresOptIn.Level.ERROR)
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
annotation class InternalValidationApi

@RequiresOptIn(level = RequiresOptIn.Level.ERROR)
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
annotation class UnsafeValidationContext

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class MustBeValid

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class ValidatorConfig(val subjectAlias: String)

data class ValidatorDescriptor<T : Any> internal constructor(
    private val values: List<ConstraintDescriptor<T>>
) : List<ConstraintDescriptor<T>> by values

data class ConstraintDescriptor<T : Any> internal constructor(
    val violation: String,
    val includedValidators: List<IncludedValidatorDescriptor<T, *, *>>,
    val validations: List<ValidationDescriptor<T>>,
    val metadata: List<MetadataDescriptor<T, *>>
)

data class IncludedValidatorDescriptor<T : Any, T1 : Any, C : Validator<T1>>(
    val meta: String,
    private val binding: SubjectHolder<T>.() -> Pair<T1?, C>
) {

    @InternalValidationApi
    fun getBinding(item: T) = getBinding(SubjectHolder(item))

    @InternalValidationApi
    fun getBinding(item: SubjectHolder<T>) = binding.invoke(item)

    @InternalValidationApi
    inline fun isValid(
        item: T,
        crossinline isValid: C.(T1) -> Boolean
    ): Boolean = isValid(SubjectHolder(item), isValid)

    @InternalValidationApi
    inline fun isValid(
        item: SubjectHolder<T>,
        crossinline isValid: C.(T1) -> Boolean
    ): Boolean {
        val (target, validator) = getBinding(item)
        return target?.let {
            validator.isValid(it)
        } ?: true
    }

    @InternalValidationApi
    inline fun <V : Any> findViolations(
        item: T,
        crossinline validate: C.(T1) -> Case<Set<V>, T1>
    ): Set<V> = findViolations(SubjectHolder(item), validate)

    @InternalValidationApi
    inline fun <V : Any> findViolations(
        item: SubjectHolder<T>,
        crossinline validate: C.(T1) -> Case<Set<V>, T1>
    ): Set<V> {
        val (target, validator) = getBinding(item)
        return target?.let {
            validator.validate(it).swap().orElse { emptySet() }
        }.orEmpty()
    }
}

//TODO: both, and IncludedValidator should accept a SubjectHolder instead, and
// it should be created once at call site
data class MetadataDescriptor<T : Any, P> internal constructor(
    val name: String,
    private val get: SubjectHolder<T>.() -> P
) {
    fun get(item: T) = get(SubjectHolder(item))
    fun get(item: SubjectHolder<T>) = get.invoke(item)
}

data class ValidationDescriptor<DT> internal constructor(
    private val validate: SubjectHolder<DT>.() -> Boolean
) {
    fun validate(item: DT) = validate(SubjectHolder(item))
    fun validate(item: SubjectHolder<DT>) = validate.invoke(item)
}
