package dev.ahmedmourad.validation.compiler.descriptors

import dev.ahmedmourad.validation.compiler.utils.VIOLATIONS_SUPER_CLASS_SUFFIX
import dev.ahmedmourad.validation.compiler.utils.simpleName
import org.jetbrains.kotlin.com.intellij.psi.PsiElement
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtClassOrObject
import org.jetbrains.kotlin.types.KotlinType

data class ConstraintsDescriptor constructor(
    val constrainedType: KotlinType,
    val constrainedTypePsi: PsiElement,
    val constrainerClassOrObject: KtClassOrObject,
    val violations: List<Violation>
) {
    val packageName by lazy { (constrainerClassOrObject.containingFile as KtFile).packageFqName.asString() }
    val packageAsPath by lazy { packageName.replace('.', '/') }
    val violationsParentName by lazy { constrainedType.simpleName() + VIOLATIONS_SUPER_CLASS_SUFFIX }
}

data class Violation(
    val name: String,
    val nameExpression: KtExpression,
    val constrainedType: KotlinType,
    val constrainedTypePsi: PsiElement,
    val constrainerClassOrObject: KtClassOrObject,
    val params: List<Param>
)

data class Param(
    val name: String,
    val nameExpression: KtExpression,
    val type: KotlinType
)
