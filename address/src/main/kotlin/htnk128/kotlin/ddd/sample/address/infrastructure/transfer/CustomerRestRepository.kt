package htnk128.kotlin.ddd.sample.address.infrastructure.transfer

import htnk128.kotlin.ddd.sample.address.domain.model.customer.Customer
import htnk128.kotlin.ddd.sample.address.domain.model.customer.CustomerId
import htnk128.kotlin.ddd.sample.address.domain.model.customer.CustomerRepository
import htnk128.kotlin.ddd.sample.address.domain.model.customer.Email
import htnk128.kotlin.ddd.sample.address.domain.model.customer.Name
import htnk128.kotlin.ddd.sample.address.domain.model.customer.NamePronunciation
import java.net.URI
import java.time.Instant
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate

@Repository
class CustomerRestRepository(
    private val customerClient: CustomerClient
) : CustomerRepository {

    override fun find(customerId: CustomerId): Customer? = customerClient.findCustomer(customerId)
}

@Component
class CustomerClient(
    @Value("\${api.customer.url:http://localhost:8080/customers}")
    private val customerUrl: String,
    private val restTemplate: RestTemplate
) {

    fun findCustomer(customerId: CustomerId): Customer? = runCatching {
        RequestEntity
            .get(URI("$customerUrl/$customerId"))
            .build()
            .run { restTemplate.exchange(this, CustomerResponse::class.java) }
            .takeIf { it.statusCode.is2xxSuccessful }
            ?.body
            ?.responseToModel()
            ?: error("customer response status is not OK.")
    }.getOrNull()

    private data class CustomerResponse(
        val customerId: String,
        val name: String,
        val namePronunciation: String,
        val email: String,
        val createdAt: Long,
        val deletedAt: Long?,
        val updatedAt: Long
    ) {

        fun responseToModel(): Customer =
            Customer(
                CustomerId.valueOf(customerId),
                Name.valueOf(name),
                NamePronunciation.valueOf(namePronunciation),
                Email.valueOf(email),
                Instant.ofEpochMilli(createdAt),
                deletedAt?.let { Instant.ofEpochMilli(it) },
                Instant.ofEpochMilli(updatedAt)
            )
    }
}
