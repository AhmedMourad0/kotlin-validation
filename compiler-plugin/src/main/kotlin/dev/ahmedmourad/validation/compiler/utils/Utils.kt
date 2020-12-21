package dev.ahmedmourad.validation.compiler.utils

import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.types.KotlinType

internal fun KotlinType.simpleName(): String? {
    return this.getJetTypeFqName(false)
        .split('.')
        .lastOrNull()
}
