package htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer

/**
 * [Customer]を操作するためのリポジトリを表現する。
 */
interface CustomerRepository {

    fun find(customerId: CustomerId): Customer?

    fun findAll(): List<Customer>

    fun add(customer: Customer)

    fun set(customer: Customer): Int

    fun remove(customer: Customer): Int

    fun nextCustomerId(): CustomerId = CustomerId.generate()
}
