package htnk128.kotlin.ddd.sample.address.domain.model.address

import htnk128.kotlin.ddd.sample.dddcore.domain.SomeValueObject

/**
 * 住所の氏名または会社名を表現する。
 *
 * 100桁までの文字列をもつ。
 */
class FullName private constructor(value: String) : SomeValueObject<FullName, String>(value) {

    companion object {

        private val LENGTH_RANGE = (1..100)

        /**
         * [value]に指定された値を住所の氏名または会社名に変換する。
         *
         * 値には、100桁までの文字列を指定することが可能である。
         *
         * @throws AddressInvalidRequestException 条件に違反した値を指定した場合
         * @return 指定された値を持つ住所の氏名または会社名
         */
        fun valueOf(value: String): FullName = value
            .takeIf { LENGTH_RANGE.contains(it.length) }
            ?.let { FullName(it) }
            ?: throw AddressInvalidRequestException("Full name must be 100 characters or less.")
    }
}
