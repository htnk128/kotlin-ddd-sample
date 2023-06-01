import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.maven

fun RepositoryHandler.allModuleRepositories() {
    mavenCentral()
    maven("https://plugins.gradle.org/m2/")
}
