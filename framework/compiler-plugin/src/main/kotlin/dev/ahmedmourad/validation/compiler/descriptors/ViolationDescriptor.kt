package dev.ahmedmourad.validation.compiler.descriptors

import org.jetbrains.kotlin.psi.KtExpression

internal data class ViolationDescriptor(
    val name: String,
    val nameExpression: KtExpression,
    val metas: List<MetaDescriptor>
) {
    val regularMetas by lazy { metas.filter { it.includedConstraint == null } }
    val inclusionMetas by lazy { metas.filter { it.includedConstraint != null } }
}
