pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}
rootProject.name = "kotlin-ddd-sample"

include(":shared")
include(":ddd-core")

include(":account")
include(":address")
