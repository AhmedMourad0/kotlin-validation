package dev.ahmedmourad.validation.compiler

import dev.ahmedmourad.validation.compiler.utils.fqNameMustBeValid
import dev.ahmedmourad.validation.compiler.utils.error
import dev.ahmedmourad.validation.compiler.utils.hasAnnotation
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor
import org.jetbrains.kotlin.js.resolve.diagnostics.findPsi
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension

open class ValidationSyntheticResolveExtension(
    private val messageCollector: MessageCollector?
) : SyntheticResolveExtension {

    override fun generateSyntheticMethods(
        thisDescriptor: ClassDescriptor,
        name: Name,
        bindingContext: BindingContext,
        fromSupertypes: List<SimpleFunctionDescriptor>,
        result: MutableCollection<SimpleFunctionDescriptor>
    ) {
        if (thisDescriptor.isData && name.asString() == "copy" &&
            thisDescriptor.hasAnnotation(fqNameMustBeValid)
        ) {

            val generatedCopyMethodIndex = result.findGeneratedCopyMethodIndex(thisDescriptor)

            if (generatedCopyMethodIndex == null) {
                messageCollector?.error("Cannot find generated copy method!", null)
                return
            }

            result.remove(result.elementAt(generatedCopyMethodIndex))

        } else {
            super.generateSyntheticMethods(thisDescriptor, name, bindingContext, fromSupertypes, result)
        }
    }
}

private fun Collection<SimpleFunctionDescriptor>.findGeneratedCopyMethodIndex(
    classDescriptor: ClassDescriptor
): Int? {

    if (size == 1) {
        return 0
    }

    val primaryConstructor = classDescriptor.constructors.firstOrNull { it.isPrimary } ?: return null
    val primaryConstructorParameters = primaryConstructor.valueParameters

    val index = this.indexOfLast {
        it.name.asString() == "copy"
                && it.returnType == classDescriptor.defaultType
                && it.valueParameters.size == primaryConstructorParameters.size
                && it.valueParameters.filterIndexed { index, descriptor ->
            primaryConstructorParameters[index].type != descriptor.type &&
                    primaryConstructorParameters[index].name != descriptor.name
        }.isEmpty()
    }
    return if (index < 0) {
        null
    } else {
        index
    }
}
