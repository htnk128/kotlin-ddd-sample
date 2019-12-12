package htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.customer

/**
 * [Customer]を操作するためのリポジトリを表現する。
 */
interface CustomerRepository {

    fun find(customerId: CustomerId): Customer?
}
