package dev.ahmedmourad.validation.compiler.utils

import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

internal const val PACKAGE_BASE = "dev.ahmedmourad.validation"
internal const val PACKAGE_CORE = "$PACKAGE_BASE.core"

internal const val SUFFIX_VIOLATIONS_SUPER_CLASS = "Violation"
internal const val SUFFIX_VALIDATION_CONTEXT = "ValidationContext"
internal const val SUFFIX_VALIDATION_CONTEXT_IMPL = "Impl"
internal const val SUFFIX_OUTPUT_FILE_NAME = "Validations"
internal const val OUTPUT_FOLDER = "validations"

internal val fqNameConstraintsDescriptor = FqName("$PACKAGE_CORE.ConstraintsDescriptor")
internal val fqNameConstrains = FqName("$PACKAGE_CORE.Constrains")
internal val fqNameConstraint = FqName("$PACKAGE_CORE.Constraint")
internal val fqNameValidation = FqName("$PACKAGE_CORE.Validation")
internal val fqNameIncludedConstraints = FqName("$PACKAGE_CORE.IncludedConstraints")
internal val fqNameValidator = FqName("$PACKAGE_CORE.Validator")
internal val fqNameCase = FqName("$PACKAGE_CORE.Case")
internal val fqNameMustBeValid = FqName("$PACKAGE_CORE.MustBeValid")
internal val fqNameMeta = FqName("$PACKAGE_CORE.Meta")
internal val fqNameMetaName = FqName("$PACKAGE_CORE.MetaName")
internal val fqNameMetaType = FqName("$PACKAGE_CORE.MetaType")
internal val fqNameInclusionType = FqName("$PACKAGE_CORE.InclusionType")
internal val fqNameInternalValidationApi = FqName("$PACKAGE_CORE.InternalValidationApi")
internal val fqNameConstrainerConfig = FqName("$PACKAGE_CORE.ConstrainerConfig")

internal val fqNameLegalFun = FqName("$PACKAGE_CORE.legal")
internal val fqNameIllegalFun = FqName("$PACKAGE_CORE.illegal")
internal val fqNameSwapFun = FqName("$PACKAGE_CORE.swap")
internal val fqNameOrElseFun = FqName("$PACKAGE_CORE.orElse")
internal val fqNameDescribeFun = FqName("$PACKAGE_CORE.describe")
internal val fqNameConstraintFun = FqName("$PACKAGE_CORE.ConstraintsBuilder.constraint")
internal val fqNameMetaFun = FqName("$PACKAGE_CORE.ConstraintBuilder.meta")

internal val paramConstrainedAlias = Name.identifier("constrainedAlias")

internal val propertyConstraints = Name.identifier("constraints")
