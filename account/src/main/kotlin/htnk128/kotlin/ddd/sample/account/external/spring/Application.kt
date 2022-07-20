package htnk128.kotlin.ddd.sample.account.external.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["htnk128.kotlin.ddd.sample.account"])
@ConfigurationPropertiesScan(basePackages = ["htnk128.kotlin.ddd.sample.account"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
