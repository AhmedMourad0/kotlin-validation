package dev.ahmedmourad.validation.compiler.descriptors

import dev.ahmedmourad.validation.compiler.utils.deepFqName
import org.jetbrains.kotlin.types.KotlinType

internal data class IncludedValidatorDescriptor(
    val validationsFileFqName: String,
    val subjectType: KotlinType,
    val validatorType: KotlinType,
    val subjectAliasOrSimpleName: String
) {

    val subjectFqName by lazy { subjectType.deepFqName() }

    val validatorFqName by lazy { validatorType.deepFqName() }

    val validateFqName by lazy { "$validationsFileFqName.validate" }
    val isValidFqName by lazy { "$validationsFileFqName.isValid" }
}
