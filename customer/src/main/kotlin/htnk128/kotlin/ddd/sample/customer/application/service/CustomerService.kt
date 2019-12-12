package htnk128.kotlin.ddd.sample.customer.application.service

import htnk128.kotlin.ddd.sample.customer.application.dto.CustomerDTO
import htnk128.kotlin.ddd.sample.customer.application.exception.CustomerNotFoundException
import htnk128.kotlin.ddd.sample.customer.domain.model.customer.Customer
import htnk128.kotlin.ddd.sample.customer.domain.model.customer.CustomerId
import htnk128.kotlin.ddd.sample.customer.domain.model.customer.CustomerRepository
import htnk128.kotlin.ddd.sample.customer.domain.model.customer.Email
import htnk128.kotlin.ddd.sample.customer.domain.model.customer.Name
import htnk128.kotlin.ddd.sample.customer.domain.model.customer.NamePronunciation
import htnk128.kotlin.ddd.sample.shared.UnexpectedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 顧客([Customer])ドメインの操作を提供するアプリケーションサービス。
 */
@Service
class CustomerService(private val customerRepository: CustomerRepository) {

    @Transactional(readOnly = true)
    fun find(aCustomerId: String): CustomerDTO {
        val customerId = CustomerId.valueOf(aCustomerId)

        return customerRepository.find(customerId)
            ?.takeUnless { it.isDeleted }
            ?.toDTO()
            ?: throw CustomerNotFoundException(
                customerId
            )
    }

    @Transactional(readOnly = true)
    fun findAll(): List<CustomerDTO> = customerRepository.findAll()
        .asSequence()
        .filterNot { it.isDeleted }
        .map { it.toDTO() }
        .toList()

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT, rollbackFor = [Exception::class])
    fun create(aName: String, aNamePronunciation: String, aEmail: String): CustomerDTO = Customer
        .create(
            customerRepository.nextCustomerId(),
            Name.valueOf(aName),
            NamePronunciation.valueOf(aNamePronunciation),
            Email.valueOf(aEmail)
        )
        .also(customerRepository::add)
        .toDTO()

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT, rollbackFor = [Exception::class])
    fun update(aCustomerId: String, aName: String?, aNamePronunciation: String?, aEmail: String?): CustomerDTO {
        val customerId = CustomerId.valueOf(aCustomerId)
        val name = aName?.let { Name.valueOf(it) }
        val namePronunciation = aNamePronunciation?.let { NamePronunciation.valueOf(it) }
        val email = aEmail?.let { Email.valueOf(it) }

        return customerRepository
            .find(customerId)
            ?.update(name, namePronunciation, email)
            ?.also { customer ->
                customerRepository.set(customer)
                    .takeIf { it > 0 }
                    ?: throw UnexpectedException("Customer update failed.")
            }
            ?.toDTO()
            ?: throw CustomerNotFoundException(
                customerId
            )
    }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT, rollbackFor = [Exception::class])
    fun delete(aCustomerId: String) {
        val customerId = CustomerId.valueOf(aCustomerId)

        customerRepository
            .find(customerId)
            ?.delete()
            ?.also { customer ->
                customerRepository.remove(customer)
                    .takeIf { it > 0 }
                    ?: throw UnexpectedException("Customer update failed.")
            }
            ?: throw CustomerNotFoundException(
                customerId
            )
    }

    private fun Customer.toDTO(): CustomerDTO =
        CustomerDTO(
            customerId.value,
            name.value,
            namePronunciation.value,
            email.value,
            createdAt.toEpochMilli(),
            deletedAt?.toEpochMilli(),
            updatedAt.toEpochMilli()
        )

    private companion object {
        const val TRANSACTIONAL_TIMEOUT: Int = 30
    }
}
