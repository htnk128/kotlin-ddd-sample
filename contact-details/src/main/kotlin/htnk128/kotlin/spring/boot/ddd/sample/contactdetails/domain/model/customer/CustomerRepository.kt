package htnk128.kotlin.spring.boot.ddd.sample.contactdetails.domain.model.customer

interface CustomerRepository {

    fun find(customerId: CustomerIdentity): Customer?
}
