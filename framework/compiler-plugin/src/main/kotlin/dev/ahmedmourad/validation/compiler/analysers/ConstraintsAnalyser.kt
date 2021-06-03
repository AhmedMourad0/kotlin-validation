package dev.ahmedmourad.validation.compiler.analysers

import dev.ahmedmourad.validation.compiler.descriptors.*
import dev.ahmedmourad.validation.compiler.dsl.DslValidator
import dev.ahmedmourad.validation.compiler.utils.*
import org.jetbrains.kotlin.descriptors.ClassConstructorDescriptor
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.callUtil.getResolvedCall
import org.jetbrains.kotlin.resolve.calls.callUtil.getValueArgumentsInParentheses
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCall
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameOrNull
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.typeUtil.supertypes
import org.jetbrains.kotlin.utils.addToStdlib.safeAs

internal class ConstraintsAnalyser(
    private val bindingContext: BindingContext,
    private val dslValidator: DslValidator
) {
    fun analyse(projectFiles: Collection<KtFile>): Sequence<ConstraintsDescriptor> {

        val unverifiedConstructorCalls = /*mutableListOf<ResolvedCall<*>>()*/bindingContext.getSliceContents(BindingContext.RESOLVED_CALL)
            .asSequence()
            .mapNotNull(Map.Entry<Call, ResolvedCall<*>>::value)
            .onEach { resolvedCall ->
                when {
                    resolvedCall.candidateDescriptor.fqNameSafe == fqNameConstraintFun -> {
                        dslValidator.verifyConstraintIsCalledDirectlyInsideDescribe(resolvedCall)
                    }
                    resolvedCall.candidateDescriptor.hasAnnotation(fqNameMeta) -> {
                        dslValidator.verifyMetaIsCalledDirectlyInsideConstraint(resolvedCall)
                    }
                }
            }.filter { resolvedCall ->
                resolvedCall.candidateDescriptor
                    .safeAs<ClassConstructorDescriptor>()
                    ?.containingDeclaration
                    ?.hasAnnotation(fqNameMustBeValid) == true
            }.toMutableList()
//        throw RuntimeException(projectFiles.asSequence().flatMap {
//            it.classesAndInnerClasses().map {
//                it.superTypeListEntries.map {
//                    it.typeReference?.kotlinType(bindingContext)
//                }.toString()
//            }
//        }.toList().toString())
        val constraintsDescriptors = projectFiles.asSequence().flatMap {
            it.classesAndInnerClasses().filter { classOrObject ->
                classOrObject.hasSuperType(bindingContext, fqNameConstrains)
            }
        }/*.also {
            throw RuntimeException("XXXXXXXXXXXX${it.count()}")
        }*/.mapNotNull { constrainer ->

            dslValidator.reportError(constrainer.nameAsSafeName.asString(), null)

            val constraintCalls = mutableListOf<ResolvedCall<*>>()
            val metaCalls = mutableListOf<ResolvedCall<*>>()

            constrainer.getOrCreateBody().properties.firstOrNull {
                it.nameAsSafeName == propertyConstraints
            }?.delegate //TODO: this should be segregated with an error conditions
                ?.expression
                ?.safeAs<KtCallExpression>()
                ?.lambdaArguments
                ?.firstOrNull()
                ?.getLambdaExpression()
                ?.bodyExpression
                ?.statements
                ?.mapNotNull {
                    it.getResolvedCall(bindingContext)
                }?.forEach { resolvedCall ->
                    when {
                        resolvedCall.candidateDescriptor.fqNameSafe == fqNameConstraintFun -> {
                            constraintCalls += resolvedCall
                        }
                        resolvedCall.candidateDescriptor.hasAnnotation(fqNameMeta) -> {
                            metaCalls += resolvedCall
                        }
                    }
                } ?: return@mapNotNull null

            val constrainedType = findConstrainedType(constrainer) ?: return@mapNotNull null

            val violationsDescriptors = constraintCalls.mapNotNull innerMap@{ violationResolvedCall ->
                val (name, nameExpression) = findViolationName(violationResolvedCall) ?: return@innerMap null
                val metas = findAllMetas(violationResolvedCall) ?: return@innerMap null
                ViolationDescriptor(
                    name,
                    nameExpression,
                    metas
                )
            }

            dslValidator.verifyNoDuplicateViolations(
                ConstraintsDescriptor(
                    bindingContext,
                    dslValidator,
                    constrainedType,
                    constrainer,
                    violationsDescriptors
                )
            )
        }.onEach { descriptor ->
            unverifiedConstructorCalls.removeIf {
                dslValidator.isConstructorCallIsAllowed(descriptor, it)
            }
        }

        unverifiedConstructorCalls.forEach { call ->
            val constrainedName = call.candidateDescriptor
                .containingDeclaration
                .fqNameOrNull()
                ?.shortName()
                ?.asString()
            dslValidator.reportError(
                "Only validated instances of $constrainedName can be created",
                call.call.callElement
            )
        }

        return constraintsDescriptors
    }

    private fun findConstrainedType(constrainer: KtClassOrObject): KotlinType? {

        val constrainsSuperType = constrainer.findSuperType(bindingContext, fqNameConstrains)

        return constrainsSuperType?.typeAsUserType
            ?.typeArguments
            ?.elementAtOrNull(0)
            ?.typeReference
            ?.kotlinType(bindingContext)
            ?: dslValidator.reportError(
                "Failed to resolve constrained type",
                constrainsSuperType
            )
    }

    private fun findViolationName(constraintResolvedCall: ResolvedCall<*>): Pair<String, KtExpression>? {

        val nameExpression = constraintResolvedCall.call.getValueArgumentsInParentheses()
            .getOrNull(0)
            ?.getArgumentExpression()
            ?: return dslValidator.reportError(
                "Unable to find `violation` name",
                constraintResolvedCall.call.callElement
            )

        return dslValidator.verifyViolationName(nameExpression)
    }

    private fun findAllMetas(constraintResolvedCall: ResolvedCall<*>): List<MetaDescriptor>? {

        val metas = constraintResolvedCall.call
            .functionLiteralArguments
            .getOrNull(0)
            ?.getLambdaExpression()
            ?.bodyExpression
            ?.statements
            ?.asSequence()
            ?.mapNotNull {
                it.getResolvedCall(bindingContext)
            }?.filter {
                it.candidateDescriptor.hasAnnotation(fqNameMeta)
            }?.mapNotNull(::findMeta)
            ?: return dslValidator.reportError(
                "Unable to resolve violation meta",
                constraintResolvedCall.call.callElement
            )

        return dslValidator.verifyNoDuplicateMetas(metas).toList()
    }

    private fun findMeta(statementResolvedCall: ResolvedCall<*>): MetaDescriptor? {

        val (includedConstraint, metaTypeFqName) = findMetaType(
            statementResolvedCall
        ) ?: return dslValidator.reportError(
            "Unable to find `meta` type",
            statementResolvedCall.call.callElement
        )

        val nameIndex = statementResolvedCall.candidateDescriptor
            .valueParameters
            .firstOrNull { it.hasAnnotation(fqNameMetaName) }
            ?.index
            ?: return dslValidator.reportError(
                "Unable to find `meta` name",
                statementResolvedCall.call.callElement
            )

        val metaNameExpression = statementResolvedCall.call
            .valueArguments
            .elementAtOrNull(nameIndex)
            ?.getArgumentExpression()
            ?: return dslValidator.reportError(
                "Unable to find `meta` name",
                statementResolvedCall.call.callElement
            )

        return dslValidator.verifyMetaName(metaNameExpression)?.let { (metaName, _) ->
            MetaDescriptor(
                metaName,
                metaNameExpression,
                metaTypeFqName,
                includedConstraint
            )
        }
    }

    private fun findMetaType(statementResolvedCall: ResolvedCall<*>): Pair<IncludedConstraintDescriptor?, String>? {

        val metaType = statementResolvedCall.typeArguments.entries.firstOrNull { (meta, _) ->
            meta.hasAnnotation(fqNameMetaType)
        } ?: return null

        return if (metaType.key.hasAnnotation(fqNameInclusionType)) {

            val constrainsSuperType = metaType.value
                ?.supertypes()
                ?.firstOrNull { it.fqNameSafe == fqNameConstrains }
                ?: return null

            val constrainedAlias = metaType.value
                ?.constructor
                ?.declarationDescriptor
                ?.findAnnotation(fqNameConstrainerConfig)
                ?.allValueArguments
                ?.get(paramConstrainedAlias)
                ?.value
                ?.safeAs<String>()

            val constrainedType = constrainsSuperType.arguments
                .firstOrNull()
                ?.type
                ?: return null

            val constrainedAliasOrSimpleName = constrainedAlias ?: constrainedType.simpleName() ?: return null

            val validationsFileFqName = metaType.value.constructor
                .declarationDescriptor
                ?.ktFile()
                ?.packageFqName
                ?.asString()
                ?.plus(".")
                ?.plus(OUTPUT_FOLDER)
                ?: return null

            IncludedConstraintDescriptor(
                validationsFileFqName,
                constrainedType,
                metaType.value,
                constrainedAliasOrSimpleName
            ) to "Set<$validationsFileFqName.$constrainedAliasOrSimpleName$SUFFIX_VIOLATIONS_SUPER_CLASS>"

        } else {
            //TODO: if the metaTypeArg is a type param, inherit it from the constrainer .. or its parents?
            null to metaType.value.deepFqName()
        }
    }
}
