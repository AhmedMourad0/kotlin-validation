package dev.ahmedmourad.validation.compiler.codegen

import dev.ahmedmourad.validation.compiler.descriptors.ValidatorDescriptor

internal interface CodeSectionGenerator {
    fun imports(validatorDescriptor: ValidatorDescriptor): Set<String>
    fun generate(validatorDescriptor: ValidatorDescriptor): Set<String>
}
