package dev.ahmedmourad.validation.core.validations

internal fun <T> Iterable<T>.contentEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: Iterable<T>
): Boolean {

    val thisWithDistinct = if (ignoreDuplicates) this.distinct() else this
    val otherWithDistinct = if (ignoreDuplicates) other.distinct() else other

    val thisCount = thisWithDistinct.count()
    if (thisCount != otherWithDistinct.count()) {
        return false
    }

    return if (ignoreOrder) {
        if (ignoreDuplicates) {
            thisWithDistinct.all { otherWithDistinct.contains(it) }
        } else {
            val otherWithOccurrences = otherWithDistinct.groupBy { it }
            thisWithDistinct.groupBy { it }.all { (item, occurrences) ->
                otherWithOccurrences[item]?.size == occurrences.size
            }
        }
    } else {
        for (i in 0 until thisCount) {
            if (thisWithDistinct.elementAt(i) != otherWithDistinct.elementAt(i)) {
                return false
            }
        }
        true
    }
}

internal fun <T> Iterable<T>.contentNotEquals(
    ignoreDuplicates: Boolean,
    ignoreOrder: Boolean,
    other: Iterable<T>
): Boolean {

    val thisWithDistinct = if (ignoreDuplicates) this.distinct() else this
    val otherWithDistinct = if (ignoreDuplicates) other.distinct() else other

    val thisCount = thisWithDistinct.count()
    if (thisCount != otherWithDistinct.count()) {
        return true
    }

    return if (ignoreOrder) {
        if (ignoreDuplicates) {
            thisWithDistinct.any { !otherWithDistinct.contains(it) }
        } else {
            val otherWithOccurrences = otherWithDistinct.groupBy { it }
            thisWithDistinct.groupBy { it }.any { (item, occurrences) ->
                otherWithOccurrences[item]?.size != occurrences.size
            }
        }
    } else {
        for (i in 0 until thisCount) {
            if (thisWithDistinct.elementAt(i) != otherWithDistinct.elementAt(i)) {
                return true
            }
        }
        false
    }
}
