
plugins {
    kotlin("multiplatform")
    id("com.github.johnrengelman.shadow")
}

allprojects {
    repositories {
        mavenCentral()
        maven(url = "https://oss.jfrog.org/artifactory/oss-snapshot-local/")
        jcenter()
    }
}
