package dev.ahmedmourad.validation.core

sealed class Case<out V : Any, out T : Any> {
    data class Illegal<V : Any>(val v: V) : Case<V, Nothing>()
    data class Legal<T : Any>(val v: T) : Case<Nothing, T>()
}

fun <T : Any> T.legal(): Case<Nothing, T> {
    return Case.Legal(this)
}

fun <V : Any> V.illegal(): Case<V, Nothing> {
    return Case.Illegal(this)
}

fun <V : Any, T : Any> Case<V, T>.swap(): Case<T, V> {
    return when (this) {
        is Case.Illegal -> this.v.legal()
        is Case.Legal -> this.v.illegal()
    }
}

fun <V : Any, T : Any> Case<V, T>.orElse(substitute: () -> T): T {
    return when (this) {
        is Case.Illegal -> substitute()
        is Case.Legal -> this.v
    }
}
