import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

group = "dev.ahmedmourad.validation"
version = "0.0.1-SNAPSHOT"

val jvmTargetVersion: String by project
val kotlinVersion: String by project
val arrowMetaVersion: String by project

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly("org.jetbrains.kotlin:kotlin-compiler-embeddable:$kotlinVersion")
    compileOnly("io.arrow-kt:compiler-plugin:$arrowMetaVersion")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = jvmTargetVersion
        }
    }
    named<ShadowJar>("shadowJar") {
        configurations = listOf(project.configurations.compileOnly.get())
        dependencies {
            exclude("org.jetbrains.kotlin:kotlin-stdlib")
            exclude("org.jetbrains.kotlin:kotlin-compiler-embeddable")
        }
    }
    build {
        dependsOn(shadowJar)
    }
}
