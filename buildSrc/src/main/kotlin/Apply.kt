import org.gradle.api.plugins.ObjectConfigurationAction

fun ObjectConfigurationAction.allModule() {
    plugin("kotlin")
    plugin("idea")
    plugin("org.jlleitschuh.gradle.ktlint")
    plugin("org.jetbrains.kotlinx.kover")
}

fun ObjectConfigurationAction.spring() {
    plugin("io.spring.dependency-management")
}
