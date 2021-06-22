
rootProject.name = "validation"
include("validators")
include("sample")

pluginManagement {
    val kotlinVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion
    }
}

includeBuild("framework") {
    dependencySubstitution {
        substitute(module("dev.ahmedmourad.validation:validation-gradle-plugin")).with(project(":gradle-plugin"))
        substitute(module("dev.ahmedmourad.validation:validation-compiler-plugin")).with(project(":compiler-plugin"))
        substitute(module("dev.ahmedmourad.validation:validation-core")).with(project(":core"))
    }
}
