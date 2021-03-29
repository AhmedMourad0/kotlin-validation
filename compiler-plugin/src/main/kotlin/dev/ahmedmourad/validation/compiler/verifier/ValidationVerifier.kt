package dev.ahmedmourad.validation.compiler.verifier

import arrow.meta.phases.CompilerContext
import dev.ahmedmourad.validation.compiler.descriptors.ConstraintsDescriptor
import dev.ahmedmourad.validation.compiler.descriptors.ParamDescriptor
import dev.ahmedmourad.validation.compiler.descriptors.ViolationDescriptor
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_CONSTRAINT_FUN
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_DESCRIBE_FUN
import dev.ahmedmourad.validation.compiler.utils.OUTPUT_FOLDER
import dev.ahmedmourad.validation.compiler.utils.simpleName
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageUtil
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.descriptors.ClassConstructorDescriptor
import org.jetbrains.kotlin.descriptors.ConstructorDescriptor
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.containingClassOrObject
import org.jetbrains.kotlin.psi.psiUtil.isIdentifier
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getParentCall
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCall
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameOrNull
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.resolve.lazy.descriptors.LazyClassDescriptor
import org.jetbrains.kotlin.utils.addToStdlib.safeAs

internal class ValidationVerifier(
    private val cc: CompilerContext,
    private val bindingContext: BindingContext
) {

    internal fun verifyConstructorCallIsAllowed(
        constraintsDescriptor: ConstraintsDescriptor?,
        constructorResolvedCall: ResolvedCall<*>
    ): ResolvedCall<*>? {

        constraintsDescriptor ?: return null

        val secondaryConstructorOwnerFqName = constructorResolvedCall.call
            .callElement
            .parent
            .safeAs<KtSecondaryConstructor>()
            ?.containingClassOrObject
            ?.fqName
            ?.asString()

        if (secondaryConstructorOwnerFqName == constraintsDescriptor.constrainedClass?.fqNameOrNull()?.asString()) {
            return null
        }

        val callerFqName = constructorResolvedCall.call
            .callElement
            .parent
            ?.parent
            ?.parent
            ?.parent
            ?.safeAs<KtFunction>()
            ?.fqName
            ?.asString()

        val allowedCaller = "${constraintsDescriptor.packageName}.$OUTPUT_FOLDER.${constraintsDescriptor.constrainedType.simpleName()}"

        return if (callerFqName != allowedCaller) {
            reportError(
                "Only validated instances of ${constraintsDescriptor.constrainedType.simpleName()} can be created",
                constructorResolvedCall.call.callElement
            )
        } else {
            constructorResolvedCall
        }
    }

    internal fun verifyParamIsCalledDirectlyInsideConstraint(
        paramResolvedCall: ResolvedCall<*>
    ): KtElement? {
        return verifyParentBlockAs(
            paramResolvedCall,
            FQ_NAME_CONSTRAINT_FUN,
            "`${paramResolvedCall.candidateDescriptor.name.asString()}` can only be called directly inside a `constraint` block"
        )
    }

    //TODO: verify describe is only called where it should be
    internal fun verifyConstraintIsCalledDirectlyInsideDescribe(
        constraintResolvedCall: ResolvedCall<*>
    ): KtElement? {
        return verifyParentBlockAs(
            constraintResolvedCall,
            FQ_NAME_DESCRIBE_FUN,
            "`constraint` can only be called directly inside a `describe` block"
        )
    }

    internal fun verifyNoDuplicateViolations(
        violationsPerConstrainer: Map<String?, List<ViolationDescriptor>>
    ): Map<String?, List<ViolationDescriptor>> {
        return violationsPerConstrainer.mapValues { (_, violationsGroup) ->
            violationsGroup.groupBy { it.name }.map { (name, entries) ->
                if (entries.size > 1) {
                    entries.forEach { entry ->
                        reportError(
                            "Duplicate violation: $name",
                            entry.nameExpression
                        )
                    }
                }
                entries.first()
            }
        }
    }

    internal fun verifyNoDuplicateParams(params: Sequence<ParamDescriptor>): Sequence<ParamDescriptor> {
        params.groupBy { it.name }.forEach { (name, entries) ->
            if (entries.size > 1) {
                entries.forEach { entry ->
                    reportError(
                        "Duplicate violation param: $name",
                        entry.nameExpression
                    )
                }

            }
        }
        return params.distinctBy { it.name }
    }

    internal fun verifyViolationName(nameExpression: KtExpression): Pair<String, KtExpression>? {
        return verifyValidIdentifier(
            nameExpression,
            "Illegal class identifier",
            "Violation name must be a String literal"
        )
    }

    internal fun verifyParamName(nameExpression: KtExpression): Pair<String, KtExpression>? {
        return verifyValidIdentifier(
            nameExpression,
            "Illegal property identifier",
            "Param name must be a String literal"
        )
    }

    internal fun verifyConstrainedAlias(alias: String, element: PsiElement?): String? {
        return verifyValidIdentifier(
            alias,
            element,
            "Illegal class identifier"
        )
    }

    internal fun verifyConstrainedClassHasPrimaryConstructor(
        constrainedClass: LazyClassDescriptor,
        constrainedTypePsi: PsiElement
    ): ClassConstructorDescriptor? {
        return constrainedClass.constructors
            .firstOrNull(ConstructorDescriptor::isPrimary)
            ?: reportError(
                "Only data classes and regular classes with primary constructors can be constrained",
                constrainedTypePsi
            )
    }

    private fun verifyValidIdentifier(
        nameExpression: KtExpression,
        illegalIdentifierMessage: String,
        nonStringLiteralMessage: String
    ): Pair<String, KtExpression>? {
        return try {
            val evaluated = cc.eval(nameExpression.text) as String
            if (evaluated.isIdentifier()) {
                evaluated to nameExpression
            } else {
                reportError(
                    illegalIdentifierMessage,
                    nameExpression
                )
            }
        } catch (e: Exception) {
            reportError(
                nonStringLiteralMessage,
                nameExpression
            )
        }
    }

    private fun verifyValidIdentifier(
        name: String,
        element: PsiElement?,
        illegalIdentifierMessage: String
    ): String? {
        return if (name.isIdentifier()) {
            name
        } else {
            reportError(
                illegalIdentifierMessage,
                element
            )
        }
    }

    private fun verifyParentBlockAs(
        resolvedCall: ResolvedCall<*>,
        expectedParentFqName: String,
        message: String
    ): KtElement? {

        val parentFunctionLiteral = resolvedCall.call
            .callElement
            .parent
            ?.parent as? KtFunctionLiteral

        val parentFunctionCall = parentFunctionLiteral?.getParentCall(bindingContext)
            ?.callElement

        val parentFqName = parentFunctionCall?.getResolvedCall(bindingContext)
            ?.candidateDescriptor
            ?.fqNameSafe
            ?.asString()

        if (parentFqName != expectedParentFqName) {
            reportError(
                message,
                resolvedCall.call.callElement
            )
        }

        return parentFunctionCall
    }

    internal fun reportError(message: String, psiElement: PsiElement?): Nothing? {
        cc.messageCollector?.report(
            CompilerMessageSeverity.ERROR,
            message,
            MessageUtil.psiElementToMessageLocation(psiElement)
        )
        return null
    }
}
