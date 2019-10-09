package htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer

interface CustomerRepository {

    fun find(customerId: CustomerIdentity): Customer?

    fun findAll(): List<Customer>

    fun create(customer: Customer)

    fun update(customer: Customer): Int

    fun nextCustomerId(): CustomerIdentity = CustomerIdentity.generate()
}
