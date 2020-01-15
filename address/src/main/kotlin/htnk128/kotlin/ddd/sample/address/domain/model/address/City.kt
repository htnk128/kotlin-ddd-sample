package htnk128.kotlin.ddd.sample.address.domain.model.address

import htnk128.kotlin.ddd.sample.dddcore.domain.SomeValueObject

/**
 * 住所の市町村を表現する。
 *
 * 100桁までの文字列をもつ。
 */
class City private constructor(value: String) : SomeValueObject<City, String>(value) {

    companion object {

        private val LENGTH_RANGE = (1..100)

        /**
         * [value]に指定された値を住所の市町村に変換する。
         *
         * 値には、100桁までの文字列を指定することが可能である。
         *
         * @throws AddressInvalidRequestException 条件に違反した値を指定した場合
         * @return 指定された値を持つ住所の市町村
         */
        fun valueOf(value: String): City = value
            .takeIf { LENGTH_RANGE.contains(it.length) }
            ?.let { City(it) }
            ?: throw AddressInvalidRequestException("City must be 100 characters or less.")
    }
}
