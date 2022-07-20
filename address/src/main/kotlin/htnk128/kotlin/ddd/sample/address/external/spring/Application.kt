package htnk128.kotlin.ddd.sample.address.external.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["htnk128.kotlin.ddd.sample.address"])
@ConfigurationPropertiesScan(basePackages = ["htnk128.kotlin.ddd.sample.address"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
