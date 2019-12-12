package htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.address

import htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.exception.AddressInvalidRequestException
import htnk128.kotlin.spring.boot.ddd.sample.dddcore.domain.SingleValueObject

/**
 * 住所の都道府県を表現する。
 *
 * 100桁までの文字列をもつ。
 */
class StateOrRegion private constructor(override val value: String) : SingleValueObject<StateOrRegion, String>() {

    companion object {

        private val LENGTH_RANGE = (1..100)

        /**
         * [value]に指定された値を住所の都道府県に変換する。
         *
         * 値には、100桁までの文字列を指定することが可能である。
         *
         * @throws AddressInvalidRequestException 条件に違反した値を指定した場合
         */
        fun valueOf(value: String): StateOrRegion = value
            .takeIf { LENGTH_RANGE.contains(it.length) }
            ?.let { StateOrRegion(it) }
            ?: throw AddressInvalidRequestException("State or region must be 100 characters or less.")
    }
}
