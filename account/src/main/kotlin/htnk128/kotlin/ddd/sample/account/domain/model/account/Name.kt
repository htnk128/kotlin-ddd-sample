package htnk128.kotlin.ddd.sample.account.domain.model.account

import htnk128.kotlin.ddd.sample.dddcore.domain.SomeValueObject

/**
 * アカウントの氏名または会社名を表現する。
 *
 * 100桁までの文字列をもつ。
 */
class Name private constructor(value: String) : SomeValueObject<Name, String>(value) {

    companion object {

        private val LENGTH_RANGE = (1..100)

        /**
         * [value]に指定された値をアカウントの氏名または会社名に変換する。
         *
         * 値には、100桁までの文字列を指定することが可能である。
         *
         * @throws AccountInvalidRequestException 条件に違反した値を指定した場合
         * @return 指定された値を持つアカウントの氏名または会社名
         */
        fun valueOf(value: String): Name = value
            .takeIf { LENGTH_RANGE.contains(it.length) }
            ?.let { Name(it) }
            ?: throw AccountInvalidRequestException("Name must be 100 characters or less.")
    }
}
