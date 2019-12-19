package htnk128.kotlin.ddd.sample.address.domain.model.account

import htnk128.kotlin.ddd.sample.address.domain.exception.AccountInvalidRequestException
import htnk128.kotlin.ddd.sample.dddcore.domain.Identity

/**
 * アカウントのIDを表現する。
 *
 * 64桁までの一意な文字列をもつ。
 */
class AccountId private constructor(private val value: String) : Identity<AccountId, String> {

    override fun id(): String = value

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as AccountId
        return sameValueAs(other)
    }

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value

    override fun sameValueAs(other: AccountId): Boolean = value == other.value

    companion object {

        private val LENGTH_RANGE = (1..64)
        private val PATTERN = "[\\p{Alnum}-_]*".toRegex()

        /**
         * [value]に指定された値をアカウントのIDに変換する。
         *
         * 値には、64桁までの一意な文字列を指定することが可能で、
         * 指定可能な値は、英数字、ハイフン、アンダースコアとなる。
         * この条件に違反した値を指定した場合には例外となる。
         *
         * @throws AccountInvalidRequestException 条件に違反した値を指定した場合
         * @return 指定された値を持つアカウントのID
         */
        fun valueOf(value: String): AccountId = value
            .takeIf { LENGTH_RANGE.contains(it.length) && PATTERN.matches(it) }
            ?.let { AccountId(it) }
            ?: throw AccountInvalidRequestException(
                "Account id must be 64 characters or less and alphanumeric, hyphen, underscore."
            )
    }
}
