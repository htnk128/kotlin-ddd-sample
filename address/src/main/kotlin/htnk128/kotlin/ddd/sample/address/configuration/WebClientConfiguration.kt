package htnk128.kotlin.ddd.sample.address.configuration

import io.netty.channel.ChannelOption
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.client.reactive.ReactorResourceFactory
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

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
