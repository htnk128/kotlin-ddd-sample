package htnk128.kotlin.spring.boot.ddd.sample.contactdetails.application.service

import htnk128.kotlin.spring.boot.ddd.sample.contactdetails.application.dto.ContactDetailsDTO
import htnk128.kotlin.spring.boot.ddd.sample.contactdetails.domain.model.contactdetails.ContactDetails
import htnk128.kotlin.spring.boot.ddd.sample.contactdetails.domain.model.contactdetails.ContactDetailsIdentity
import htnk128.kotlin.spring.boot.ddd.sample.contactdetails.domain.model.contactdetails.ContactDetailsRepository
import htnk128.kotlin.spring.boot.ddd.sample.contactdetails.domain.model.contactdetails.TelephoneNumber
import htnk128.kotlin.spring.boot.ddd.sample.contactdetails.domain.model.customer.CustomerIdentity
import htnk128.kotlin.spring.boot.ddd.sample.contactdetails.domain.model.customer.CustomerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ContactDetailsService(
    private val contactDetailsRepository: ContactDetailsRepository,
    private val customerRepository: CustomerRepository
) {

    @Transactional(readOnly = true)
    fun findAll(aCustomerId: String): List<ContactDetailsDTO> =
        contactDetailsRepository.findAll(CustomerIdentity.valueOf(aCustomerId))
            .map { it.toDTO() }

    @Transactional(timeout = 10, rollbackFor = [Exception::class])
    fun create(aCustomerId: String, aTelephoneNumber: String): ContactDetailsDTO {
        val customerId = CustomerIdentity.valueOf(aCustomerId)
        val telephoneNumber = TelephoneNumber.valueOf(aTelephoneNumber)

        return with(
            customerRepository.find(customerId) ?: throw RuntimeException("customer not found.")
        ) {
            ContactDetails
                .create(contactDetailsRepository.nextContactDetailsId(), this.customerId, telephoneNumber)
                .also(contactDetailsRepository::create)
                .toDTO()
        }
    }

    @Transactional(timeout = 10, rollbackFor = [Exception::class])
    fun update(aContactDetailsId: String, aTelephoneNumber: String): ContactDetailsDTO {
        val contactDetailsId = ContactDetailsIdentity.valueOf(aContactDetailsId)
        val telephoneNumber = TelephoneNumber.valueOf(aTelephoneNumber)

        return with(
            contactDetailsRepository.find(contactDetailsId) ?: throw RuntimeException("contact details not found.")
        ) {
            this.update(telephoneNumber)
                .also { contactDetails ->
                    contactDetailsRepository.update(contactDetails)
                        .takeIf { it > 0 }
                        ?: throw RuntimeException("contact details update failed.")
                }
                .toDTO()
        }
    }
}

private fun ContactDetails.toDTO(): ContactDetailsDTO =
    ContactDetailsDTO(
        contactDetailsId.value,
        customerId.value,
        telephoneNumber.value
    )
