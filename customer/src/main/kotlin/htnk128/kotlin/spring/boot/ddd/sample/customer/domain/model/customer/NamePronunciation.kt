package htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer

import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.exception.CustomerInvalidRequestException
import htnk128.kotlin.spring.boot.ddd.sample.dddcore.domain.SingleValueObject

/**
 * 顧客の発音を表現する。
 *
 * 100桁までの文字列をもつ。
 */
class NamePronunciation private constructor(override val value: String) :
    SingleValueObject<NamePronunciation, String>() {

    companion object {

        private val LENGTH_RANGE = (1..100)

        /**
         * [value]に指定された値を顧客の発音に変換する。
         *
         * 値には、100桁までの文字列を指定することが可能である。
         *
         * @throws CustomerInvalidRequestException 条件に違反した値を指定した場合
         * @return 指定された値を持つ顧客の発音
         */
        fun valueOf(value: String): NamePronunciation = value
            .takeIf { LENGTH_RANGE.contains(it.length) }
            ?.let { NamePronunciation(it) }
            ?: throw CustomerInvalidRequestException("Name pronunciation must be 100 characters or less.")
    }
}
