
buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }

    val kotlinVersion: String by project
    val mavenPublishPluginVersion: String by project

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.vanniktech:gradle-maven-publish-plugin:$mavenPublishPluginVersion")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
}
