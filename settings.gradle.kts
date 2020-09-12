
rootProject.name = "validation"
include("compiler-plugin")
include("sample")
include("core")

pluginManagement {
    val kotlinVersion: String by settings
    val shadowJarVersion: String by settings
    plugins {
        kotlin("multiplatform") version kotlinVersion
        id("com.github.johnrengelman.shadow") version shadowJarVersion apply false
    }
}
