
plugins {
    kotlin("jvm")
}

allprojects {
    repositories {
        mavenCentral()
        maven(url = "https://oss.jfrog.org/artifactory/oss-snapshot-local/")
        jcenter()
    }
}
