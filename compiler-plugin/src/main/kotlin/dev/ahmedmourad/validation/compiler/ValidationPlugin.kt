package dev.ahmedmourad.validation.compiler

import arrow.meta.CliPlugin
import arrow.meta.Meta
import arrow.meta.phases.CompilerContext
import kotlin.contracts.ExperimentalContracts

class ValidationPlugin : Meta {
    @ExperimentalContracts
    override fun intercept(ctx: CompilerContext): List<CliPlugin> =
        listOf(
            kotlinValidation
        )
}
