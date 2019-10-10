package htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.contactdetails

import htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.customer.CustomerIdentity

interface ContactDetailsRepository {

    fun find(contactDetailsId: ContactDetailsIdentity): ContactDetails?

    fun findAll(customerId: CustomerIdentity): List<ContactDetails>

    fun create(contactDetails: ContactDetails)

    fun update(contactDetails: ContactDetails): Int

    fun nextContactDetailsId(): ContactDetailsIdentity = ContactDetailsIdentity.generate()
}
