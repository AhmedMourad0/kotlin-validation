package dev.ahmedmourad.validation.compiler.generators

import arrow.meta.phases.CompilerContext
import dev.ahmedmourad.validation.compiler.utils.*
import dev.ahmedmourad.validation.compiler.descriptors.ViolationsDescriptor
import org.jetbrains.kotlin.js.descriptorUtils.getJetTypeFqName
import org.jetbrains.kotlin.js.translate.utils.finalElement
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.kotlin.psi.psiUtil.isIdentifier
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall
import org.jetbrains.kotlin.resolve.calls.callUtil.getValueArgumentsInParentheses
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCall
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.types.KotlinType

data class ViolationEntry(
    val name: String,
    val nameExpression: KtExpression,
    val packageName: String,
    val constrainedName: String,
    val constrainedAsType: KotlinType,
    val text: String
)

data class ParamEntry(
    val name: String,
    val type: KotlinType,
    val nameExpression: KtExpression
)

internal class ViolationsGenerator(
    private val cc: CompilerContext,
    private val bindingContext: BindingContext
) {

    internal fun generateAllViolations(): List<ViolationsDescriptor> {
        val violationEntries = bindingContext.getSliceContents(BindingContext.RESOLVED_CALL)
            .mapNotNull { (_, resolvedCall) ->
                when (resolvedCall?.candidateDescriptor?.fqNameSafe?.asString()) {

                    FQ_NAME_CONSTRAINT -> {
                        val packageName = getPackageName(resolvedCall) ?: return@mapNotNull null
                        val (constrainedName, constrainedAsType) = getConstrainedSimpleName(resolvedCall) ?: return@mapNotNull null
                        val (violationName, violationNameExpression) = getViolationName(resolvedCall) ?: return@mapNotNull null
                        val params = getViolationParams(resolvedCall) ?: return@mapNotNull null
                        ViolationEntry(
                            violationName,
                            violationNameExpression,
                            packageName,
                            constrainedName,
                            constrainedAsType,
                            generateViolationText(
                                getViolationsSealedParentName(constrainedName),
                                violationName,
                                params
                            )
                        )
                    }

                    FQ_NAME_PARAM -> {
                        cc.verifyParentBlockAs(
                            bindingContext,
                            FQ_NAME_CONSTRAINT,
                            resolvedCall,
                            "`param` can only be called directly inside a `constraint` block"
                        )
                        null
                    }

                    else -> null
                }
            }

        return violationEntries.groupBy {
            it.packageName to it.constrainedName
        }.map { (packageNameToConstrainedName, violationEntries) ->

            violationEntries.groupBy { it.name }.forEach { (name, entries) ->
                if (entries.size > 1) {
                    entries.forEach { entry ->
                        cc.reportError(
                            "Duplicate violations: $name",
                            entry.nameExpression
                        )
                    }

                }
            }

            ViolationsDescriptor(
                packageNameToConstrainedName.first,
                packageNameToConstrainedName.second,
                generateViolationsSealedParentText(
                    getViolationsSealedParentName(packageNameToConstrainedName.second),
                    violationEntries.distinctBy { it.name }
                )
            )
        }
    }

    private fun getConstrainedSimpleName(
        resolvedCall: ResolvedCall<*>,
        printTypeArguments: Boolean = false
    ): Pair<String, KotlinType>? {

        val constrainedTypeArgument = resolvedCall.typeArguments
            .values
            .elementAtOrNull(0)
            ?: return cc.reportError("Could not find the constrained type", resolvedCall.call.callElement)

        return constrainedTypeArgument
            .getJetTypeFqName(printTypeArguments)
            .split('.')
            .lastOrNull()
            ?.to(constrainedTypeArgument)
            ?: cc.reportError("Could not find the constrained type", resolvedCall.call.callElement)
    }

    private fun getPackageName(constraintResolvedCall: ResolvedCall<*>): String? {

        val constraintsCall = cc.verifyParentBlockAs(
            bindingContext,
            FQ_NAME_CONSTRAINTS,
            constraintResolvedCall,
            "`constraint` can only be called directly inside a `constraints` block"
        )

        val constrainingObject = constraintsCall?.parent
            ?.parent
            ?.parent as? KtObjectDeclaration

        if (constrainingObject == null) {
            cc.reportError(
                "Only objects and companion objects can implement Constrains",
                constraintsCall?.parent?.parent?.parent?.finalElement
            )
        }

        return (constraintsCall?.containingFile as? KtFile)?.packageFqName?.asString()
    }

    private fun getViolationName(resolvedCall: ResolvedCall<*>): Pair<String, KtExpression>? {

        val nameExpression = resolvedCall.call.getValueArgumentsInParentheses()
            .getOrNull(0)
            ?.getArgumentExpression()
            ?: return cc.reportError(
                "Unable to find `violation` name",
                resolvedCall.call.callElement
            )

        return try {
            val evaluated = cc.eval(nameExpression.text) as String
            if (evaluated.isIdentifier()) {
                evaluated to nameExpression
            } else {
                cc.reportError(
                    "Illegal class identifier",
                    nameExpression
                )
            }
        } catch (e: Exception) {
            cc.reportError(
                "`violation` name must be a String literal",
                nameExpression
            )
        }
    }

    private fun getViolationParams(resolvedCall: ResolvedCall<*>): List<ParamEntry>? {

        fun extractParamEntry(statementResolvedCall: ResolvedCall<*>): ParamEntry? {

            val paramType = statementResolvedCall.typeArguments.values.elementAtOrNull(1)
            val paramNameExpression = statementResolvedCall.call
                .getValueArgumentsInParentheses()
                .getOrNull(0)
                ?.getArgumentExpression()

            return when {

                paramType == null -> cc.reportError(
                    "Unable to find `param` type",
                    statementResolvedCall.call.callElement
                )

                paramNameExpression == null -> cc.reportError(
                    "Unable to find `param` name",
                    statementResolvedCall.call.callElement
                )

                else -> {

                    val paramName = try {
                        val evaluated = cc.eval(paramNameExpression.text) as String
                        if (evaluated.isIdentifier()) {
                            evaluated
                        } else {
                            cc.reportError(
                                "Illegal property identifier",
                                paramNameExpression
                            )
                        }
                    } catch (e: Exception) {
                        cc.reportError(
                            "`param` name must be a String literal",
                            paramNameExpression
                        )
                    }

                    paramName?.let {
                        ParamEntry(
                            it,
                            paramType,
                            paramNameExpression
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
                it.candidateDescriptor.fqNameSafe.asString() == FQ_NAME_PARAM
            }?.mapNotNull { statementResolvedCall ->
                extractParamEntry(statementResolvedCall)
            } ?: return cc.reportError(
            "Unable resolve violation param",
            resolvedCall.call.callElement
        )

        params.groupBy { it.name }.forEach { (name, entries) ->
            if (entries.size > 1) {
                entries.forEach { entry ->
                    cc.reportError(
                        "Duplicate violation params: $name",
                        entry.nameExpression
                    )
                }

            }
        }

        return params.distinctBy { it.name }
    }

    private fun getViolationsSealedParentName(constrainedName: String): String {
        return constrainedName + VIOLATIONS_SUPER_CLASS_SUFFIX
    }

    private fun generateViolationsSealedParentText(
        name: String,
        violations: List<ViolationEntry>
    ): String {
        return """
        |sealed class $name {
        |    ${violations.joinToString("\n\t") { it.text.replace("\n", "\n\t") }}
        |}
        """.trimMargin()
    }

    private fun generateViolationText(
        sealedParentName: String,
        violationName: String,
        params: List<ParamEntry>
    ): String {
        return if (params.isEmpty()) {
            "object $violationName : $sealedParentName()"
        } else {
            val typedParams = params.map { (paramName, paramType, _) ->
                "val $paramName: ${paramType.getJetTypeFqName(true)}"
            }
            "data class $violationName(\n\t${typedParams.joinToString(",\n\t")}\n) : $sealedParentName()"
        }
    }
}
