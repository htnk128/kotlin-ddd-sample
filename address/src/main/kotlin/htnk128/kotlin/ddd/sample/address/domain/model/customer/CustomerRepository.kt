package htnk128.kotlin.ddd.sample.address.domain.model.customer

/**
 * 顧客を操作するためのリポジトリを表現する。
 */
interface CustomerRepository {

    fun find(customerId: CustomerId): Customer?
}