pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}
rootProject.name = "kotlin-spring-boot-ddd-sample"

include(":shared")
include(":ddd-core")

include(":customer")
include(":address")
include(":contact-details")
