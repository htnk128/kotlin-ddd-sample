package htnk128.kotlin.ddd.sample.address.domain.model.customer

import htnk128.kotlin.ddd.sample.address.domain.exception.CustomerInvalidRequestException
import htnk128.kotlin.ddd.sample.dddcore.domain.SingleValueObject

/**
 * 顧客のメールアドレスを表現する。
 *
 * 100桁までの文字列をもつ。
 */
class Email private constructor(override val value: String) : SingleValueObject<Email, String>() {

    companion object {

        private val LENGTH_RANGE = (1..100)

        /**
         * [value]に指定された値を顧客のメールアドレスに変換する。
         *
         * 値には、100桁までの文字列を指定することが可能である。
         *
         * @throws CustomerInvalidRequestException 条件に違反した値を指定した場合
         * @return 指定された値を持つ顧客のメールアドレス
         */
        fun valueOf(value: String): Email = value
            .takeIf { LENGTH_RANGE.contains(it.length) }
            ?.let { Email(it) }
            ?: throw CustomerInvalidRequestException("Email must be 100 characters or less.")
    }
}
