package htnk128.kotlin.spring.boot.ddd.sample.application.service

import htnk128.kotlin.spring.boot.ddd.sample.domain.model.account.Account
import htnk128.kotlin.spring.boot.ddd.sample.domain.model.account.AccountIdentity
import htnk128.kotlin.spring.boot.ddd.sample.domain.model.account.AccountRepository
import htnk128.kotlin.spring.boot.ddd.sample.domain.model.account.Name
import io.swagger.annotations.ApiModelProperty
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(private val accountRepository: AccountRepository) {

    @Transactional(readOnly = true)
    fun find(aAccountId: String): AccountResponse =
        accountRepository.find(AccountIdentity.valueOf(aAccountId))?.toAccountResponse()
            ?: throw RuntimeException("account not found.")

    @Transactional(readOnly = true)
    fun findAll(): AccountsResponse =
        AccountsResponse(accountRepository.findAll().map { it.toAccountResponse() })

    @Transactional(timeout = 10, rollbackFor = [Exception::class])
    fun create(aName: String): AccountResponse =
        Account(accountRepository.nextAccountId(), Name.valueOf(aName))
            .also(accountRepository::create)
            .toAccountResponse()

    @Transactional(timeout = 10, rollbackFor = [Exception::class])
    fun update(aAccountId: String, aName: String): AccountResponse {
        val accountId = AccountIdentity.valueOf(aAccountId)
        val name = Name.valueOf(aName)

        return accountRepository.find(accountId)
            ?.let { Account(it.accountId, name) }
            ?.also { account ->
                accountRepository.update(account)
                    .takeIf { it > 0 }
                    ?: throw RuntimeException("account update failed.")
            }
            ?.toAccountResponse()
            ?: throw RuntimeException("account not found.")
    }
}

class AccountResponse(
    @ApiModelProperty(value = "アカウントID", name = "accountId", example = "accountId01", position = 1)
    val accountId: String,
    @ApiModelProperty(value = "アカウント名", name = "name", example = "sample01", position = 2)
    val name: String
)

class AccountsResponse(
    val data: List<AccountResponse>
)

private fun Account.toAccountResponse(): AccountResponse = AccountResponse(accountId.value, name.value)
