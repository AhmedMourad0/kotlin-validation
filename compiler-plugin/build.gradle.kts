import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.nio.file.Paths

plugins {
    kotlin("jvm")
}

group = "dev.ahmedmourad.validation"
version = "0.0.1-SNAPSHOT"

val jvmTargetVersion: String by project
val arrowMetaVersion: String by project

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(kotlin("compiler-embeddable"))
    compileOnly("io.arrow-kt:compiler-plugin:$arrowMetaVersion")

    implementation("org.jetbrains.kotlin:kotlin-script-util:1.3.61") {
        exclude("org.jetbrains.kotlin", "kotlin-stdlib")
        exclude("org.jetbrains.kotlin", "kotlin-compiler")
        exclude("org.jetbrains.kotlin", "kotlin-compiler-embeddable")
    }
    implementation("org.jetbrains.kotlin:kotlin-compiler-embeddable:1.3.61")
    implementation("org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable:1.3.61")

    testImplementation(kotlin("stdlib"))
    testImplementation(kotlin("compiler-embeddable"))
    testImplementation("io.arrow-kt:compiler-plugin:$arrowMetaVersion")
    testImplementation("com.github.tschuchortdev:kotlin-compile-testing:1.2.6")
    testImplementation("junit:junit:4.12")
    testImplementation(project(":core"))
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = jvmTargetVersion
        }
    }
    register<org.gradle.jvm.tasks.Jar>("createValidationPlugin") {
        dependsOn(classes)
        archiveClassifier.set("all")
        from("build/classes/kotlin/main")
        from("build/resources/main")
        from(sourceSets.main.get().compileClasspath.find { it.absolutePath.contains(Paths.get("io.arrow-kt","compiler-plugin").toString()) }!!.let(::zipTree)) {
            exclude("META-INF/services/org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar")
        }
    }
}
