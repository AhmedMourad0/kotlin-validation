package dev.ahmedmourad.validation.compiler.descriptors

import dev.ahmedmourad.validation.compiler.utils.deepFqName
import org.jetbrains.kotlin.types.KotlinType

internal data class IncludedConstraintDescriptor(
    val validationsFileFqName: String,
    val constrainedType: KotlinType,
    val constrainerType: KotlinType,
    val constrainedAliasOrSimpleName: String
) {

    val constrainedFqName by lazy { constrainedType.deepFqName() }

    val constrainerFqName by lazy { constrainerType.deepFqName() }

    val validateFqName by lazy { "$validationsFileFqName.validate" }
    val isValidFqName by lazy { "$validationsFileFqName.isValid" }
}
