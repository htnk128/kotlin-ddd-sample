package htnk128.kotlin.ddd.sample.address.adapter.rest

import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressOwner
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressOwnerId
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressOwnerNotFoundException
import htnk128.kotlin.ddd.sample.address.domain.service.address.AddressOwnerOperator
import java.time.Instant
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class AddressOwnerRestOperator(
    private val accountClient: AccountClient
) : AddressOwnerOperator {

    override fun find(addressOwnerId: AddressOwnerId): Mono<AddressOwner> =
        accountClient.find(addressOwnerId)
}

@Component
class AccountClient(
    @Value("\${api.account.url:http://localhost:8080/accounts}")
    private val accountUrl: String
) {

    fun find(addressOwnerId: AddressOwnerId): Mono<AddressOwner> =
        WebClient
            .builder()
            .build()
            .get()
            .uri("$accountUrl/$addressOwnerId")
            .retrieve()
            .bodyToMono(AccountResponse::class.java)
            .map { it.responseToModel() }
            .onErrorResume { throw AddressOwnerNotFoundException(addressOwnerId, cause = it) }

    private data class AccountResponse(
        val accountId: String,
        val deletedAt: Long?
    ) {

        fun responseToModel(): AddressOwner =
            AddressOwner(
                AddressOwnerId.valueOf(accountId),
                deletedAt?.let { Instant.ofEpochMilli(it) }
            )
    }
}
