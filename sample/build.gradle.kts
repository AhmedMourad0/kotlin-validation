import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

apply(plugin = "dev.ahmedmourad.validation.validation-gradle-plugin")

group = "dev.ahmedmourad.validation"
version = "0.1.0-SNAPSHOT"

val validationVersion: String by project
val jvmTargetVersion: String by project

dependencies {
    implementation(kotlin("stdlib"))

    implementation("dev.ahmedmourad.validation:validation-core")
    implementation("dev.ahmedmourad.validation:validation-validators")
    kotlinCompilerClasspath("org.jetbrains.kotlin:kotlin-script-util:1.5.0") {
        exclude("org.jetbrains.kotlin", "kotlin-stdlib")
        exclude("org.jetbrains.kotlin", "kotlin-compiler")
        exclude("org.jetbrains.kotlin", "kotlin-compiler-embeddable")
    }
    kotlinCompilerClasspath("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.5.0")
    kotlinCompilerClasspath("org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable:1.5.0")
//    kotlinCompilerClasspath("dev.ahmedmourad.validation:validation-core")
}

tasks {
    val compileKotlin: KotlinCompile by this
    val compileTestKotlin: KotlinCompile by this
    compileKotlin.kotlinOptions {
        jvmTarget = jvmTargetVersion
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
    compileTestKotlin.kotlinOptions {
        jvmTarget = jvmTargetVersion
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
    }
    jar {
        manifest {
            attributes("Main-Class" to "dev.ahmedmourad.validation.sample.MainKt")
        }
    }
}
