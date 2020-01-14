package htnk128.kotlin.ddd.sample.account.domain.service.account

import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountAddress
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountAddressId
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountId
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * アカウントの住所を操作するドメインサービス。
 */
interface AccountAddressOperator {

    fun findAll(accountId: AccountId): Flux<AccountAddress>

    fun remove(accountAddressId: AccountAddressId): Mono<Unit>
}
