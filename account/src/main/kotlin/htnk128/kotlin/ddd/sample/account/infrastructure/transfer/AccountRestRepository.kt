package htnk128.kotlin.ddd.sample.account.infrastructure.transfer

import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountId
import htnk128.kotlin.ddd.sample.account.domain.model.address.Address
import htnk128.kotlin.ddd.sample.account.domain.model.address.AddressId
import htnk128.kotlin.ddd.sample.account.domain.model.address.AddressRepository
import java.net.URI
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate

@Repository
class AddressRestRepository(
    private val addressClient: AddressClient
) : AddressRepository {

    override fun findAll(accountId: AccountId): List<Address> = addressClient.findAll(accountId)

    override fun remove(address: Address): Int = addressClient.delete(address.addressId)
}

@Component
class AddressClient(
    @Value("\${api.address.url:http://localhost:8081/addresses}")
    private val addressUrl: String,
    private val restTemplate: RestTemplate
) {

    fun findAll(accountId: AccountId): List<Address> = runCatching {
        RequestEntity
            .get(URI("$addressUrl?accountId=$accountId"))
            .build()
            .run { restTemplate.exchange(this, AddressResponses::class.java) }
            .takeIf { it.statusCode.is2xxSuccessful }
            ?.body
            ?.data
            ?.map { it.responseToModel() }
            ?: error("Address response status is not OK.")
    }.getOrElse { emptyList() }

    fun delete(addressId: AddressId): Int = runCatching {
        restTemplate.exchange(
            URI("$addressUrl/$addressId"),
            HttpMethod.DELETE,
            HttpEntity.EMPTY,
            String::class.java
        )
            .takeIf { it.statusCode.is2xxSuccessful }
            ?.let { 1 }
            ?: error("Address response status is not OK.")
    }.getOrThrow()

    private data class AddressResponses(
        val data: List<AddressResponse>
    )

    private data class AddressResponse(
        val addressId: String
    ) {

        fun responseToModel(): Address =
            Address(
                AddressId.valueOf(addressId)
            )
    }
}
