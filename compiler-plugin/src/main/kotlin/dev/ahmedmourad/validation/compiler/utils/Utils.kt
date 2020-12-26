package dev.ahmedmourad.validation.compiler.utils

import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.psi.KtTypeParameter
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.bindingContextUtil.getAbbreviatedTypeOrType
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.typeUtil.isTypeParameter

internal fun KotlinType.simpleName(): String? {
    return this.getJetTypeFqName(false)
        .split('.')
        .lastOrNull()
}

internal fun KotlinType.fqNameTotal(): String {

    if (this.isTypeParameter()) {
        return this.toString()
    }

    val thisFqName = this.getJetTypeFqName(false)

    val children = this.arguments.map {
        val variance = it.projectionKind.label
        if (it.isStarProjection) {
            "$variance *"
        } else {
            variance + " " + it.type.fqNameTotal()
        }.trim()
    }

    val type = if (children.isNotEmpty()) {
        "$thisFqName<${children.joinToString(", ")}>"
    } else {
        thisFqName
    }

    return if (this.isMarkedNullable) {
        "$type?"
    } else {
        type
    }
}

internal fun KtTypeParameter.fqNameTotal(bindingContext: BindingContext): String {

    val variance = this.variance.label
    val name = this.nameAsSafeName.asString()
    val bound = this.extendsBound
        ?.typeElement
        ?.getAbbreviatedTypeOrType(bindingContext)
        ?.fqNameTotal()

    return if (bound == null) {
        "$variance $name"
    } else {
        "$variance $name : $bound"
    }.trim()
}
