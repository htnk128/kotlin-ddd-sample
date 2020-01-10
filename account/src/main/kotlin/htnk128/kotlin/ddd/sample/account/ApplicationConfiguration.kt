package htnk128.kotlin.ddd.sample.account

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.netty.channel.ChannelOption
import javax.sql.DataSource
import org.jetbrains.exposed.spring.SpringTransactionManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.client.reactive.ReactorResourceFactory
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.annotation.TransactionManagementConfigurer
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux

@Configuration
@EnableTransactionManagement
class ExposedConfiguration(val dataSource: DataSource) : TransactionManagementConfigurer {

    @Bean
    override fun annotationDrivenTransactionManager(): PlatformTransactionManager =
        SpringTransactionManager(dataSource)

    @Bean
    fun persistenceExceptionTranslationPostProcessor(): PersistenceExceptionTranslationPostProcessor =
        PersistenceExceptionTranslationPostProcessor()
}

@Configuration
class JacksonConfiguration {

    @Bean
    fun objectMapper(): ObjectMapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
}

@EnableSwagger2WebFlux
@Configuration
class SwaggerConfiguration {

    @Bean
    fun customDocket(): Docket = Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("htnk128.kotlin.ddd.sample.account.adapter.http"))
        .build()
        .useDefaultResponseMessages(false)
        .apiInfo(apiInfo())

    private fun apiInfo(): ApiInfo = ApiInfoBuilder()
        .title("Account APIs")
        .description("API specifications for account")
        .contact(Contact("htnk128", "https://github.com/htnk128", "hiroaki.tanaka128@gmail.com"))
        .version("1.0.0")
        .build()
}

@Configuration
class WebClientConfiguration {

    @Bean
    fun reactorResourceFactory() = ReactorResourceFactory()
        .apply {
            isUseGlobalResources = false
        }

    @Bean
    fun webClient(): WebClient {
        val mapper: (HttpClient) -> HttpClient = { hc ->
            hc.tcpConfiguration { tc ->
                tc.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TCP_CONNECT_TIMEOUT_MILLIS)
            }
        }
        val connector = ReactorClientHttpConnector(reactorResourceFactory(), mapper)
        return WebClient.builder().clientConnector(connector).build()
    }

    private companion object {

        const val TCP_CONNECT_TIMEOUT_MILLIS = 10000
    }
}
