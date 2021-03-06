
plugins {
    kotlin("jvm")
}

buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }

    val kotlinVersion: String by project
    val mavenPublishPluginVersion: String by project
    val validationVersion: String by project

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.vanniktech:gradle-maven-publish-plugin:$mavenPublishPluginVersion")
        classpath("dev.ahmedmourad.validation:validation-gradle-plugin:$validationVersion")
    }
}

allprojects {
    repositories {
        mavenCentral()
        maven(url = "https://oss.jfrog.org/artifactory/oss-snapshot-local/")
        jcenter()
    }
}
