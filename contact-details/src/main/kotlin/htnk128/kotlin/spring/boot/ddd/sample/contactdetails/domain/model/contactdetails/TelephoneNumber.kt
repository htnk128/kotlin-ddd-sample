package htnk128.kotlin.spring.boot.ddd.sample.contactdetails.domain.model.contactdetails

import htnk128.kotlin.spring.boot.ddd.sample.core.domain.SingleValueObject

class TelephoneNumber(override val value: String) : SingleValueObject<TelephoneNumber, String>() {

    companion object {

        private val LENGTH_RANGE = (1..50)
        private val PATTERN = "[\\p{Digit}:-]*".toRegex()

        fun valueOf(value: String): TelephoneNumber {
            return value
                .takeIf { LENGTH_RANGE.contains(it.length) && PATTERN.matches(it) }
                ?.let {
                    TelephoneNumber(
                        it
                    )
                }
                ?: throw IllegalArgumentException("telephoneNumber must be 50 characters or less.")
        }
    }
}
