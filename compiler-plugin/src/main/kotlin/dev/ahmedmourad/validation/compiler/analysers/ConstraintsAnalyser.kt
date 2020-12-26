package dev.ahmedmourad.validation.compiler.analysers

import dev.ahmedmourad.validation.compiler.descriptors.Param
import dev.ahmedmourad.validation.compiler.descriptors.Violation
import dev.ahmedmourad.validation.compiler.utils.*
import dev.ahmedmourad.validation.compiler.descriptors.ConstraintsDescriptor
import dev.ahmedmourad.validation.compiler.verifier.DslVerifier
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.js.translate.utils.finalElement
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getParentCall
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall
import org.jetbrains.kotlin.resolve.calls.callUtil.getValueArgumentsInParentheses
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCall
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.util.containingNonLocalDeclaration

internal class ConstraintsAnalyser(
    private val bindingContext: BindingContext,
    private val verifier: DslVerifier
) {

    internal fun analyse(): List<ConstraintsDescriptor> {
        return bindingContext.getSliceContents(BindingContext.RESOLVED_CALL)
            .mapNotNull { (_, resolvedCall) ->

                when (resolvedCall?.candidateDescriptor?.fqNameSafe?.asString()) {

                    FQ_NAME_CONSTRAINT_FUN -> {
                        val describeCall = getDescribeCall(resolvedCall) ?: return@mapNotNull null
                        val constrainerObject = getConstrainerClassOrObject(describeCall) ?: return@mapNotNull null
                        val constrainedType = getConstrainedType(resolvedCall) ?: return@mapNotNull null
                        val constrainedTypePsi = getConstrainedTypePsi(resolvedCall) ?: return@mapNotNull null
                        val (violationName, violationNameExpression) = getViolationName(resolvedCall)
                            ?: return@mapNotNull null
                        val params = getViolationParams(resolvedCall) ?: return@mapNotNull null
                        Violation(
                            violationName,
                            violationNameExpression,
                            constrainedType,
                            constrainedTypePsi,
                            constrainerObject,
                            params
                        )
                    }

                    FQ_NAME_PARAM_FUN -> {
                        verifier.verifyParamIsCalledInsideConstraint(resolvedCall)
                        null
                    }

                    else -> null
                }
            }.groupBy {
                it.constrainerClassOrObject.fqName?.asString()
            }.let {
                verifier.verifyNoDuplicateViolations(it)
            }.map { (_, violationsGroup) ->

                verifier.reportError("zzzzzzzzzz", null)

                val any = violationsGroup.first()
                ConstraintsDescriptor(
                    any.constrainedType,
                    any.constrainedTypePsi,
                    any.constrainerClassOrObject,
                    violationsGroup
                )
            }
    }

    private fun getConstrainedTypePsi(constraintResolvedCall: ResolvedCall<*>): PsiElement? {

        val describeFunctionLiteral = constraintResolvedCall.call
            .callElement
            .parent
            ?.parent as? KtFunctionLiteral

        return (describeFunctionLiteral?.getParentCall(bindingContext)
            ?.callElement
            ?.containingNonLocalDeclaration() as? KtClassOrObject)
            ?.superTypeListEntries
            ?.firstOrNull {
                bindingContext.get(BindingContext.TYPE, it.typeReference)
                    ?.getJetTypeFqName(false) == FQ_NAME_CONSTRAINS
            }?.typeAsUserType
            ?.typeArguments
            ?.elementAtOrNull(0)
            ?.finalElement
            ?: verifier.reportError(
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
            ?: return verifier.reportError(
                "Could not find the constrained type",
                constraintResolvedCall.call.callElement
            )
    }

    private fun getDescribeCall(constraintResolvedCall: ResolvedCall<*>): KtElement? {
        return verifier.verifyConstraintIsCalledInsideDescribe(constraintResolvedCall)
    }

    private fun getConstrainerClassOrObject(describeCall: KtElement?): KtClassOrObject? {
        return verifier.verifyConstrainerIsObjectOrRegularClassWithCompanion(describeCall)
    }

    private fun getViolationName(resolvedCall: ResolvedCall<*>): Pair<String, KtExpression>? {

        val nameExpression = resolvedCall.call.getValueArgumentsInParentheses()
            .getOrNull(0)
            ?.getArgumentExpression()
            ?: return verifier.reportError(
                "Unable to find `violation` name",
                resolvedCall.call.callElement
            )

        return verifier.verifyViolationName(nameExpression)
    }

    private fun getViolationParams(resolvedCall: ResolvedCall<*>): List<Param>? {

        fun extractParamEntry(statementResolvedCall: ResolvedCall<*>): Param? {

            val paramType = statementResolvedCall.typeArguments.values.elementAtOrNull(0)
            val paramNameExpression = statementResolvedCall.call
                .getValueArgumentsInParentheses()
                .getOrNull(0)
                ?.getArgumentExpression()

            return when {

                paramType == null -> verifier.reportError(
                    "Unable to find `param` type",
                    statementResolvedCall.call.callElement
                )

                paramNameExpression == null -> verifier.reportError(
                    "Unable to find `param` name",
                    statementResolvedCall.call.callElement
                )

                else -> {
                    verifier.verifyParamName(paramNameExpression)
                        ?.first
                        ?.let { paramName ->
                            Param(
                                paramName,
                                paramNameExpression,
                                paramType
                            )
                        }
                }
            }
        }

        val params = resolvedCall.call
            .functionLiteralArguments
            .getOrNull(0)
            ?.getLambdaExpression()
            ?.bodyExpression
            ?.statements
            ?.mapNotNull {
                it.getResolvedCall(bindingContext)
            }?.filter {
                it.candidateDescriptor.fqNameSafe.asString() == FQ_NAME_PARAM_FUN
            }?.mapNotNull { statementResolvedCall ->
                extractParamEntry(statementResolvedCall)
            } ?: return verifier.reportError(
            "Unable to resolve violation param",
            resolvedCall.call.callElement
        )

        return verifier.verifyNoDuplicateParams(params)
    }
}
