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
annotation class ConstrainerConfig(val constrainedAlias: String)

data class ConstraintsDescriptor<T : Any> internal constructor(
    private val values: List<Constraint<T>>
) : List<Constraint<T>> by values

data class Constraint<T : Any> internal constructor(
    val violation: String,
    val includedConstraints: List<IncludedConstraints<T, *, *>>,
    val validations: List<Validation<T>>,
    val metadata: List<Metadata<T, *>>
)

data class IncludedConstraints<T : Any, T1 : Any, C : Constrains<T1>>(
    val meta: String,
    private val constrained: (T) -> T1?,
    private val constrainer: (T, T1) -> C
) {

    @InternalValidationApi
    fun getConstrained(item: T) = constrained.invoke(item)

    @InternalValidationApi
    fun getConstrainer(item: T, included: T1) = constrainer.invoke(item, included)

    @InternalValidationApi
    inline fun isValid(
        item: T,
        crossinline isValid: C.(T1) -> Boolean
    ): Boolean {
        return getConstrained(item)?.let {
            getConstrainer(item, it).isValid(it)
        } ?: true
    }

    @InternalValidationApi
    inline fun <V : Any> findViolations(
        item: T,
        crossinline validate: C.(T1) -> Case<Set<V>, T1>
    ): Set<V> {
        return getConstrained(item)?.let {
            getConstrainer(item, it).validate(it).swap().orElse { emptySet() }
        }.orEmpty()
    }
}

data class Metadata<T : Any, P> internal constructor(
    val name: String,
    private val get: (T) -> P
) {
    fun get(item: T) = get.invoke(item)
}

data class Validation<DT> internal constructor(
    private val validate: (DT) -> Boolean
) {
    fun validate(item: DT) = validate.invoke(item)
}
