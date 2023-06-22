package dev.ahmedmourad.validation.compiler.utils

import org.jetbrains.kotlin.cli.common.messages.CompilerMessageLocation
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.common.messages.MessageUtil
import com.intellij.psi.PsiElement

internal fun MessageCollector.error(message: String, element: PsiElement?) {
    this.report(
            CompilerMessageSeverity.ERROR,
            message,
            MessageUtil.psiElementToMessageLocation(element)
    )
}

internal fun MessageCollector.log(message: String) {
    this.report(
            CompilerMessageSeverity.LOGGING,
            "Kotlin Validation: $message",
            CompilerMessageLocation.create(null)
    )
}
