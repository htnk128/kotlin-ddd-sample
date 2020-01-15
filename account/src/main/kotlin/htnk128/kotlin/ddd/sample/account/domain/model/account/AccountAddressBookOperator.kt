package htnk128.kotlin.ddd.sample.account.domain.model.account

import reactor.core.publisher.Mono

/**
 * アカウントの住所録を操作するドメインサービス。
 */
interface AccountAddressBookOperator {

    fun find(accountId: AccountId): Mono<AccountAddressBook>

    fun remove(accountAddressId: AccountAddressId): Mono<Unit>
}
