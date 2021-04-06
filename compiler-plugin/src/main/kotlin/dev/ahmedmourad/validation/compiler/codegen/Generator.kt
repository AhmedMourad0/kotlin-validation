package dev.ahmedmourad.validation.compiler.codegen

import dev.ahmedmourad.validation.compiler.descriptors.ConstraintsDescriptor

internal interface Generator {
    fun imports(constraintsDescriptor: ConstraintsDescriptor): List<String>
    fun generate(constraintsDescriptor: ConstraintsDescriptor): List<String>
}
