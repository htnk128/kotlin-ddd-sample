package htnk128.kotlin.ddd.sample.address.adapter.gateway.rest

import htnk128.kotlin.ddd.sample.address.domain.model.owner.Owner
import htnk128.kotlin.ddd.sample.address.domain.model.owner.OwnerId
import htnk128.kotlin.ddd.sample.address.domain.model.owner.OwnerNotFoundException
import htnk128.kotlin.ddd.sample.address.domain.model.owner.OwnerService
import java.time.Instant
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class OwnerRestService(
    private val accountClient: AccountClient
) : OwnerService {

    override fun find(ownerId: OwnerId): Mono<Owner> =
        accountClient.find(ownerId)
}

@Component
class AccountClient(
    @Value("\${api.account.url:http://localhost:8080/accounts}")
    private val accountUrl: String
) {

    fun find(ownerId: OwnerId): Mono<Owner> =
        WebClient
            .builder()
            .build()
            .get()
            .uri("$accountUrl/$ownerId")
            .retrieve()
            .bodyToMono(AccountResponse::class.java)
            .map { it.responseToModel() }
            .onErrorResume { throw OwnerNotFoundException(ownerId, cause = it) }

    private data class AccountResponse(
        val accountId: String,
        val deletedAt: Long?
    ) {

        fun responseToModel(): Owner =
            Owner(
                OwnerId.valueOf(accountId),
                deletedAt?.let { Instant.ofEpochMilli(it) }
            )
    }
}
