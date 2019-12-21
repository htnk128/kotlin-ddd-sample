package htnk128.kotlin.ddd.sample.account.domain.model.address

import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountId
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * 住所を操作するためのリポジトリを表現する。
 */
interface AddressRepository {

    fun findAll(accountId: AccountId): Flux<Address>

    fun remove(address: Address): Mono<Address>
}
