package htnk128.kotlin.ddd.sample.account.application.service

import htnk128.kotlin.ddd.sample.account.application.command.CreateAccountCommand
import htnk128.kotlin.ddd.sample.account.application.command.DeleteAccountCommand
import htnk128.kotlin.ddd.sample.account.application.command.FindAccountCommand
import htnk128.kotlin.ddd.sample.account.application.command.FindAllAccountCommand
import htnk128.kotlin.ddd.sample.account.application.command.UpdateAccountCommand
import htnk128.kotlin.ddd.sample.account.application.dto.AccountDTO
import htnk128.kotlin.ddd.sample.account.domain.model.account.Account
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountEvent
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountId
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountRepository
import htnk128.kotlin.ddd.sample.account.domain.model.account.Email
import htnk128.kotlin.ddd.sample.account.domain.model.account.Name
import htnk128.kotlin.ddd.sample.account.domain.model.account.NamePronunciation
import htnk128.kotlin.ddd.sample.account.domain.model.account.Password
import htnk128.kotlin.ddd.sample.account.domain.model.addressbook.AddressBookService
import htnk128.kotlin.ddd.sample.dddcore.domain.DomainEventPublisher
import htnk128.kotlin.ddd.sample.shared.application.dto.PaginationDTO
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

/**
 * アカウント([Account])ドメインの操作を提供するアプリケーションサービス。
 */
@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val addressBookService: AddressBookService,
    private val domainEventPublisher: DomainEventPublisher<AccountEvent<*>>
) {

    @Transactional(readOnly = true)
    fun find(command: FindAccountCommand): Mono<AccountDTO> = runCatching {
        val accountId = AccountId.valueOf(command.accountId)

        Mono.just(accountRepository.find(accountId).toDTO())
            .onErrorResume { Mono.error(it.error()) }
    }
        .getOrElse { Mono.error(it.error()) }

    @Transactional(readOnly = true)
    fun findAll(command: FindAllAccountCommand): Mono<PaginationDTO<AccountDTO>> = runCatching {
        Mono.just(
            PaginationDTO(
                accountRepository.count(),
                command.limit,
                command.offset,
                accountRepository.findAll(command.limit, command.offset).map { it.toDTO() })
        )
    }
        .getOrElse { Mono.error(it.error()) }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    fun create(command: CreateAccountCommand): Mono<AccountDTO> = runCatching {
        val accountId = accountRepository.nextAccountId()
        val name = Name.valueOf(command.name)
        val namePronunciation = NamePronunciation.valueOf(command.namePronunciation)
        val email = Email.valueOf(command.email)
        val password = Password.valueOf(command.password, accountId)

        val created = Account
            .create(accountId, name, namePronunciation, email, password)
            .also { accountRepository.add(it) }

        Mono.just(created.toDTO())
            .also { created.publish() }
            .onErrorResume { Mono.error(it.error()) }
    }
        .getOrElse { Mono.error(it.error()) }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    fun update(command: UpdateAccountCommand): Mono<AccountDTO> = runCatching {
        val accountId = AccountId.valueOf(command.accountId)
        val name = command.name?.let { Name.valueOf(it) }
        val namePronunciation = command.namePronunciation?.let { NamePronunciation.valueOf(it) }
        val email = command.email?.let { Email.valueOf(it) }
        val password = command.password?.let { Password.valueOf(it, accountId) }

        val updated = accountRepository
            .find(accountId, lock = true)
            .update(name, namePronunciation, email, password)
            .also { accountRepository.set(it) }

        Mono.just(updated.toDTO())
            .also { updated.publish() }
            .onErrorResume { Mono.error(it.error()) }
    }
        .getOrElse { Mono.error(it.error()) }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    fun delete(command: DeleteAccountCommand): Mono<AccountDTO> = runCatching {
        val accountId = AccountId.valueOf(command.accountId)

        val deleted = accountRepository
            .find(accountId, lock = true)
            .delete()
            .also { accountRepository.remove(it) }

        addressBookService.find(accountId)
            .map { it.availableAccountAddresses.forEach { aa -> addressBookService.remove(aa.accountAddressId) } }
            .map { deleted.toDTO() }
            .also { deleted.publish() }
            .onErrorResume { Mono.error(it.error()) }
    }
        .getOrElse { Mono.error(it.error()) }

    private fun Account.publish() {
        occurredEvents().forEach { domainEventPublisher.publish(it) }
    }

    private companion object {

        const val TRANSACTIONAL_TIMEOUT_SECONDS: Int = 10
    }
}
