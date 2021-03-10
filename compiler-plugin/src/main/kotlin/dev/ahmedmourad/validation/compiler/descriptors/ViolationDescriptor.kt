package dev.ahmedmourad.validation.compiler.descriptors

import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.types.KotlinType

internal data class ViolationDescriptor(
    val name: String,
    val nameExpression: KtExpression,
    val constrainedType: KotlinType,
    val constrainedTypePsi: PsiElement,
    val constrainerClassOrObject: KtClassOrObject,
    val params: List<ParamDescriptor>
) {
    val regularParams by lazy { params.filter { it.includedConstraint == null } }
    val inclusionParams by lazy { params.filter { it.includedConstraint != null } }
}
