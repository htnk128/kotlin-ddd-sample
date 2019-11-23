package htnk128.kotlin.spring.boot.ddd.sample.contactdetails.domain.model.customer

import htnk128.kotlin.spring.boot.ddd.sample.dddcore.domain.SingleValueObject

class Name(override val value: String) : SingleValueObject<Name, String>() {

    companion object {

        private val LENGTH_RANGE = (1..100)

        fun valueOf(value: String): Name {
            return value
                .takeIf { LENGTH_RANGE.contains(it.length) }
                ?.let {
                    Name(
                        it
                    )
                }
                ?: throw IllegalArgumentException("name must be 100 characters or less.")
        }
    }
}
