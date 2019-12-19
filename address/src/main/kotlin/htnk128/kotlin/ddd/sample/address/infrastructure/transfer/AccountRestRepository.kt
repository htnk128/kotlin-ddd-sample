package htnk128.kotlin.ddd.sample.address.infrastructure.transfer

import htnk128.kotlin.ddd.sample.address.domain.model.account.Account
import htnk128.kotlin.ddd.sample.address.domain.model.account.AccountId
import htnk128.kotlin.ddd.sample.address.domain.model.account.AccountNotFoundException
import htnk128.kotlin.ddd.sample.address.domain.model.account.AccountRepository
import java.net.URI
import java.time.Instant
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import org.springframework.web.client.RestTemplate

@Repository
class AccountRestRepository(
    private val accountClient: AccountClient
) : AccountRepository {

    override fun find(accountId: AccountId): Account =
        accountClient.find(accountId)
            ?: throw AccountNotFoundException(accountId)
}

@Component
class AccountClient(
    @Value("\${api.account.url:http://localhost:8080/accounts}")
    private val accountUrl: String,
    private val restTemplate: RestTemplate
) {

    fun find(accountId: AccountId): Account? = runCatching {
        RequestEntity
            .get(URI("$accountUrl/$accountId"))
            .build()
            .run { restTemplate.exchange(this, AccountResponse::class.java) }
            .takeIf { it.statusCode.is2xxSuccessful }
            ?.body
            ?.responseToModel()
            ?: error("Account response status is not OK.")
    }.getOrNull()

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
