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

kover {
    verify {
        rule {
            name = "Minimal line coverage rate in percents"
            bound {
                minValue = 26 // TODO 80%くらいにはしたい
            }
        }
    }
}
