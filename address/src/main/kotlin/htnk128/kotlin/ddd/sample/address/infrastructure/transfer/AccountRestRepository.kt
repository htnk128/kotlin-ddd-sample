package htnk128.kotlin.ddd.sample.address.infrastructure.transfer

import htnk128.kotlin.ddd.sample.address.domain.model.account.Account
import htnk128.kotlin.ddd.sample.address.domain.model.account.AccountId
import htnk128.kotlin.ddd.sample.address.domain.model.account.AccountNotFoundException
import htnk128.kotlin.ddd.sample.address.domain.model.account.AccountRepository
import java.time.Instant
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Repository
class AccountRestRepository(
    private val accountClient: AccountClient
) : AccountRepository {

    override fun find(accountId: AccountId): Mono<Account> =
        accountClient.find(accountId)
}

@Component
class AccountClient(
    @Value("\${api.account.url:http://localhost:8080/accounts}")
    private val accountUrl: String
) {

    fun find(accountId: AccountId): Mono<Account> =
        WebClient
            .builder()
            .build()
            .get()
            .uri("$accountUrl/$accountId")
            .retrieve()
            .bodyToMono(AccountResponse::class.java)
            .map { it.responseToModel() }
            .onErrorResume { throw AccountNotFoundException(accountId, cause = it) }

    private data class AccountResponse(
        val accountId: String,
        val deletedAt: Long?
    ) {

        fun responseToModel(): Account =
            Account(
                AccountId.valueOf(accountId),
                deletedAt?.let { Instant.ofEpochMilli(it) }
            )
    }
}
