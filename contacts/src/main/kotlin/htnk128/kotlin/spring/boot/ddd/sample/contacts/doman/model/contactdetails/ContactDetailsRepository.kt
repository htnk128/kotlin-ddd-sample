package htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.contactdetails

interface ContactDetailsRepository {

    fun find(contactDetailsId: ContactDetailsIdentity): ContactDetails?

    fun findAll(): List<ContactDetails>

    fun create(contactDetails: ContactDetails)

    fun update(contactDetails: ContactDetails): Int

    fun nextContactDetailsId(): ContactDetailsIdentity = ContactDetailsIdentity.generate()
}
