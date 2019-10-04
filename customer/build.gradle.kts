plugins {
    springPlugins()
    flywayPlugins()
}

apply {
    spring()
    flyway()
}

dependencies {
    springDependencies()
    jacksonDependencies()
    sqlDependencies()
    flywayDependencies()
    swaggerDependencies()
}
