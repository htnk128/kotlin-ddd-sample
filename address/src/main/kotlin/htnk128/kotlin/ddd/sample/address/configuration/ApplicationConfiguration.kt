package htnk128.kotlin.ddd.sample.address.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@Configuration
class ApplicationConfiguration(
    @Value("\${rest.connect.timeout:3000}")
    private val restConnectTimeout: Int,
    @Value("\${rest.read.timeout:5000}")
    private val restReadTimeout: Int
) {

    @Bean
    fun restTemplate(): RestTemplate = RestTemplate(
        SimpleClientHttpRequestFactory()
            .apply {
                setConnectTimeout(restConnectTimeout)
                setReadTimeout(restReadTimeout)
            }
    )
}
