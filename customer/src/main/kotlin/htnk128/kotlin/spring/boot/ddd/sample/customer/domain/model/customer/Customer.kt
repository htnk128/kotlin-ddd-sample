package htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer

import htnk128.kotlin.spring.boot.ddd.sample.dddcore.domain.Entity

class Customer(
    val customerId: CustomerIdentity,
    val name: Name,
    val events: List<CustomerEvent<*>> = emptyList()
) : Entity<Customer> {

    fun update(name: Name): Customer =
        Customer(customerId, name, events + CustomerUpdated())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Customer
        return sameIdentityAs(other)
    }

    override fun hashCode(): Int = customerId.hashCode()

    override fun sameIdentityAs(other: Customer): Boolean = customerId == other.customerId

    companion object {

        fun create(customerId: CustomerIdentity, name: Name): Customer =
            Customer(customerId, name, listOf(CustomerCreated()))
    }
}
