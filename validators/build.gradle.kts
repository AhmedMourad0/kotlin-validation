plugins {
    kotlin("multiplatform")
}

group = "dev.ahmedmourad.validation"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm()
    js {
        browser()
        nodejs()
    }
    ios()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("dev.ahmedmourad.validation:validation-core")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
