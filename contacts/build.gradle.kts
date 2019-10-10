plugins {
    springPlugins()
    flywayPlugins()
}

apply {
    spring()
    flyway()
}

dependencies {
    coreDependency()
    springDependencies()
    jacksonDependencies()
    sqlDependencies()
    flywayDependencies()
    swaggerDependencies()
}
