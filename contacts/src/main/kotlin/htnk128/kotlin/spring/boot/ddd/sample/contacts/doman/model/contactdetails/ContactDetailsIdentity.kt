package htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.contactdetails

import htnk128.kotlin.spring.boot.ddd.sample.core.domain.Identity
import java.util.UUID

class ContactDetailsIdentity(override val value: String) : Identity<ContactDetailsIdentity, String> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ContactDetailsIdentity
        return sameValueAs(other)
    }

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value

    override fun sameValueAs(other: ContactDetailsIdentity): Boolean = value == other.value

    companion object {

        fun generate(): ContactDetailsIdentity = ContactDetailsIdentity(UUID.randomUUID().toString())

        private val LENGTH_RANGE = (1..64)
        private val PATTERN = "[\\p{Alnum}-_]*".toRegex()

        fun valueOf(value: String): ContactDetailsIdentity {
            return value
                .takeIf { LENGTH_RANGE.contains(it.length) && PATTERN.matches(it) }
                ?.let { ContactDetailsIdentity(it) }
                ?: throw IllegalArgumentException("contactDetailsIdentity must be 64 characters or less and alphanumeric.")
        }
    }
}
