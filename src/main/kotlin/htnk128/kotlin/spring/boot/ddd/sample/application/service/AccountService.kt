package htnk128.kotlin.spring.boot.ddd.sample.application.service

import htnk128.kotlin.spring.boot.ddd.sample.application.service.dto.AccountDTO
import htnk128.kotlin.spring.boot.ddd.sample.domain.model.account.Account
import htnk128.kotlin.spring.boot.ddd.sample.domain.model.account.AccountIdentity
import htnk128.kotlin.spring.boot.ddd.sample.domain.model.account.AccountRepository
import htnk128.kotlin.spring.boot.ddd.sample.domain.model.account.Name
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(private val accountRepository: AccountRepository) {

    @Transactional(readOnly = true)
    fun find(aAccountId: String): AccountDTO =
        accountRepository.find(AccountIdentity.valueOf(aAccountId))?.toDTO()
            ?: throw RuntimeException("account not found.")

    @Transactional(readOnly = true)
    fun findAll(): List<AccountDTO> =
        accountRepository.findAll().map { it.toDTO() }

    @Transactional(timeout = 10, rollbackFor = [Exception::class])
    fun create(aName: String): AccountDTO =
        Account(accountRepository.nextAccountId(), Name.valueOf(aName))
            .also(accountRepository::create)
            .toDTO()

    @Transactional(timeout = 10, rollbackFor = [Exception::class])
    fun update(aAccountId: String, aName: String): AccountDTO {
        val accountId = AccountIdentity.valueOf(aAccountId)
        val name = Name.valueOf(aName)

        return accountRepository.find(accountId)
            ?.let { Account(it.accountId, name) }
            ?.also { account ->
                accountRepository.update(account)
                    .takeIf { it > 0 }
                    ?: throw RuntimeException("account update failed.")
            }
            ?.toDTO()
            ?: throw RuntimeException("account not found.")
    }
}

private fun Account.toDTO(): AccountDTO = AccountDTO(accountId.value, name.value)
