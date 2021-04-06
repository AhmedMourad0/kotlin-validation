package dev.ahmedmourad.validation.compiler.utils

import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.psi.KtTypeParameter
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.bindingContextUtil.getAbbreviatedTypeOrType
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.typeUtil.isTypeParameter

//TODO: replace getJetTypeFqName with type.constructor.declarationDescriptor?.fqNameSafe
internal fun KotlinType.simpleName(): String? {
    return this.getJetTypeFqName(false)
        .split('.')
        .lastOrNull()
}

internal fun KotlinType.deepFqName(): String {

    if (this.isTypeParameter()) {
        return this.toString()
    }

    val thisFqName = this.getJetTypeFqName(false)

    val children = this.arguments.map {
        if (it.isStarProjection) {
            "*"
        } else {
            val variance = it.projectionKind.label
            "$variance ${it.type.deepFqName()}"
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

internal fun KtTypeParameter.deepFqName(bindingContext: BindingContext): String {

    val variance = this.variance.label
    val name = this.nameAsSafeName.asString()
    val bound = this.extendsBound
        ?.typeElement
        ?.getAbbreviatedTypeOrType(bindingContext)
        ?.deepFqName()

    return if (bound == null) {
        "$variance $name"
    } else {
        "$variance $name : $bound"
    }.trim()
}
