package htnk128.kotlin.ddd.sample.account.domain.model.account

import htnk128.kotlin.ddd.sample.dddcore.domain.SomeValueObject

/**
 * アカウントの発音を表現する。
 *
 * 100桁までの文字列をもつ。
 */
class NamePronunciation private constructor(value: String) : SomeValueObject<NamePronunciation, String>(value) {

    companion object {

        private val LENGTH_RANGE = (1..100)

        /**
         * [value]に指定された値をアカウントの発音に変換する。
         *
         * 値には、100桁までの文字列を指定することが可能である。
         *
         * @throws AccountInvalidRequestException 条件に違反した値を指定した場合
         * @return 指定された値を持つアカウントの発音
         */
        fun valueOf(value: String): NamePronunciation = value
            .takeIf { LENGTH_RANGE.contains(it.length) }
            ?.let { NamePronunciation(it) }
            ?: throw AccountInvalidRequestException("Name pronunciation must be 100 characters or less.")
    }
}
