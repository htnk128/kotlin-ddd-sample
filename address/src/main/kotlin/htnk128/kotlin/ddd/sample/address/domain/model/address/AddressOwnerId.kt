package htnk128.kotlin.ddd.sample.address.domain.model.address

import htnk128.kotlin.ddd.sample.dddcore.domain.SomeIdentity

/**
 * 住所の持ち主のIDを表現する。
 *
 * 64桁までの一意な文字列をもつ。
 */
class AddressOwnerId private constructor(id: String) : SomeIdentity<AddressOwnerId>(id) {

    companion object {

        /**
         * [id]に指定された値をアカウントのIDに変換する。
         *
         * 値には、64桁までの一意な文字列を指定することが可能で、
         * 指定可能な値は、英数字、ハイフン、アンダースコアとなる。
         * この条件に違反した値を指定した場合には例外となる。
         *
         * @throws AddressInvalidRequestException 条件に違反した値を指定した場合
         * @return 指定された値を持つアカウントのID
         */
        fun valueOf(id: String): AddressOwnerId = id
            .takeIf { LENGTH_RANGE.contains(it.length) && PATTERN.matches(it) }
            ?.let { AddressOwnerId(it) }
            ?: throw AddressInvalidRequestException(
                "Account owner id must be 64 characters or less and alphanumeric, hyphen, underscore."
            )
    }
}
