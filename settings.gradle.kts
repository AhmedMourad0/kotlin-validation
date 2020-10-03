
rootProject.name = "validation"
include("compiler-plugin")
include("sample")
include("core")

pluginManagement {
    val kotlinVersion: String by settings
    plugins {
        kotlin("jvm") version kotlinVersion
    }
}
