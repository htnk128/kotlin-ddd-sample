package htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.customer

interface CustomerRepository {

    fun find(customerId: CustomerIdentity): Customer?
}
