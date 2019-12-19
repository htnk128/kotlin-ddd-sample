package htnk128.kotlin.ddd.sample.address.domain.model.address

import htnk128.kotlin.ddd.sample.address.domain.model.account.AccountId

/**
 * 住所を操作するためのリポジトリを表現する。
 */
interface AddressRepository {

    fun find(addressId: AddressId, lock: Boolean = false): Address?

    fun findAll(accountId: AccountId): List<Address>

    fun add(address: Address)

    fun set(address: Address): Int

    fun remove(address: Address): Int

    fun nextAddressId(): AddressId = AddressId.generate()
}
