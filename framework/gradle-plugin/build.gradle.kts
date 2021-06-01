import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.gradle.plugin-publish") version "0.12.0"
    `java-gradle-plugin`
}

apply(plugin = "org.jetbrains.kotlin.jvm")
apply(plugin = "org.jetbrains.kotlin.kapt")
apply(plugin = "com.vanniktech.maven.publish")

group = "dev.ahmedmourad.validation"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

gradlePlugin {
    plugins {
        create("validationPlugin") {
            id = "dev.ahmedmourad.validation.validation-gradle-plugin"
            implementationClass = "dev.ahmedmourad.validation.gradle.ValidationGradlePlugin"
        }
    }
}

val validationVersion: String by project

pluginBundle {
    website = "http://validation.ahmedmourad.dev/"
    vcsUrl = "https://github.com/AhmedMourad0/kotlin-validation"
    description = "The Gradle plugin for kotlin-validation, A multiplatform, declarative, flexible and type-safe Kotlin validation framework."
    tags = listOf(
        "kotlin-compiler",
        "gradle-plugin",
        "intellij-plugin",
        "compiler-plugin",
        "data-class",
        "value-based",
        "annotations",
        "kotlin-extensions",
        "kotlin",
        "kotlin-language",
        "kotlin-library",
        "kotlin-compiler-plugin",
        "inspections",
        "kotlin-plugin"
    )
    (plugins) {
        "validationPlugin" {
            displayName = "Kotlin Validation Gradle Plugin"
            version = validationVersion
        }
    }
    mavenCoordinates {
        groupId = "dev.ahmedmourad.validation"
        artifactId = "validation-gradle-plugin"
        version = validationVersion
    }
}

val compileKotlin: KotlinCompile by project
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

tasks.jar {
    manifest {
        attributes["Specification-Title"] = project.name
        attributes["Specification-Version"] = project.version
        attributes["Implementation-Title"] = "dev.ahmedmourad.validation.validation-gradle-plugin"
        attributes["Implementation-Version"] = project.version
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("gradle-plugin-api"))
    implementation(kotlin("reflect"))
    compileOnly(kotlin("gradle-plugin"))
}
