package htnk128.kotlin.ddd.sample.account.adapter.rest

import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountAddress
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountAddressBook
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountAddressBookOperator
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountAddressId
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountId
import java.time.Instant
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class AccountAddressBookRestOperator(
    private val addressClient: AddressClient
) : AccountAddressBookOperator {

    override fun find(accountId: AccountId): Mono<AccountAddressBook> =
        addressClient.findAll(accountId)
            .collectList()
            .map { AccountAddressBook(it) }

    override fun remove(accountAddressId: AccountAddressId): Mono<Unit> =
        addressClient.delete(accountAddressId)
}

@Component
class AddressClient(
    @Value("\${api.address.url:http://localhost:8081/addresses}")
    private val addressUrl: String
) {

    fun findAll(accountId: AccountId): Flux<AccountAddress> =
        WebClient
            .builder()
            .build()
            .get()
            .uri("$addressUrl?addressOwnerId=$accountId")
            .retrieve()
            .bodyToMono(AddressResponses::class.java)
            .flatMapIterable { it.data }
            .map { it.responseToModel() }

    fun delete(accountAddressId: AccountAddressId): Mono<Unit> =
        WebClient
            .builder()
            .build()
            .delete()
            .uri("$addressUrl/$accountAddressId")
            .retrieve()
            .bodyToMono(AddressResponse::class.java)
            .map { }

    private data class AddressResponses(
        val data: List<AddressResponse>
    )

    private data class AddressResponse(
        val addressId: String,
        val deletedAt: Long?
    ) {

        fun responseToModel(): AccountAddress =
            AccountAddress(
                AccountAddressId.valueOf(addressId),
                deletedAt?.let { Instant.ofEpochMilli(it) }
            )
    }
}
