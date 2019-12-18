package htnk128.kotlin.ddd.sample.customer.domain.model.address

import htnk128.kotlin.ddd.sample.customer.domain.model.customer.CustomerId

/**
 * 住所を操作するためのリポジトリを表現する。
 */
interface AddressRepository {

    fun findAll(customerId: CustomerId): List<Address>

    fun remove(address: Address): Int
}
