package htnk128.kotlin.ddd.sample.account.domain.model.addressbook

import htnk128.kotlin.ddd.sample.dddcore.domain.ValueObject
import java.time.Instant

/**
 * アカウントの住所を表現する。
 */
class AccountAddress(
    val accountAddressId: AccountAddressId,
    private val deletedAt: Instant?
) : ValueObject<AccountAddress> {

    /**
     * アカウントの住所が有効な場合に`true`を返す。
     *
     * 有効とは[deletedAt]が`null`の場合である。
     */
    val isAvailable: Boolean = deletedAt == null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as AccountAddress
        return sameValueAs(other)
    }

    override fun hashCode(): Int {
        var result = accountAddressId.hashCode()
        result = 31 * result + deletedAt.hashCode()
        return result
    }

    override fun sameValueAs(other: AccountAddress): Boolean {
        if (accountAddressId != other.accountAddressId) return false
        if (deletedAt != other.deletedAt) return false
        return true
    }
}
