package htnk128.kotlin.ddd.sample.account.domain.model.addressbook

import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountId
import reactor.core.publisher.Mono

/**
 * アカウントの住所録を操作するドメインサービス。
 */
interface AddressBookService {

    fun find(accountId: AccountId): Mono<AddressBook>

    fun remove(accountAddressId: AccountAddressId): Mono<Unit>
}
