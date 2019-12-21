package htnk128.kotlin.ddd.sample.address.domain.model.account

import reactor.core.publisher.Mono

/**
 * アカウントを操作するためのリポジトリを表現する。
 */
interface AccountRepository {

    fun find(accountId: AccountId): Mono<Account>
}
