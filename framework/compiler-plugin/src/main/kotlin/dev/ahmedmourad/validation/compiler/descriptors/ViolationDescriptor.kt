package dev.ahmedmourad.validation.compiler.descriptors

import org.jetbrains.kotlin.psi.KtExpression

internal data class ViolationDescriptor(
    val name: String,
    val nameExpression: KtExpression,
    val params: List<ParamDescriptor>
) {
    val regularParams by lazy { params.filter { it.includedConstraint == null } }
    val inclusionParams by lazy { params.filter { it.includedConstraint != null } }
}
