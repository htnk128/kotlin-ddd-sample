plugins {
    springPlugins()
}

apply {
    spring()
}

dependencies {
    coreDependency()
    springDependencies()
    jacksonDependencies()
    sqlDependencies()
    flywayDependencies()
    swaggerDependencies()
}
