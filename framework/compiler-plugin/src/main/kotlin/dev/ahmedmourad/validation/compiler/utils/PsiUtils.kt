package dev.ahmedmourad.validation.compiler.utils

import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.annotations.Annotated
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptor
import org.jetbrains.kotlin.js.resolve.diagnostics.findPsi
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.bindingContextUtil.getAbbreviatedTypeOrType
import org.jetbrains.kotlin.resolve.bindingContextUtil.getReferenceTargets
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.typeUtil.isTypeParameter
import org.jetbrains.kotlin.utils.addToStdlib.safeAs

internal fun KtFile.classesAndInnerClasses(): Sequence<KtClassOrObject> {
    return generateSequence(findChildrenByClass(KtClassOrObject::class.java).toList()) { list ->
        list.flatMap {
            it.declarations.filterIsInstance<KtClassOrObject>()
        }.ifEmpty { null }
    }.flatMap { it.asSequence() }
}

internal fun KtClassOrObject.findSuperType(bindingContext: BindingContext, fqName: FqName): KtSuperTypeListEntry? {
    return this.superTypeListEntries.firstOrNull {
        it.typeReference?.kotlinType(bindingContext)?.fqNameSafe == fqName
    }
}

internal fun KtClassOrObject.hasSuperType(bindingContext: BindingContext, fqName: FqName): Boolean {
    return this.findSuperType(bindingContext, fqName) != null
}

internal fun KtTypeReference.kotlinType(bindingContext: BindingContext): KotlinType? {
    return bindingContext.get(BindingContext.TYPE, this)
}

internal fun Annotated.hasAnnotation(fqName: FqName): Boolean {
    return this.annotations.hasAnnotation(fqName)
}

internal fun Annotated.findAnnotation(fqName: FqName): AnnotationDescriptor? {
    return this.annotations.findAnnotation(fqName)
}

internal fun DeclarationDescriptor.ktFile(): KtFile? {
    return findPsi()?.containingFile.safeAs()
}

internal val KotlinType.fqNameSafe
    get() = this.constructor.declarationDescriptor?.fqNameSafe

internal fun KotlinType.simpleName(): String? {
    return this.fqNameSafe?.shortName()?.asString()
}

internal fun KotlinType.deepFqName(): String {

    if (this.isTypeParameter()) {
        return this.toString()
    }

    val thisFqName = this.fqNameSafe?.asString() ?: return this.toString()

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
