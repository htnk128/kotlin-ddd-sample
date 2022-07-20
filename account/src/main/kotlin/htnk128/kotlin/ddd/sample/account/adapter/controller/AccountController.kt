package htnk128.kotlin.ddd.sample.account.adapter.controller

import htnk128.kotlin.ddd.sample.account.adapter.controller.resource.AccountResponse
import htnk128.kotlin.ddd.sample.account.adapter.controller.resource.AccountResponses
import htnk128.kotlin.ddd.sample.account.usecase.inputport.AccountUseCase
import htnk128.kotlin.ddd.sample.account.usecase.inputport.command.CreateAccountCommand
import htnk128.kotlin.ddd.sample.account.usecase.inputport.command.DeleteAccountCommand
import htnk128.kotlin.ddd.sample.account.usecase.inputport.command.FindAccountCommand
import htnk128.kotlin.ddd.sample.account.usecase.inputport.command.FindAllAccountCommand
import htnk128.kotlin.ddd.sample.account.usecase.inputport.command.UpdateAccountCommand
import htnk128.kotlin.ddd.sample.account.usecase.outputport.AccountPresenter
import htnk128.kotlin.ddd.sample.account.usecase.outputport.dto.AccountDTO
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AccountController(
    private val accountUseCase: AccountUseCase,
    private val accountPresenter: AccountPresenter
) {

    fun find(
        accountId: String
    ): Mono<AccountResponse> {
        val command = FindAccountCommand(accountId)
        return accountUseCase.find(command)
            .map { accountPresenter.toDTO(it) }
            .map { it.toResponse() }
    }

    fun findAll(
        limit: Int,
        offset: Int
    ): Mono<AccountResponses> {
        val command = FindAllAccountCommand(limit, offset)

        return accountUseCase.findAll(command)
            .map { (count, accounts) ->
                accountPresenter.toDTO(accounts, count, command.limit, command.offset)
            }
            .map { dto ->
                AccountResponses(
                    dto.count,
                    dto.hasMore,
                    dto.data.map { it.toResponse() }
                )
            }
    }

    fun create(
        name: String,
        namePronunciation: String,
        email: String,
        password: String
    ): Mono<AccountResponse> {
        val command = CreateAccountCommand(
            name,
            namePronunciation,
            email,
            password
        )

        return accountUseCase.create(command)
            .map { accountPresenter.toDTO(it) }
            .map { it.toResponse() }
    }

    fun update(
        accountId: String,
        name: String?,
        namePronunciation: String?,
        email: String?,
        password: String?
    ): Mono<AccountResponse> {
        val command = UpdateAccountCommand(
            accountId,
            name,
            namePronunciation,
            email,
            password
        )

        return accountUseCase.update(command)
            .map { accountPresenter.toDTO(it) }
            .map { it.toResponse() }
    }

    fun delete(
        accountId: String
    ): Mono<AccountResponse> {
        val command = DeleteAccountCommand(accountId)

        return accountUseCase.delete(command)
            .map { accountPresenter.toDTO(it) }
            .map { it.toResponse() }
    }

    private fun AccountDTO.toResponse() =
        AccountResponse.from(this)
}
