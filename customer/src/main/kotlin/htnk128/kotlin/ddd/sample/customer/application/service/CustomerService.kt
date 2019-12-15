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
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * 顧客([Customer])ドメインの操作を提供するアプリケーションサービス。
 */
@Service
class CustomerService(private val customerRepository: CustomerRepository) {

    @Transactional(readOnly = true)
    fun find(aCustomerId: String): Mono<CustomerDTO> {
        val customerId = CustomerId.valueOf(aCustomerId)

        return Mono.just(
            customerRepository.find(customerId)
                ?.takeUnless { it.isDeleted }
                ?.toDTO()
                ?: throw CustomerNotFoundException(customerId)
        )
    }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    fun lock(customerId: CustomerId): Mono<Customer> =
        Mono.just(
            customerRepository.find(customerId, lock = true)
                ?.takeUnless { it.isDeleted }
                ?: throw CustomerNotFoundException(customerId)
        )

    @Transactional(readOnly = true)
    fun findAll(): Flux<CustomerDTO> =
        Flux.fromIterable(
            customerRepository.findAll()
                .asSequence()
                .filterNot { it.isDeleted }
                .map { it.toDTO() }
                .toList()
        )

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    fun create(aName: String, aNamePronunciation: String, aEmail: String): Mono<CustomerDTO> =
        Mono.just(
            Customer
                .create(
                    customerRepository.nextCustomerId(),
                    Name.valueOf(aName),
                    NamePronunciation.valueOf(aNamePronunciation),
                    Email.valueOf(aEmail)
                )
                .also(customerRepository::add)
                .toDTO()
        )

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    fun update(aCustomerId: String, aName: String?, aNamePronunciation: String?, aEmail: String?): Mono<CustomerDTO> {
        val customerId = CustomerId.valueOf(aCustomerId)
        val name = aName?.let { Name.valueOf(it) }
        val namePronunciation = aNamePronunciation?.let { NamePronunciation.valueOf(it) }
        val email = aEmail?.let { Email.valueOf(it) }

        return lock(customerId)
            .map { customer ->
                customer.update(name, namePronunciation, email)
                    .also { updated ->
                        customerRepository.set(updated)
                            .takeIf { it > 0 }
                            ?: throw UnexpectedException("Customer update failed.")
                    }
                    .toDTO()
            }
    }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    fun delete(aCustomerId: String): Mono<CustomerDTO> {
        val customerId = CustomerId.valueOf(aCustomerId)

        return lock(customerId)
            .map { customer ->
                customer.delete()
                    .also { deleted ->
                        customerRepository.remove(deleted)
                            .takeIf { it > 0 }
                            ?: throw UnexpectedException("Customer update failed.")
                    }
                    .toDTO()
            }
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

        const val TRANSACTIONAL_TIMEOUT_SECONDS: Int = 10
    }
}
