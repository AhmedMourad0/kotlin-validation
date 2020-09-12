import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

group = "dev.ahmedmourad.validation"
version = "0.0.1-SNAPSHOT"

val validationVersion: String by project
val arrowVersion: String by project
val jvmTargetVersion: String by project
val kotlinTestVersion: String by project

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly("io.arrow-kt:arrow-annotations:$arrowVersion")

    api(project(path = ":compiler-plugin", configuration = "shadow"))

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
    compileTestKotlin.kotlinOptions {
        jvmTarget = jvmTargetVersion
    }
    named<ShadowJar>("shadowJar") {
        configurations = listOf(project.configurations.compileOnly.get())
    }
    jar {
        manifest {
            attributes("Main-Class" to "dev.ahmedmourad.validation.sample.MainKt")
        }
    }
}
