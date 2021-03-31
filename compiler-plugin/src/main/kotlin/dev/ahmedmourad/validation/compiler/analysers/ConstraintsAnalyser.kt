package dev.ahmedmourad.validation.compiler.analysers

import arrow.meta.quotes.ktFile
import dev.ahmedmourad.validation.compiler.descriptors.*
import dev.ahmedmourad.validation.compiler.descriptors.ConstraintsDescriptor
import dev.ahmedmourad.validation.compiler.descriptors.IncludedConstraintDescriptor
import dev.ahmedmourad.validation.compiler.descriptors.ParamDescriptor
import dev.ahmedmourad.validation.compiler.descriptors.ViolationDescriptor
import dev.ahmedmourad.validation.compiler.utils.*
import dev.ahmedmourad.validation.compiler.dsl.DslValidator
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.descriptors.ClassConstructorDescriptor
import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.js.translate.utils.finalElement
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getParentCall
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall
import org.jetbrains.kotlin.resolve.calls.callUtil.getValueArgumentsInParentheses
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCall
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameOrNull
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.resolve.lazy.descriptors.LazyClassDescriptor
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.typeUtil.supertypes
import org.jetbrains.kotlin.util.containingNonLocalDeclaration
import org.jetbrains.kotlin.utils.addToStdlib.safeAs

