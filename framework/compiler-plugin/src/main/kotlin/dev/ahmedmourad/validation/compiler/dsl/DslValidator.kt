package dev.ahmedmourad.validation.compiler.dsl

import com.intellij.psi.PsiElement
import dev.ahmedmourad.validation.compiler.descriptors.ValidatorDescriptor
import dev.ahmedmourad.validation.compiler.descriptors.MetaDescriptor
import dev.ahmedmourad.validation.compiler.utils.fqNameConstraintFun
import dev.ahmedmourad.validation.compiler.utils.fqNameDescribeFun
import dev.ahmedmourad.validation.compiler.utils.OUTPUT_FOLDER
import dev.ahmedmourad.validation.compiler.utils.simpleName
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.common.messages.MessageUtil
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.psi.psiUtil.containingClassOrObject
import org.jetbrains.kotlin.psi.psiUtil.isIdentifier
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.util.getParentCall
import org.jetbrains.kotlin.resolve.calls.util.getResolvedCall
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCall
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameOrNull
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmLocalScriptEngineFactory
import org.jetbrains.kotlin.utils.addToStdlib.safeAs

//TODO: validate validator is not an inner class or anything that can use the type params of something else
internal class DslValidator(
    private val bindingContext: BindingContext,
    private val messageCollector: MessageCollector
) {

    internal fun isConstructorCallAllowed(
        validatorDescriptor: ValidatorDescriptor,
        constructorResolvedCall: ResolvedCall<*>
    ): Boolean {

        val secondaryConstructorOwnerFqName = constructorResolvedCall.call
            .callElement
            .parent
            .safeAs<KtSecondaryConstructor>()
            ?.containingClassOrObject
            ?.fqName

        val callerFqName = constructorResolvedCall.call
            .callElement
            .parent
            ?.parent
            ?.parent
            ?.parent
            ?.safeAs<KtFunction>()
            ?.fqName

        val subjectClass = validatorDescriptor.subjectClass

        if (secondaryConstructorOwnerFqName == subjectClass?.fqNameOrNull()) {
            return true
        }

        val allowedCaller =
            "${validatorDescriptor.packageName}.$OUTPUT_FOLDER.${validatorDescriptor.subjectType.simpleName()}"

        return callerFqName?.asString() == allowedCaller
    }

    internal fun verifyMetaIsCalledDirectlyInsideConstraint(
        metaResolvedCall: ResolvedCall<*>
    ): KtElement? {
        return verifyParentBlockAs(
            metaResolvedCall,
            fqNameConstraintFun,
            "`${metaResolvedCall.candidateDescriptor.name.asString()}` can only be called directly inside a `constraint` block"
        )
    }

    internal fun verifyConstraintIsCalledDirectlyInsideDescribe(
        constraintResolvedCall: ResolvedCall<*>
    ): KtElement? {
        return verifyParentBlockAs(
            constraintResolvedCall,
            fqNameDescribeFun,
            "`constraint` can only be called directly inside a `describe` block"
        )
    }

    internal fun verifyNoDuplicateViolations(
        validatorDescriptor: ValidatorDescriptor
    ): ValidatorDescriptor {
        validatorDescriptor.violations.groupBy { it.name }.forEach { (name, entries) ->
            if (entries.size > 1) {
                entries.forEach { entry ->
                    reportError(
                        "Duplicate violation: $name",
                        entry.nameExpression
                    )
                }
            }
        }
        return validatorDescriptor
    }

    internal fun verifyNoDuplicateMetas(metas: Sequence<MetaDescriptor>): Sequence<MetaDescriptor> {
        metas.groupBy { it.name }.forEach { (name, entries) ->
            if (entries.size > 1) {
                entries.forEach { entry ->
                    reportError(
                        "Duplicate violation meta: $name",
                        entry.nameExpression
                    )
                }

            }
        }
        return metas.distinctBy { it.name }
    }

    internal fun verifyViolationName(nameExpression: KtExpression): Pair<String, KtExpression>? {
        return verifyValidIdentifier(
            nameExpression,
            "Illegal class identifier",
            "Violation name must be a String literal"
        )
    }

    internal fun verifyMetaName(nameExpression: KtExpression): Pair<String, KtExpression>? {
        return verifyValidIdentifier(
            nameExpression,
            "Illegal property identifier",
            "Meta name must be a String literal"
        )
    }

    internal fun verifySubjectAlias(alias: String, element: PsiElement?): String? {
        return verifyValidIdentifier(
            alias,
            element,
            "Illegal class identifier"
        )
    }

    private fun verifyValidIdentifier(
        nameExpression: KtExpression,
        illegalIdentifierMessage: String,
        nonStringLiteralMessage: String
    ): Pair<String, KtExpression>? {
        return try {
            System.setProperty("kotlin.script.classpath", "kotlin-scripting-jsr223")
            val evaluated = KotlinJsr223JvmLocalScriptEngineFactory().scriptEngine.eval(nameExpression.text) as String
            if (evaluated.isIdentifier()) {
                evaluated to nameExpression
            } else {
                reportError(
                    illegalIdentifierMessage,
                    nameExpression
                )
            }
        } catch (e: Exception) {
            throw e
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
        expectedParent: FqName,
        message: String
    ): KtElement? {

        val parentFunctionLiteral = resolvedCall.call
            .callElement
            .parent
            ?.parent as? KtFunctionLiteral

        val parentFunctionCall = parentFunctionLiteral?.getParentCall(bindingContext)
            ?.callElement

        val parent = parentFunctionCall?.getResolvedCall(bindingContext)
            ?.candidateDescriptor
            ?.fqNameSafe

        if (parent != expectedParent) {
            reportError(
                message,
                resolvedCall.call.callElement
            )
        }

        return parentFunctionCall
    }

    internal fun reportError(message: String, psiElement: PsiElement?): Nothing? {
        messageCollector.report(
            CompilerMessageSeverity.ERROR,
            message,
            MessageUtil.psiElementToMessageLocation(psiElement)
        )
        return null
    }
}
