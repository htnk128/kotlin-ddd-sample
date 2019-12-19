package htnk128.kotlin.ddd.sample.account.domain.model.address

import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountId

/**
 * 住所を操作するためのリポジトリを表現する。
 */
interface AddressRepository {

    fun findAll(accountId: AccountId): List<Address>

    fun remove(address: Address): Int
}
