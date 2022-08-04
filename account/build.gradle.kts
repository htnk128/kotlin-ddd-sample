plugins {
    springPlugins()
}

apply {
    spring()
}

dependencies {
    sharedDependency()
    dddCoreDependency()
    springDependencies()
    jacksonDependencies()
    sqlDependencies()
    flywayDependencies()
    springfoxDependencies()
    loggingDependency()
}
