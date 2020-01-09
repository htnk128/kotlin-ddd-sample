package htnk128.kotlin.ddd.sample.address.infrastructure.service

import htnk128.kotlin.ddd.sample.address.domain.model.address.AccountId
import htnk128.kotlin.ddd.sample.address.domain.model.address.Owner
import htnk128.kotlin.ddd.sample.address.domain.model.address.OwnerNotFoundException
import htnk128.kotlin.ddd.sample.address.domain.service.OwnerDomainService
import java.time.Instant
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class AddressOwnerDomainService(
    private val accountClient: AccountClient
) : OwnerDomainService {

    override fun find(accountId: AccountId): Mono<Owner> =
        accountClient.find(accountId)
}

@Component
class AccountClient(
    @Value("\${api.account.url:http://localhost:8080/accounts}")
    private val accountUrl: String
) {

    fun find(accountId: AccountId): Mono<Owner> =
        WebClient
            .builder()
            .build()
            .get()
            .uri("$accountUrl/$accountId")
            .retrieve()
            .bodyToMono(AccountResponse::class.java)
            .map { it.responseToModel() }
            .onErrorResume { throw OwnerNotFoundException(accountId, cause = it) }

    private data class AccountResponse(
        val accountId: String,
        val deletedAt: Long?
    ) {

        fun responseToModel(): Owner =
            Owner(
                AccountId.valueOf(accountId),
                deletedAt?.let { Instant.ofEpochMilli(it) }
            )
    }
}
