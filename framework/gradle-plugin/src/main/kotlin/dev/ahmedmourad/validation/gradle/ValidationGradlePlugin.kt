package dev.ahmedmourad.validation.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.*

class ValidationGradlePlugin : KotlinCompilerPluginSupportPlugin {

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean {
        return kotlinCompilation.target.project.plugins.hasPlugin(ValidationGradlePlugin::class.java)
    }

    override fun getCompilerPluginId(): String = "validation-compiler-plugin"

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = "dev.ahmedmourad.validation",
        artifactId = "validation-compiler-plugin",
        version = "0.1.0-SNAPSHOT"
    )

    override fun apply(target: Project) {
        target.extensions.create("kotlin-validation", ValidationGradleExtension::class.java)
    }

    override fun applyToCompilation(
        kotlinCompilation: KotlinCompilation<*>
    ): Provider<List<SubpluginOption>> {

//        project.dependencies.add(
//            "implementation",
//            "dev.ahmedmourad.validation:validation-core:0.1.0-SNAPSHOT"
//        )

        val project = kotlinCompilation.target.project
        return project.provider { emptyList() }
    }
}

open class ValidationGradleExtension
