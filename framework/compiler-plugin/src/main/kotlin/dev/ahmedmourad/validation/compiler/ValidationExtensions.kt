package dev.ahmedmourad.validation.compiler

//TODO: also enforce no vars
//TODO: this's just poc, it's terrible, it will be changed when typed quotes are released
//internal fun Meta.validationsClassDeclarationQuote(cc: CompilerContext): ExtensionPhase = classDeclaration(cc, {
//    this.isAnnotatedWith("@MustBeValid".toRegex())
//}) {
//    this.allConstructors.value.forEach {
//        if (!it.hasModifier(KtTokens.INTERNAL_KEYWORD)) {
//            cc.messageCollector?.report(
//                CompilerMessageSeverity.ERROR,
//                "All constructors of @MustBeValid annotated classes must be internal",
//                MessageUtil.psiElementToMessageLocation(it)
//            )
//        }
//    }
//    Transform.empty
//}
