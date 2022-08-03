import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.version
import org.gradle.plugin.use.PluginDependenciesSpec

fun PluginDependenciesSpec.allModulePlugins() {
    kotlin("jvm") version Versions.kotlin
    id("org.jlleitschuh.gradle.ktlint") version Versions.ktlint
    id("jacoco")
}

fun PluginDependenciesSpec.springPlugins() {
    id("org.springframework.boot") version Versions.springBoot
    kotlin("plugin.spring") version Versions.kotlin
}
