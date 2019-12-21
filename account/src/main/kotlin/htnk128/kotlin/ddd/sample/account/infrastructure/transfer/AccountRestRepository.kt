package htnk128.kotlin.ddd.sample.account.infrastructure.transfer

import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountId
import htnk128.kotlin.ddd.sample.account.domain.model.address.Address
import htnk128.kotlin.ddd.sample.account.domain.model.address.AddressId
import htnk128.kotlin.ddd.sample.account.domain.model.address.AddressRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class AddressRestRepository(
    private val addressClient: AddressClient
) : AddressRepository {

    override fun findAll(accountId: AccountId): Flux<Address> =
        addressClient.findAll(accountId)

    override fun remove(address: Address): Mono<Address> =
        addressClient.delete(address.addressId)
}

@Component
class AddressClient(
    @Value("\${api.address.url:http://localhost:8081/addresses}")
    private val addressUrl: String
) {

    fun findAll(accountId: AccountId): Flux<Address> =
        WebClient
            .builder()
            .build()
            .get()
            .uri("$addressUrl?accountId=$accountId")
            .retrieve()
            .bodyToMono(AddressResponses::class.java)
            .flatMapIterable { it.data }
            .map { it.responseToModel() }

    fun delete(addressId: AddressId): Mono<Address> =
        WebClient
            .builder()
            .build()
            .delete()
            .uri("$addressUrl/$addressId")
            .retrieve()
            .bodyToMono(AddressResponse::class.java)
            .map { it.responseToModel() }

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
