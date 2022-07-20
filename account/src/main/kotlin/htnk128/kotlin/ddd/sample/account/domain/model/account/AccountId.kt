package htnk128.kotlin.ddd.sample.account.domain.model.account

import htnk128.kotlin.ddd.sample.ddd.core.domain.SomeIdentity
import java.util.UUID

/**
 * アカウントのIDを表現する。
 *
 * 64桁までの一意な文字列をもつ。
 */
class AccountId private constructor(id: String) : SomeIdentity<AccountId>(id) {

    companion object {

        /**
         * [UUID]を用いてアカウントのIDを生成する。
         *
         * @return 生成した値を持つアカウントのID
         */
        fun generate(): AccountId = AccountId("AC_${UUID.randomUUID()}")

        /**
         * [id]に指定された値をアカウントのIDに変換する。
         *
         * 値には、64桁までの一意な文字列を指定することが可能で、
         * 指定可能な値は、英数字、ハイフン、アンダースコアとなる。
         * この条件に違反した値を指定した場合には例外となる。
         *
         * @throws AccountInvalidRequestException 条件に違反した値を指定した場合
         * @return 指定された値を持つアカウントのID
         */
        fun valueOf(id: String): AccountId = id
            .takeIf { LENGTH_RANGE.contains(it.length) && PATTERN.matches(it) }
            ?.let { AccountId(it) }
            ?: throw AccountInvalidRequestException(
                "Account id must be 64 characters or less and alphanumeric, hyphen, underscore."
            )
    }
}
