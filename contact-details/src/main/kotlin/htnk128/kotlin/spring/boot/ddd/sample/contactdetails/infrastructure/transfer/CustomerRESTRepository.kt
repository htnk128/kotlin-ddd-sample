package htnk128.kotlin.spring.boot.ddd.sample.contactdetails.infrastructure.transfer

import htnk128.kotlin.spring.boot.ddd.sample.contactdetails.domain.model.customer.Customer
import htnk128.kotlin.spring.boot.ddd.sample.contactdetails.domain.model.customer.CustomerIdentity
import htnk128.kotlin.spring.boot.ddd.sample.contactdetails.domain.model.customer.CustomerRepository
import htnk128.kotlin.spring.boot.ddd.sample.contactdetails.domain.model.customer.Name
import java.net.URI
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate

@Repository
class CustomerRESTRepository(
    private val customerClient: CustomerClient
) : CustomerRepository {

    override fun find(customerId: CustomerIdentity): Customer? =
        customerClient.findCustomer(customerId)
}

@Component
class CustomerClient(
    @Value("\${api.customer.url:http://localhost:8080/customers}")
    private val customerUrl: String,
    private val restTemplate: RestTemplate
) {

    fun findCustomer(customerId: CustomerIdentity): Customer? = runCatching {
        RequestEntity
            .get(URI("$customerUrl/$customerId"))
            .build()
            .run { restTemplate.exchange(this, CustomerResponse::class.java) }
            .takeIf { it.statusCode.is2xxSuccessful }
            ?.body
            ?.responseToModel()
            ?: error("customer response status is not OK.")
    }.getOrElse {
        throw RuntimeException("customer find request is failed. (customerId:$customerId)", it)
    }
}

private data class CustomerResponse(
    val customerId: String,
    val name: String
) {

    fun responseToModel(): Customer =
        Customer(
            CustomerIdentity.valueOf(customerId),
            Name.valueOf(customerId)
        )
}
