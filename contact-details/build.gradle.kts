plugins {
    springPlugins()
}

apply {
    spring()
}

dependencies {
    dddCoreDependency()
    springDependencies()
    jacksonDependencies()
    sqlDependencies()
    flywayDependencies()
    swaggerDependencies()
}
