package htnk128.kotlin.spring.boot.ddd.sample.contacts.application.service

import htnk128.kotlin.spring.boot.ddd.sample.contacts.application.service.dto.ContactsDTO
import htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.contactdetails.ContactDetails
import htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.contactdetails.ContactDetailsIdentity
import htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.contactdetails.ContactDetailsRepository
import htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.contactdetails.TelephoneNumber
import htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.customer.CustomerIdentity
import htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.customer.CustomerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ContactsService(
    private val contactDetailsRepository: ContactDetailsRepository,
    private val customerRepository: CustomerRepository
) {

    @Transactional(readOnly = true)
    fun findAll(aCustomerId: String): List<ContactsDTO> =
        contactDetailsRepository.findAll(CustomerIdentity.valueOf(aCustomerId))
            .map { it.toDTO() }

    @Transactional(timeout = 10, rollbackFor = [Exception::class])
    fun create(aCustomerId: String, aTelephoneNumber: String): ContactsDTO =
        customerRepository.find(CustomerIdentity.valueOf(aCustomerId))
            ?.run {
                ContactDetails(
                    contactDetailsRepository.nextContactDetailsId(),
                    customerId,
                    TelephoneNumber.valueOf(aTelephoneNumber)
                )
                    .also(contactDetailsRepository::create)
                    .toDTO()
            }
            ?: throw RuntimeException("customer not found.")

    @Transactional(timeout = 10, rollbackFor = [Exception::class])
    fun update(aContactDetailsId: String, aTelephoneNumber: String): ContactsDTO {
        val contactDetailsId = ContactDetailsIdentity.valueOf(aContactDetailsId)
        val telephoneNumber = TelephoneNumber.valueOf(aTelephoneNumber)

        return contactDetailsRepository.find(contactDetailsId)
            ?.let { ContactDetails(it.contactDetailsId, it.customerId, telephoneNumber) }
            ?.also { contactDetails ->
                contactDetailsRepository.update(contactDetails)
                    .takeIf { it > 0 }
                    ?: throw RuntimeException("contacts update failed.")
            }
            ?.toDTO()
            ?: throw RuntimeException("contacts not found.")
    }
}

private fun ContactDetails.toDTO(): ContactsDTO =
    ContactsDTO(
        contactDetailsId.value,
        customerId.value,
        telephoneNumber.value
    )
