package htnk128.kotlin.ddd.sample.address.domain.model.address

import htnk128.kotlin.ddd.sample.ddd.core.domain.SomeIdentity
import java.util.UUID

/**
 * 住所のIDを表現する。
 *
 * 64桁までの一意な文字列をもつ。
 */
class AddressId private constructor(id: String) : SomeIdentity<AddressId>(id) {

    companion object {

        /**
         * [UUID]を用いて住所のIDを生成する。
         *
         * @return 生成した値を持つ住所のID
         */
        fun generate(): AddressId = AddressId("ADDR_${UUID.randomUUID()}")

        /**
         * [id]に指定された値を住所のIDに変換する。
         *
         * 値には、64桁までの一意な文字列を指定することが可能で、
         * 指定可能な値は、英数字、ハイフン、アンダースコアとなる。
         * この条件に違反した値を指定した場合には例外となる。
         *
         * @throws AddressInvalidRequestException 条件に違反した値を指定した場合
         * @return 指定された値を持つ住所のID
         */
        fun valueOf(id: String): AddressId = id
            .takeIf { LENGTH_RANGE.contains(it.length) && PATTERN.matches(it) }
            ?.let { AddressId(it) }
            ?: throw AddressInvalidRequestException(
                "Address id must be 64 characters or less and alphanumeric, hyphen, underscore."
            )
    }
}
