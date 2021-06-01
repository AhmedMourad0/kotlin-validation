package dev.ahmedmourad.validation.compiler.codegen

import dev.ahmedmourad.validation.compiler.descriptors.ConstraintsDescriptor

internal interface CodeSectionGenerator {
    fun imports(constraintsDescriptor: ConstraintsDescriptor): Set<String>
    fun generate(constraintsDescriptor: ConstraintsDescriptor): Set<String>
}
