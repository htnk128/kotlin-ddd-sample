package htnk128.kotlin.ddd.sample.account.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux

@EnableSwagger2WebFlux
@Configuration
class SwaggerConfiguration {

    @Bean
    fun customDocket(): Docket = Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("htnk128.kotlin.ddd.sample.account.presentation.controller"))
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
