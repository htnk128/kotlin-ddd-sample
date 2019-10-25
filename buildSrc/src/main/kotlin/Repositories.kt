import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.maven

fun RepositoryHandler.allModuleRepositories() {
    mavenCentral()
    jcenter()
    maven("http://oss.jfrog.org/artifactory/oss-snapshot-local/")
}
