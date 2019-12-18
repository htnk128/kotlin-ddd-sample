package htnk128.kotlin.ddd.sample.customer.application.service

import htnk128.kotlin.ddd.sample.customer.application.command.CreateCustomerCommand
import htnk128.kotlin.ddd.sample.customer.application.command.DeleteCustomerCommand
import htnk128.kotlin.ddd.sample.customer.application.command.FindAllCustomerCommand
import htnk128.kotlin.ddd.sample.customer.application.command.FindCustomerCommand
import htnk128.kotlin.ddd.sample.customer.application.command.UpdateCustomerCommand
import htnk128.kotlin.ddd.sample.customer.application.dto.CustomerDTO
import htnk128.kotlin.ddd.sample.customer.application.dto.PaginationCustomerDTO
import htnk128.kotlin.ddd.sample.customer.application.exception.CustomerNotFoundException
import htnk128.kotlin.ddd.sample.customer.domain.model.address.AddressRepository
import htnk128.kotlin.ddd.sample.customer.domain.model.customer.Customer
import htnk128.kotlin.ddd.sample.customer.domain.model.customer.CustomerId
import htnk128.kotlin.ddd.sample.customer.domain.model.customer.CustomerRepository
import htnk128.kotlin.ddd.sample.customer.domain.model.customer.Email
import htnk128.kotlin.ddd.sample.customer.domain.model.customer.Name
import htnk128.kotlin.ddd.sample.customer.domain.model.customer.NamePronunciation
import htnk128.kotlin.ddd.sample.shared.UnexpectedException
import java.util.stream.Collectors
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * 顧客([Customer])ドメインの操作を提供するアプリケーションサービス。
 */
@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val addressRepository: AddressRepository
) {

    @Transactional(readOnly = true)
    fun find(command: FindCustomerCommand): Mono<CustomerDTO> {
        val customerId = CustomerId.valueOf(command.customerId)

        return Mono.just(
            customerRepository.find(customerId)
                ?.toDTO()
                ?: throw CustomerNotFoundException(customerId)
        )
    }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    fun lock(customerId: CustomerId): Mono<Customer> =
        Mono.just(
            customerRepository.find(customerId, lock = true)
                ?: throw CustomerNotFoundException(customerId)
        )

    @Transactional(readOnly = true)
    fun findAll(command: FindAllCustomerCommand): Mono<PaginationCustomerDTO> =
        Flux.fromIterable(
            customerRepository.findAll(command.limit, command.offset)
                .map { it.toDTO() }
        )
            .collect(Collectors.toList())
            .map { PaginationCustomerDTO(customerRepository.count(), command.limit, command.offset, it) }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    fun create(command: CreateCustomerCommand): Mono<CustomerDTO> =
        Mono.just(
            Customer
                .create(
                    customerRepository.nextCustomerId(),
                    Name.valueOf(command.name),
                    NamePronunciation.valueOf(command.namePronunciation),
                    Email.valueOf(command.email)
                )
                .also(customerRepository::add)
                .toDTO()
        )

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    fun update(command: UpdateCustomerCommand): Mono<CustomerDTO> {
        val customerId = CustomerId.valueOf(command.customerId)
        val name = command.name?.let { Name.valueOf(it) }
        val namePronunciation = command.namePronunciation?.let { NamePronunciation.valueOf(it) }
        val email = command.email?.let { Email.valueOf(it) }

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
    fun delete(command: DeleteCustomerCommand): Mono<CustomerDTO> {
        val customerId = CustomerId.valueOf(command.customerId)

        return lock(customerId)
            .map { customer ->
                addressRepository.findAll(customerId)
                    .forEach { addressRepository.remove(it) }

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
