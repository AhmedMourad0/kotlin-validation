package dev.ahmedmourad.validation.core

@Experimental(level = Experimental.Level.ERROR)
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
annotation class InternalValidationApi

@Experimental(level = Experimental.Level.ERROR)
@MustBeDocumented
@Retention(AnnotationRetention.BINARY)
annotation class UnsafeValidationContext

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class MustBeValid

data class ConstraintsDescriptor<T : Any> internal constructor(
    private val values: List<Constraint<T>>
) : List<Constraint<T>> by values

data class Constraint<T : Any> internal constructor(
    val violation: String,
    val includedConstraints: List<IncludedConstraints<T, *, *>>,
    val validations: List<Validation<T>>,
    val params: List<Parameter<T, *>>
)

data class IncludedConstraints<T : Any, T1 : Any, C : Constrains<T1>>(
    val param: String,
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
        crossinline validate: C.(T1) -> Case<List<V>, T1>
    ): List<V> {
        return getConstrained(item)?.let {
            getConstrainer(item, it).validate(it).swap().orElse { emptyList() }
        }.orEmpty()
    }
}

data class Parameter<T : Any, P> internal constructor(
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
