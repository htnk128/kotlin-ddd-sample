package htnk128.kotlin.ddd.sample.address.domain.model.address

import htnk128.kotlin.ddd.sample.address.domain.exception.AddressInvalidRequestException
import htnk128.kotlin.ddd.sample.dddcore.domain.SingleValueObject

/**
 * 住所の住所欄2を表現する。
 *
 * 100桁までの文字列をもつ。
 */
class Line2 private constructor(private val value: String) : SingleValueObject<Line2, String>() {

    override fun toValue(): String = value

    companion object {

        private val LENGTH_RANGE = (1..100)

        /**
         * [value]に指定された値を住所の住所欄2に変換する。
         *
         * 値には、100桁までの文字列を指定することが可能である。
         *
         * @throws AddressInvalidRequestException 条件に違反した値を指定した場合
         * @return 指定された値を持つ住所の住所欄2
         */
        fun valueOf(value: String): Line2 = value
            .takeIf { LENGTH_RANGE.contains(it.length) }
            ?.let { Line2(it) }
            ?: throw AddressInvalidRequestException("Line2 must be 100 characters or less.")
    }
}
