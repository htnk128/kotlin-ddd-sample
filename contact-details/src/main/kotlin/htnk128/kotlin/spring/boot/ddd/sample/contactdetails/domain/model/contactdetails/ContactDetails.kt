package htnk128.kotlin.spring.boot.ddd.sample.contactdetails.domain.model.contactdetails

import htnk128.kotlin.spring.boot.ddd.sample.contactdetails.domain.model.customer.CustomerIdentity
import htnk128.kotlin.spring.boot.ddd.sample.core.domain.Entity

class ContactDetails(
    val contactDetailsId: ContactDetailsIdentity,
    val customerId: CustomerIdentity,
    val telephoneNumber: TelephoneNumber
) : Entity<ContactDetails> {

    fun update(telephoneNumber: TelephoneNumber): ContactDetails =
        ContactDetails(contactDetailsId, customerId, telephoneNumber)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ContactDetails
        return sameIdentityAs(other)
    }

    override fun hashCode(): Int = contactDetailsId.hashCode()

    override fun sameIdentityAs(other: ContactDetails): Boolean = contactDetailsId == other.contactDetailsId

    companion object {

        fun create(
            contactDetailsId: ContactDetailsIdentity,
            customerId: CustomerIdentity,
            telephoneNumber: TelephoneNumber
        ): ContactDetails =
            ContactDetails(contactDetailsId, customerId, telephoneNumber)
    }
}
