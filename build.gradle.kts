import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import org.jlleitschuh.gradle.ktlint.KtlintFormatTask

plugins {
    allModulePlugins()
}

allprojects {
    apply {
        allModule()
    }

    repositories {
        allModuleRepositories()
    }

    dependencies {
        allModuleDependencies()
    }

    ktlint {
        version.set(Versions.ktlintCore)
        debug.set(false)
        verbose.set(false)
        android.set(false)
        outputToConsole.set(true)
        ignoreFailures.set(false)
        reporters {
            reporter(ReporterType.PLAIN)
            reporter(ReporterType.CHECKSTYLE)

            customReporters {
                register("html") {
                    fileExtension = "html"
                    dependency = "me.cassiano:ktlint-html-reporter:0.2.3"
                }
            }
        }
    }

    tasks {
        val ktlintKotlinScriptFormat by existing(KtlintFormatTask::class)
        val ktlintFormat by existing(Task::class)

        withType<KotlinCompile> {
            dependsOn(listOf(ktlintKotlinScriptFormat, ktlintFormat))

            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = Versions.java.toString()
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
    java.sourceCompatibility = Versions.java
    java.targetCompatibility = Versions.java
}
