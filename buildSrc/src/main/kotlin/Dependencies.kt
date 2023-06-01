import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.project

fun DependencyHandler.implementation(dependency: Any) {
    add("implementation", dependency)
}

fun DependencyHandler.testImplementation(dependency: Any) {
    add("testImplementation", dependency)
}

fun DependencyHandler.runtimeOnly(dependency: Any) {
    add("runtimeOnly", dependency)
}

fun DependencyHandler.testRuntimeOnly(dependency: Any) {
    add("testRuntimeOnly", dependency)
}

fun DependencyHandler.allModuleDependencies() {
    // Kotlin
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))
    // Test
    testImplementation("org.junit.jupiter:junit-jupiter-api:${Versions.junit}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${Versions.junit}")
    testImplementation("io.kotlintest:kotlintest-runner-junit5:${Versions.kotlintest}")
    testImplementation("io.kotlintest:kotlintest-extensions-spring:${Versions.kotlintest}")
    testImplementation("org.mockito.kotlin:mockito-kotlin:${Versions.mockitoKotlin}")
}

fun DependencyHandler.springDependencies() {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    runtimeOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

fun DependencyHandler.jacksonDependencies() {
    implementation("com.fasterxml.jackson.module:jackson-modules-java8:${Versions.jackson}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${Versions.jackson}")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names:${Versions.jackson}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:${Versions.jackson}")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Versions.jackson}")
}

fun DependencyHandler.sqlDependencies() {
    implementation("org.jetbrains.exposed:exposed-core:${Versions.exposed}")
    implementation("org.jetbrains.exposed:exposed-jdbc:${Versions.exposed}")
    implementation("org.jetbrains.exposed:exposed-jodatime:${Versions.exposed}")
    implementation("org.jetbrains.exposed:spring-transaction:${Versions.exposed}")
    runtimeOnly("com.h2database:h2:${Versions.h2}")
}

fun DependencyHandler.flywayDependencies() {
    implementation("org.flywaydb:flyway-core:${Versions.flyway}")
}

fun DependencyHandler.springfoxDependencies() {
    implementation("io.springfox:springfox-boot-starter:${Versions.springfoxVersion}")
}

fun DependencyHandler.loggingDependency() {
    implementation("io.github.microutils:kotlin-logging:${Versions.kotlinLogging}")
}

fun DependencyHandler.sharedDependency() {
    implementation(project(":shared"))
}

fun DependencyHandler.dddCoreDependency() {
    implementation(project(":ddd-core"))
}