internal class ConstraintsAnalyser(
    private val bindingContext: BindingContext,
    private val dslValidator: DslValidator
) {

    internal fun analyse(): List<ConstraintsDescriptor> {

        val violationsCalls = mutableListOf<ResolvedCall<*>>()
        val paramsCalls = mutableListOf<ResolvedCall<*>>()
        val constructorsCalls = mutableListOf<ResolvedCall<*>>()

        bindingContext.getSliceContents(BindingContext.RESOLVED_CALL).forEach { (_, resolvedCall) ->
            when {

                resolvedCall?.candidateDescriptor?.fqNameSafe?.asString() == FQ_NAME_CONSTRAINT_FUN -> {
                    violationsCalls += resolvedCall
                }

                resolvedCall?.candidateDescriptor?.annotations?.hasAnnotation(FqName(FQ_NAME_PARAM_ANNOTATION)) == true -> {
                    paramsCalls += resolvedCall
                }

                resolvedCall?.candidateDescriptor is ClassConstructorDescriptor -> {
                    val mustBeValid = resolvedCall.candidateDescriptor
                        .containingDeclaration
                        .annotations
                        .hasAnnotation(FqName(FQ_NAME_MUST_BE_VALID_ANNOTATION))
                    if (mustBeValid) {
                        constructorsCalls += resolvedCall
                    }
                }
            }
        }

        paramsCalls.forEach {
            dslValidator.verifyParamIsCalledDirectlyInsideConstraint(it)
        }

        val constraintsDescriptors = violationsCalls.asSequence().mapNotNull { resolvedCall ->
            val describeCall = getDescribeCall(resolvedCall) ?: return@mapNotNull null
            val constrainerObject = getConstrainerClassOrObject(describeCall) ?: return@mapNotNull null
            val constrainedType = getConstrainedType(resolvedCall) ?: return@mapNotNull null
            val constrainedTypePsi = getConstrainedTypePsi(resolvedCall) ?: return@mapNotNull null
            val (violationName, violationNameExpression) = getViolationName(resolvedCall) ?: return@mapNotNull null
            val params = getViolationParams(resolvedCall) ?: return@mapNotNull null
            ViolationDescriptor(
                violationName,
                violationNameExpression,
                constrainedType,
                constrainedTypePsi,
                constrainerObject,
                params
            )
        }.groupBy {
            it.constrainerClassOrObject.fqName?.asString()
        }.let {
            dslValidator.verifyNoDuplicateViolations(it)
        }.map { (_, violationsGroup) ->

//            dslValidator.reportError("zzzzzzzzzz", null)

            val any = violationsGroup.first()
            ConstraintsDescriptor(
                bindingContext,
                dslValidator,
                any.constrainedType,
                any.constrainedTypePsi,
                any.constrainerClassOrObject,
                violationsGroup
            )
        }

        constructorsCalls.forEach { resolvedCall ->

            val constructorOwnerFqName = resolvedCall.candidateDescriptor
                .containingDeclaration
                .safeAs<LazyClassDescriptor>()
                ?.fqNameOrNull()
                ?.asString()

            val descriptors = constraintsDescriptors.filter {
                it.constrainedClass?.fqNameOrNull()?.asString() == constructorOwnerFqName
            }

            dslValidator.verifyConstructorCallIsAllowed(descriptors, resolvedCall)
        }

        return constraintsDescriptors
    }

    private fun getConstrainedTypePsi(constraintResolvedCall: ResolvedCall<*>): PsiElement? {

        val describeFunctionLiteral = constraintResolvedCall.call
            .callElement
            .parent
            ?.parent as? KtFunctionLiteral

        return describeFunctionLiteral?.getParentCall(bindingContext)
            ?.callElement
            ?.containingNonLocalDeclaration()
            ?.safeAs<KtClassOrObject>()
            ?.superTypeListEntries
            ?.firstOrNull {
                bindingContext.get(BindingContext.TYPE, it.typeReference)
                    ?.getJetTypeFqName(false) == FQ_NAME_CONSTRAINS
            }?.typeAsUserType
            ?.typeArguments
            ?.elementAtOrNull(0)
            ?.finalElement
            ?: dslValidator.reportError(
                "Failed to resolve constrained type",
                describeFunctionLiteral
            )
    }

    private fun getConstrainedType(constraintResolvedCall: ResolvedCall<*>): KotlinType? {

        val describeFunctionLiteral = constraintResolvedCall.call
            .callElement
            .parent
            ?.parent as? KtFunctionLiteral

        return describeFunctionLiteral?.getParentCall(bindingContext)
            ?.callElement
            ?.getResolvedCall(bindingContext)
            ?.typeArguments
            ?.values
            ?.elementAtOrNull(0)
            ?: return dslValidator.reportError(
                "Could not find the constrained type",
                constraintResolvedCall.call.callElement
            )
    }

    private fun getDescribeCall(constraintResolvedCall: ResolvedCall<*>): KtElement? {
        return dslValidator.verifyConstraintIsCalledDirectlyInsideDescribe(constraintResolvedCall)
    }

    private fun getConstrainerClassOrObject(describeCall: KtElement?): KtClassOrObject? {
        return describeCall?.parent
            ?.parent
            ?.parent
            ?.parent as? KtClassOrObject
    }

    private fun getViolationName(resolvedCall: ResolvedCall<*>): Pair<String, KtExpression>? {

        val nameExpression = resolvedCall.call.getValueArgumentsInParentheses()
            .getOrNull(0)
            ?.getArgumentExpression()
            ?: return dslValidator.reportError(
                "Unable to find `violation` name",
                resolvedCall.call.callElement
            )

        return dslValidator.verifyViolationName(nameExpression)
    }

    private fun getViolationParams(resolvedCall: ResolvedCall<*>): List<ParamDescriptor>? {

        fun analyseParamType(statementResolvedCall: ResolvedCall<*>): Pair<IncludedConstraintDescriptor?, String>? {

            val (paramTypeParam, paramTypeArg) = statementResolvedCall.typeArguments
                .asSequence()
                .firstOrNull { (param, _) ->
                    param.annotations.hasAnnotation(FqName(FQ_NAME_PARAM_TYPE_ANNOTATION))
                } ?: return null

            return if (paramTypeParam.annotations.hasAnnotation(FqName(FQ_NAME_INCLUSION_TYPE_ANNOTATION))) {

                val constrainsSuperType = paramTypeArg?.supertypes()
                    ?.firstOrNull { it.getJetTypeFqName(false) == FQ_NAME_CONSTRAINS }
                    ?: return null

                val constrainedAlias = paramTypeArg.constructor
                    .declarationDescriptor
                    ?.annotations
                    ?.findAnnotation(FqName(FQ_NAME_CONSTRAINED_ALIAS_ANNOTATION))
                    ?.allValueArguments
                    ?.get(Name.identifier(CONSTRAINED_ALIAS_PARAM_CONSTRAINER_CONFIG_ANNOTATION))
                    ?.value
                    ?.safeAs<String>()

                val constrainedType = constrainsSuperType.arguments
                    .firstOrNull()
                    ?.type
                    ?: return null

                val constrainedAliasOrSimpleName = constrainedAlias ?: constrainedType.simpleName() ?: return null

                val validationsFileFqName = paramTypeArg.constructor
                    .declarationDescriptor
                    ?.ktFile()
                    ?.packageFqName
                    ?.asString()
                    ?.split(".")
                    ?.takeIf(List<String>::isNotEmpty)
                    ?.plus(OUTPUT_FOLDER)
                    ?.joinToString(".")
                    ?: return null

                IncludedConstraintDescriptor(
                    validationsFileFqName,
                    constrainedType,
                    paramTypeArg,
                    constrainedAliasOrSimpleName
                ) to "List<$validationsFileFqName.${constrainedAliasOrSimpleName}$VIOLATIONS_SUPER_CLASS_SUFFIX>"

            } else {
                null to paramTypeArg.deepFqName()
            }
        }

        fun extractParamEntry(statementResolvedCall: ResolvedCall<*>): ParamDescriptor? {

            val (includedConstraint, paramTypeFqName) = analyseParamType(statementResolvedCall)
                ?: return dslValidator.reportError(
                    "Unable to find `param` type",
                    statementResolvedCall.call.callElement
                )

            val nameIndex = statementResolvedCall.candidateDescriptor.valueParameters.firstOrNull {
                it.annotations.hasAnnotation(FqName(FQ_NAME_PARAM_NAME_ANNOTATION))
            }?.index ?: return dslValidator.reportError(
                "Unable to find `param` name",
                statementResolvedCall.call.callElement
            )

            val paramNameExpression = statementResolvedCall.call
                .valueArguments
                .elementAtOrNull(nameIndex)
                ?.getArgumentExpression()
                ?: return dslValidator.reportError(
                    "Unable to find `param` name",
                    statementResolvedCall.call.callElement
                )

            return dslValidator.verifyParamName(paramNameExpression)
                ?.first
                ?.let { paramName ->
                    ParamDescriptor(
                        paramName,
                        paramNameExpression,
                        paramTypeFqName,
                        includedConstraint
                    )
                }
        }

        val params = resolvedCall.call
            .functionLiteralArguments
            .getOrNull(0)
            ?.getLambdaExpression()
            ?.bodyExpression
            ?.statements
            ?.asSequence()
            ?.mapNotNull {
                it.getResolvedCall(bindingContext)
            }?.filter {
                it.candidateDescriptor.annotations.hasAnnotation(FqName(FQ_NAME_PARAM_ANNOTATION))
            }?.mapNotNull(::extractParamEntry)
            ?: return dslValidator.reportError(
                "Unable to resolve violation param",
                resolvedCall.call.callElement
            )

        return dslValidator.verifyNoDuplicateParams(params).toList()
    }
}
