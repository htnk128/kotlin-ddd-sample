package htnk128.kotlin.spring.boot.ddd.sample.customer.application.service

import htnk128.kotlin.spring.boot.ddd.sample.customer.application.service.dto.CustomerDTO
import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer.Customer
import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer.CustomerIdentity
import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer.CustomerRepository
import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer.Name
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomerService(private val customerRepository: CustomerRepository) {

    @Transactional(readOnly = true)
    fun find(aCustomerId: String): CustomerDTO =
        customerRepository.find(CustomerIdentity.valueOf(aCustomerId))?.toDTO()
            ?: throw RuntimeException("customer not found.")

    @Transactional(readOnly = true)
    fun findAll(): List<CustomerDTO> =
        customerRepository.findAll().map { it.toDTO() }

    @Transactional(timeout = 10, rollbackFor = [Exception::class])
    fun create(aName: String): CustomerDTO =
        Customer(
            customerRepository.nextCustomerId(),
            Name.valueOf(aName)
        )
            .also(customerRepository::create)
            .toDTO()

    @Transactional(timeout = 10, rollbackFor = [Exception::class])
    fun update(aCustomerId: String, aName: String): CustomerDTO {
        val customerId = CustomerIdentity.valueOf(aCustomerId)
        val name = Name.valueOf(aName)

        return customerRepository.find(customerId)
            ?.let { Customer(it.customerId, name) }
            ?.also { customer ->
                customerRepository.update(customer)
                    .takeIf { it > 0 }
                    ?: throw RuntimeException("customer update failed.")
            }
            ?.toDTO()
            ?: throw RuntimeException("customer not found.")
    }
}

private fun Customer.toDTO(): CustomerDTO =
    CustomerDTO(
        customerId.value,
        name.value
    )
