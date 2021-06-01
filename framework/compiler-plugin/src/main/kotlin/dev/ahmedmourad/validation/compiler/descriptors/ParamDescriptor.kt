package dev.ahmedmourad.validation.compiler.descriptors

import org.jetbrains.kotlin.psi.KtExpression

internal data class ParamDescriptor(
    val name: String,
    val nameExpression: KtExpression,
    val typeFqName: String,
    val includedConstraint: IncludedConstraintDescriptor?
)
