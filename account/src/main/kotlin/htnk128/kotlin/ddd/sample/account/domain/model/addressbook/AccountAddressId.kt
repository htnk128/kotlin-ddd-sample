package htnk128.kotlin.ddd.sample.account.domain.model.addressbook

import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountInvalidRequestException
import htnk128.kotlin.ddd.sample.ddd.core.domain.SomeIdentity

/**
 * アカウントの住所のIDを表現する。
 *
 * 64桁までの一意な文字列をもつ。
 */
class AccountAddressId private constructor(id: String) : SomeIdentity<AccountAddressId>(id) {

    companion object {

        /**
         * [id]に指定された値をアカウントの住所のIDに変換する。
         *
         * 値には、64桁までの一意な文字列を指定することが可能で、
         * 指定可能な値は、英数字、ハイフン、アンダースコアとなる。
         * この条件に違反した値を指定した場合には例外となる。
         *
         * @throws AccountInvalidRequestException 条件に違反した値を指定した場合
         * @return 指定された値を持つアカウントの住所のID
         */
        fun valueOf(id: String): AccountAddressId = id
            .takeIf { LENGTH_RANGE.contains(it.length) && PATTERN.matches(it) }
            ?.let { AccountAddressId(it) }
            ?: throw AccountInvalidRequestException(
                "Account address id must be 64 characters or less and alphanumeric, hyphen, underscore."
            )
    }
}
