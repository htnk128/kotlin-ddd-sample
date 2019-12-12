package htnk128.kotlin.ddd.sample.address.domain.model.address

import htnk128.kotlin.ddd.sample.address.domain.exception.AddressInvalidRequestException
import htnk128.kotlin.ddd.sample.dddcore.domain.SingleValueObject

/**
 * 住所の住所欄1を表現する。
 *
 * 100桁までの文字列をもつ。
 */
class Line1 private constructor(override val value: String) : SingleValueObject<Line1, String>() {

    companion object {

        private val LENGTH_RANGE = (1..100)

        /**
         * [value]に指定された値を住所の住所欄1に変換する。
         *
         * 値には、100桁までの文字列を指定することが可能である。
         *
         * @throws AddressInvalidRequestException 条件に違反した値を指定した場合
         * @return 指定された値を持つ住所の住所欄1
         */
        fun valueOf(value: String): Line1 = value
            .takeIf { LENGTH_RANGE.contains(it.length) }
            ?.let { Line1(it) }
            ?: throw AddressInvalidRequestException(
                "Line1 must be 100 characters or less."
            )
    }
}
