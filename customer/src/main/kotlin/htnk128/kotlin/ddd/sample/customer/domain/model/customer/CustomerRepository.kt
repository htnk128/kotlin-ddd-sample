package htnk128.kotlin.ddd.sample.customer.domain.model.customer

/**
 * 顧客を操作するためのリポジトリを表現する。
 */
interface CustomerRepository {

    fun find(customerId: CustomerId, lock: Boolean = false): Customer?

    fun findAll(limit: Int, offset: Int): List<Customer>

    fun count(): Int

    fun add(customer: Customer)

    fun set(customer: Customer): Int

    fun remove(customer: Customer): Int

    fun nextCustomerId(): CustomerId = CustomerId.generate()
}
