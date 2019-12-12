package htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.address

import htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.exception.AddressInvalidRequestException
import htnk128.kotlin.spring.boot.ddd.sample.dddcore.domain.SingleValueObject

/**
 * 住所の郵便番号を表現する。
 *
 * 50桁までの文字列をもつ。
 */
class ZipCode private constructor(override val value: String) : SingleValueObject<ZipCode, String>() {

    companion object {

        private val LENGTH_RANGE = (1..50)
        private val PATTERN = "[\\p{Alnum}:-]*".toRegex()

        /**
         * [value]に指定された値を住所の電話番号に変換する。
         *
         * 値には、50桁までの文字列を指定することが可能で、指定可能な値は、英数字となる。
         * この条件に違反した値を指定した場合には例外となる。
         *
         * @throws AddressInvalidRequestException 条件に違反した値を指定した場合
         */
        fun valueOf(value: String): ZipCode = value
            .takeIf { LENGTH_RANGE.contains(it.length) && PATTERN.matches(it) }
            ?.let { ZipCode(it) }
            ?: throw AddressInvalidRequestException("Zip code must be 50 characters or less and alphanumeric.")
    }
}
