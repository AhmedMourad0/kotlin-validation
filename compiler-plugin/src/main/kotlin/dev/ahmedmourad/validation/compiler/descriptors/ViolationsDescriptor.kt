package dev.ahmedmourad.validation.compiler.descriptors

data class ViolationsDescriptor(
    val packageName: String,
    val constrainedSimpleName: String,
    val text: String
) {
    val packageAsPath by lazy { packageName.replace('.', '/') }
}
