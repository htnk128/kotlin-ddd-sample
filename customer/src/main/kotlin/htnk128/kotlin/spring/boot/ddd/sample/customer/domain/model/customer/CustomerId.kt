package htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer

import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.exception.CustomerInvalidRequestException
import htnk128.kotlin.spring.boot.ddd.sample.dddcore.domain.Identity
import java.util.UUID

/**
 * 顧客のIDを表現する。
 *
 * 64桁までの一意な文字列をもつ。
 */
class CustomerId private constructor(override val value: String) : Identity<CustomerId, String> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CustomerId
        return sameValueAs(other)
    }

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value

    override fun sameValueAs(other: CustomerId): Boolean = value == other.value

    companion object {

        private val LENGTH_RANGE = (1..64)
        private val PATTERN = "[\\p{Alnum}-_]*".toRegex()

        /**
         * [UUID]を用いて顧客IDを生成する。
         */
        fun generate(): CustomerId = CustomerId("CUS_${UUID.randomUUID()}")

        /**
         * [value]に指定された値を顧客IDに変換する。
         *
         * 値には、64桁までの一意な文字列を指定することが可能で、
         * 指定可能な値は、英数字、ハイフン、アンダースコアとなる。
         * この条件に違反した値を指定した場合には例外となる。
         *
         * @throws CustomerInvalidRequestException 条件に違反した値を指定した場合
         */
        fun valueOf(value: String): CustomerId = value
            .takeIf { LENGTH_RANGE.contains(it.length) && PATTERN.matches(it) }
            ?.let { CustomerId(it) }
            ?: throw CustomerInvalidRequestException(
                "Customer id must be 64 characters or less and alphanumeric, hyphen, underscore."
            )
    }
}
