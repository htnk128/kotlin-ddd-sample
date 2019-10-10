package htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.customer

import htnk128.kotlin.spring.boot.ddd.sample.core.domain.Identity

class CustomerIdentity(override val value: String) : Identity<CustomerIdentity, String> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CustomerIdentity
        return sameValueAs(other)
    }

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value

    override fun sameValueAs(other: CustomerIdentity): Boolean = value == other.value

    companion object {

        private val LENGTH_RANGE = (1..64)
        private val PATTERN = "[\\p{Alnum}-_]*".toRegex()

        fun valueOf(value: String): CustomerIdentity {
            return value
                .takeIf { LENGTH_RANGE.contains(it.length) && PATTERN.matches(it) }
                ?.let { CustomerIdentity(it) }
                ?: throw IllegalArgumentException("customerId must be 64 characters or less and alphanumeric.")
        }
    }
}
