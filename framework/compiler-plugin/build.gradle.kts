import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "dev.ahmedmourad.validation"
version = "0.1.0-SNAPSHOT"

val jvmTargetVersion: String by project

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(kotlin("compiler-embeddable"))

    implementation("org.jetbrains.kotlin:kotlin-script-util:1.5.0") {
        exclude("org.jetbrains.kotlin", "kotlin-stdlib")
        exclude("org.jetbrains.kotlin", "kotlin-compiler")
        exclude("org.jetbrains.kotlin", "kotlin-compiler-embeddable")
    }
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.5.0")
    implementation("org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable:1.5.0")

    testImplementation(kotlin("stdlib"))
    testImplementation(kotlin("compiler-embeddable"))
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.3.6")
    testImplementation("junit:junit:4.12")
    testImplementation(project(":core"))
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = jvmTargetVersion
            freeCompilerArgs = listOf("-Xjvm-default=enable")
        }
    }
}
