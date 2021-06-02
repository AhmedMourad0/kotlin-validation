pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        jcenter()
    }
}

rootProject.name = "framework"

include("gradle-plugin")
include("compiler-plugin")
include("core")
include("constrainers")
