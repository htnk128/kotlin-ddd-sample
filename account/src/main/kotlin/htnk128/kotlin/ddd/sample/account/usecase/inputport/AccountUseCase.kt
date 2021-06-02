package htnk128.kotlin.ddd.sample.account.usecase.inputport

import htnk128.kotlin.ddd.sample.account.domain.model.account.Account
import htnk128.kotlin.ddd.sample.account.usecase.inputport.command.CreateAccountCommand
import htnk128.kotlin.ddd.sample.account.usecase.inputport.command.DeleteAccountCommand
import htnk128.kotlin.ddd.sample.account.usecase.inputport.command.FindAccountCommand
import htnk128.kotlin.ddd.sample.account.usecase.inputport.command.FindAllAccountCommand
import htnk128.kotlin.ddd.sample.account.usecase.inputport.command.UpdateAccountCommand
import reactor.core.publisher.Mono

interface AccountUseCase {

    fun find(command: FindAccountCommand): Mono<Account>

    fun findAll(command: FindAllAccountCommand): Mono<Pair<Int, List<Account>>>

    fun create(command: CreateAccountCommand): Mono<Account>

    fun update(command: UpdateAccountCommand): Mono<Account>

    fun delete(command: DeleteAccountCommand): Mono<Account>
}
