import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "dev.ahmedmourad.validation"
version = "0.0.1-SNAPSHOT"

val validationVersion: String by project
val arrowVersion: String by project
val jvmTargetVersion: String by project
val kotlinTestVersion: String by project

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("io.arrow-kt:arrow-annotations:$arrowVersion")

    implementation(project(path = ":core"))
    kotlinCompilerClasspath("org.jetbrains.kotlin:kotlin-script-util:1.3.61") {
        exclude("org.jetbrains.kotlin", "kotlin-stdlib")
        exclude("org.jetbrains.kotlin", "kotlin-compiler")
        exclude("org.jetbrains.kotlin", "kotlin-compiler-embeddable")
    }
    kotlinCompilerClasspath("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.3.61")
    kotlinCompilerClasspath("org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable:1.3.61")
    kotlinCompilerClasspath(project(":core"))

    testImplementation("io.kotlintest:kotlintest-runner-junit4:$kotlinTestVersion")
}

tasks {
    val compileKotlin: KotlinCompile by this
    val compileTestKotlin: KotlinCompile by this
    compileKotlin.kotlinOptions {
        jvmTarget = jvmTargetVersion
        freeCompilerArgs =
            listOf("-Xplugin=${project.rootDir}/compiler-plugin/build/libs/compiler-plugin-$validationVersion-all.jar")
    }
    compileKotlin.dependsOn(":compiler-plugin:createValidationPlugin")
    compileTestKotlin.kotlinOptions {
        jvmTarget = jvmTargetVersion
    }
    jar {
        manifest {
            attributes("Main-Class" to "dev.ahmedmourad.validation.sample.MainKt")
        }
    }
}
