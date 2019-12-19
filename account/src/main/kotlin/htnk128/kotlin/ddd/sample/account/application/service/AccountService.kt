package htnk128.kotlin.ddd.sample.account.application.service

import htnk128.kotlin.ddd.sample.account.application.command.CreateAccountCommand
import htnk128.kotlin.ddd.sample.account.application.command.DeleteAccountCommand
import htnk128.kotlin.ddd.sample.account.application.command.FindAccountCommand
import htnk128.kotlin.ddd.sample.account.application.command.FindAllAccountCommand
import htnk128.kotlin.ddd.sample.account.application.command.UpdateAccountCommand
import htnk128.kotlin.ddd.sample.account.application.dto.AccountDTO
import htnk128.kotlin.ddd.sample.account.application.dto.PaginationAccountDTO
import htnk128.kotlin.ddd.sample.account.domain.model.account.Account
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountId
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountInvalidDataStateException
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountInvalidRequestException
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountNotFoundException
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountRepository
import htnk128.kotlin.ddd.sample.account.domain.model.account.Email
import htnk128.kotlin.ddd.sample.account.domain.model.account.Name
import htnk128.kotlin.ddd.sample.account.domain.model.account.NamePronunciation
import htnk128.kotlin.ddd.sample.account.domain.model.account.Password
import htnk128.kotlin.ddd.sample.account.domain.model.address.AddressInvalidRequestException
import htnk128.kotlin.ddd.sample.account.domain.model.address.AddressRepository
import htnk128.kotlin.ddd.sample.shared.applicatio.exception.ExpectedException
import htnk128.kotlin.ddd.sample.shared.applicatio.exception.UnexpectedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.stream.Collectors

/**
 * アカウント([Account])ドメインの操作を提供するアプリケーションサービス。
 */
@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val addressRepository: AddressRepository
) {

    @Transactional(readOnly = true)
    fun find(command: FindAccountCommand): Mono<AccountDTO> = runHandling {
        Mono.just(accountRepository.find(AccountId.valueOf(command.accountId)).toDTO())
    }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    fun lock(accountId: AccountId): Mono<Account> = runHandling {
        Mono.just(accountRepository.find(accountId, lock = true))
    }

    @Transactional(readOnly = true)
    fun findAll(command: FindAllAccountCommand): Mono<PaginationAccountDTO> = runHandling {
        Flux.fromIterable(
            accountRepository.findAll(command.limit, command.offset)
                .map { it.toDTO() }
        )
            .collect(Collectors.toList())
            .map { PaginationAccountDTO(accountRepository.count(), command.limit, command.offset, it) }
    }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    fun create(command: CreateAccountCommand): Mono<AccountDTO> = runHandling {
        val accountId = accountRepository.nextAccountId()

        Mono.just(
            Account
                .create(
                    accountId,
                    Name.valueOf(command.name),
                    NamePronunciation.valueOf(command.namePronunciation),
                    Email.valueOf(command.email),
                    Password.valueOf(command.password, accountId)
                )
                .also(accountRepository::add)
                .toDTO()
        )
    }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    fun update(command: UpdateAccountCommand): Mono<AccountDTO> = runHandling {
        val accountId = AccountId.valueOf(command.accountId)
        val name = command.name?.let { Name.valueOf(it) }
        val namePronunciation = command.namePronunciation?.let { NamePronunciation.valueOf(it) }
        val email = command.email?.let { Email.valueOf(it) }
        val password = command.password?.let { Password.valueOf(it, accountId) }

        lock(accountId)
            .map { account ->
                account.update(name, namePronunciation, email, password)
                    .also { updated ->
                        accountRepository.set(updated)
                            .takeIf { it > 0 }
                            ?: throw UnexpectedException("Account update failed.")
                    }
                    .toDTO()
            }
    }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    fun delete(command: DeleteAccountCommand): Mono<AccountDTO> = runHandling {
        val accountId = AccountId.valueOf(command.accountId)

        lock(accountId)
            .map { account ->
                addressRepository.findAll(accountId)
                    .forEach { addressRepository.remove(it) }

                account.delete()
                    .also { deleted ->
                        accountRepository.remove(deleted)
                            .takeIf { it > 0 }
                            ?: throw UnexpectedException("Account update failed.")
                    }
                    .toDTO()
            }
    }

    private fun <T> runHandling(block: () -> T): T = runCatching {
        block()
    }.getOrElse {
        throw when (it) {
            is AccountInvalidRequestException -> ExpectedException(it.type, 400, it.message, it)
            is AddressInvalidRequestException -> ExpectedException(it.type, 400, it.message, it)
            is AccountNotFoundException -> ExpectedException(it.type, 404, it.message, it)
            is AccountInvalidDataStateException -> ExpectedException(it.type, 409, it.message, it)
            else -> UnexpectedException(it.message, it)
        }
    }

    private fun Account.toDTO(): AccountDTO =
        AccountDTO(
            accountId.id(),
            name.toValue(),
            namePronunciation.toValue(),
            email.toValue(),
            password.format(),
            createdAt.toEpochMilli(),
            deletedAt?.toEpochMilli(),
            updatedAt.toEpochMilli()
        )

    private companion object {

        const val TRANSACTIONAL_TIMEOUT_SECONDS: Int = 10
    }
}
