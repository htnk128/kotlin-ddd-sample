package htnk128.kotlin.ddd.sample.address.domain.model.owner

import htnk128.kotlin.ddd.sample.ddd.core.domain.SomeIdentity

/**
 * 住所の持ち主のIDを表現する。
 *
 * 64桁までの一意な文字列をもつ。
 */
class OwnerId private constructor(id: String) : SomeIdentity<OwnerId>(id) {

    companion object {

        /**
         * [id]に指定された値を住所の持ち主のIDに変換する。
         *
         * 値には、64桁までの一意な文字列を指定することが可能で、
         * 指定可能な値は、英数字、ハイフン、アンダースコアとなる。
         * この条件に違反した値を指定した場合には例外となる。
         *
         * @throws OwnerInvalidRequestException 条件に違反した値を指定した場合
         * @return 指定された値を持つ住所の持ち主のID
         */
        fun valueOf(id: String): OwnerId = id
            .takeIf { LENGTH_RANGE.contains(it.length) && PATTERN.matches(it) }
            ?.let { OwnerId(it) }
            ?: throw OwnerInvalidRequestException(
                "Owner id must be 64 characters or less and alphanumeric, hyphen, underscore."
            )
    }
}
