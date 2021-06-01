plugins {
    kotlin("jvm")
}

group = "dev.ahmedmourad.validation"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation("junit:junit:4.12")
}

val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by project
val compileTestKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by project
compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
}
compileTestKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
}
