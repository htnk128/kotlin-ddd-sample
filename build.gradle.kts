import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    kotlin("jvm") version embeddedKotlinVersion
    id("org.jlleitschuh.gradle.ktlint") version "8.0.0"
    id("org.springframework.boot") version "2.0.6.RELEASE"
    kotlin("plugin.spring") version embeddedKotlinVersion
    id("org.flywaydb.flyway") version "5.2.4"
}

apply {
    plugin("kotlin")
    plugin("idea")
    plugin("org.jlleitschuh.gradle.ktlint")
    plugin("io.spring.dependency-management")
    plugin("org.flywaydb.flyway")
}

repositories {
    mavenCentral()
    maven("https://dl.bintray.com/kotlin/exposed")
    maven("http://oss.jfrog.org/artifactory/oss-snapshot-local/")
}

dependencies {
    // Kotlin
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))
    // spring
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    runtimeOnly("org.springframework.boot:spring-boot-devtools")
    // jackson json
    implementation("com.fasterxml.jackson.module:jackson-modules-java8:2.9.9")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.9")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names:2.9.9")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.9.9")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.9.9")
    // flyway
    implementation("org.flywaydb:flyway-core:5.2.4")
    // Exposed
    implementation("org.jetbrains.exposed:exposed:0.13.6")
    implementation("org.jetbrains.exposed:spring-transaction:0.13.6")
    // Spring Fox/Swagger
    implementation("io.springfox:springfox-swagger2:3.0.0-SNAPSHOT")
    implementation("io.springfox:springfox-spring-webflux:3.0.0-SNAPSHOT")
    implementation("io.springfox:springfox-swagger-ui:3.0.0-SNAPSHOT")
    runtimeOnly("com.h2database:h2")
    // Test
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

ktlint {
    version.set("0.32.0")
    debug.set(false)
    verbose.set(false)
    android.set(false)
    outputToConsole.set(true)
    reporters.set(setOf(ReporterType.PLAIN, ReporterType.CHECKSTYLE))
    ignoreFailures.set(false)
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }

    withType<Test> {
        testLogging {
            events("skipped", "failed")
            setExceptionFormat("full")
        }
        useJUnitPlatform {
            includeEngines("junit-vintage", "junit-jupiter")
        }
    }
}

group = "htnk128"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8
