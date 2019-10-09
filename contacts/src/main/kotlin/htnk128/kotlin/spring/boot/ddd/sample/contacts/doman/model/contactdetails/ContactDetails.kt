package htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.contactdetails

import htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.shared.Entity

class ContactDetails(
    val contactDetailsId: ContactDetailsIdentity,
    val customerId: CustomerIdentity,
    val telephoneNumber: TelephoneNumber
) : Entity<ContactDetails> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ContactDetails
        return sameIdentityAs(other)
    }

    override fun hashCode(): Int = contactDetailsId.hashCode()

    override fun sameIdentityAs(other: ContactDetails): Boolean = contactDetailsId == other.contactDetailsId
}
