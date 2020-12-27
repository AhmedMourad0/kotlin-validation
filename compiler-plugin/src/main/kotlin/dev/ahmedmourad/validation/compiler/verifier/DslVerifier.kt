package dev.ahmedmourad.validation.compiler.verifier

import arrow.meta.phases.CompilerContext
import dev.ahmedmourad.validation.compiler.descriptors.Param
import dev.ahmedmourad.validation.compiler.descriptors.Violation
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_CONSTRAINT_FUN
import dev.ahmedmourad.validation.compiler.utils.FQ_NAME_DESCRIBE_FUN
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageUtil
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.descriptors.ClassConstructorDescriptor
import org.jetbrains.kotlin.js.translate.utils.finalElement
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.isIdentifier
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getParentCall
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCall
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.resolve.lazy.descriptors.LazyClassDescriptor
import org.jetbrains.kotlin.types.KotlinType

//TODO: verify location based on annotations and not names to support adding
// elements to extension functions as well
internal class DslVerifier(
    private val cc: CompilerContext,
    private val bindingContext: BindingContext
) {

    internal fun warnOnBooleanReturningExpressions() {
        //TODO
    }

    internal fun verifyParamIsCalledInsideConstraint(
        paramResolvedCall: ResolvedCall<*>
    ): KtElement? {
        return verifyParentBlockAs(
            paramResolvedCall,
            FQ_NAME_CONSTRAINT_FUN,
            "`param` can only be called directly inside a `constraint` block"
        )
    }

    internal fun verifyConstraintIsCalledInsideDescribe(
        constraintResolvedCall: ResolvedCall<*>
    ): KtElement? {
        return verifyParentBlockAs(
            constraintResolvedCall,
            FQ_NAME_DESCRIBE_FUN,
            "`constraint` can only be called directly inside a `describe` block"
        )
    }

    internal fun verifyNoDuplicateViolations(
        violationsPerConstrainer: Map<String?, List<Violation>>
    ): Map<String?, List<Violation>> {
        return violationsPerConstrainer.mapValues { (_, violationsGroup) ->
            violationsGroup.groupBy { it.name }.map { (name, entries) ->
                if (entries.size > 1) {
                    entries.forEach { entry ->
                        reportError(
                            "Duplicate violations: $name",
                            entry.nameExpression
                        )
                    }
                }
                entries.first()
            }
        }
    }

    internal fun verifyNoDuplicateParams(params: List<Param>): List<Param> {
        params.groupBy { it.name }.forEach { (name, entries) ->
            if (entries.size > 1) {
                entries.forEach { entry ->
                    reportError(
                        "Duplicate violation params: $name",
                        entry.nameExpression
                    )
                }

            }
        }
        return params.distinctBy { it.name }
    }

    //TODO: sealed, interface, abstract, inline, empty constructor
    //TODO: warn when no type parameters or value parameters
    //TODO: type params of constrainer and constrained must match
    internal fun verifyConstrainerIsObjectOrRegularClassWithCompanion(
        describeCall: KtElement?
    ): KtClassOrObject? {

        val constrainer = describeCall?.parent
            ?.parent
            ?.parent
            ?.parent as? KtClassOrObject

        return when (constrainer) {

            is KtObjectDeclaration -> constrainer

            is KtClass -> {
                if (constrainer.companionObjects.isEmpty()) {
                    reportError(
                        "A companion object must be provided in order to implement Constrains",
                        constrainer.finalElement
                    )
                } else {
                    constrainer
                }
            }

            else -> {
                reportError(
                    "Only objects and regular classes with companion objects can implement Constrains",
                    constrainer?.finalElement
                )
            }
        }
    }

    internal fun verifyViolationName(nameExpression: KtExpression): Pair<String, KtExpression>? {
        return verifyValidIdentifier(
            nameExpression,
            "Illegal class identifier",
            "`violation` name must be a String literal"
        )
    }

    internal fun verifyParamName(nameExpression: KtExpression): Pair<String, KtExpression>? {
        return verifyValidIdentifier(
            nameExpression,
            "Illegal property identifier",
            "`param` name must be a String literal"
        )
    }

    internal fun verifyConstrainedClassType(
        constrainedType: KotlinType,
        constrainedTypePsi: PsiElement
    ): LazyClassDescriptor? {

        val constrainedClass = constrainedType.constructor
            .declarationDescriptor as? LazyClassDescriptor
            ?: return reportError(
                "Only data classes and regular classes with primary constructors can be constrained",
                constrainedTypePsi
            )

        //TODO: sealed, objects, interfaces, inner
        if (!constrainedClass.isData && constrainedClass.isInline) {
            return reportError(
                "Only data classes and regular classes with primary constructors can be constrained",
                constrainedTypePsi
            )
        }

        return constrainedClass
    }

    internal fun verifyConstrainedClassHasPrimaryConstructor(
        constrainedClass: LazyClassDescriptor,
        constrainedTypePsi: PsiElement
    ): ClassConstructorDescriptor? {

        return constrainedClass.constructors
            .firstOrNull { it.isPrimary }
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
