package htnk128.kotlin.ddd.sample.account.domain.model.account

import htnk128.kotlin.ddd.sample.ddd.core.domain.SomeValueObject

/**
 * アカウントのメールアドレスを表現する。
 *
 * 100桁までの文字列をもつ。
 */
class Email private constructor(value: String) : SomeValueObject<Email, String>(value) {

    companion object {

        private val LENGTH_RANGE = (1..100)

        /**
         * [value]に指定された値をアカウントのメールアドレスに変換する。
         *
         * 値には、100桁までの文字列を指定することが可能である。
         *
         * @throws AccountInvalidRequestException 条件に違反した値を指定した場合
         * @return 指定された値を持つアカウントのメールアドレス
         */
        fun valueOf(value: String): Email = value
            .takeIf { LENGTH_RANGE.contains(it.length) }
            ?.let { Email(it) }
            ?: throw AccountInvalidRequestException("Email must be 100 characters or less.")
    }
}
