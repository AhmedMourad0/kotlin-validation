package dev.ahmedmourad.validation.compiler.utils

import arrow.meta.phases.CompilerContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageUtil
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtFunctionLiteral
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getParentCall
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCall
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe

internal fun CompilerContext.verifyParentBlockAs(
    bindingContext: BindingContext,
    fqName: String,
    resolvedCall: ResolvedCall<*>,
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

    if (parentFqName != fqName) {
        reportError(
            message,
            resolvedCall.call.callElement
        )
    }

    return parentFunctionCall
}

internal fun CompilerContext.reportError(message: String, psiElement: PsiElement?): Nothing? {
    messageCollector?.report(
        CompilerMessageSeverity.ERROR,
        message,
        MessageUtil.psiElementToMessageLocation(psiElement)
    )
    return null
}
