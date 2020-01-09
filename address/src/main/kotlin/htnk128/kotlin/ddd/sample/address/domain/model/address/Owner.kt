package htnk128.kotlin.ddd.sample.address.domain.model.address

import htnk128.kotlin.ddd.sample.dddcore.domain.ValueObject
import java.time.Instant

/**
 * 住所([Address])の持ち主を表現する。
 */
class Owner(
    val accountId: AccountId,
    private val deletedAt: Instant?
) : ValueObject<Owner> {

    /**
     * この住所の持ち主が有効な場合に`true`を返す。
     *
     * 有効とは[deletedAt]が`null`の場合である。
     */
    val isAvailable: Boolean = deletedAt == null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Owner
        return sameValueAs(other)
    }

    override fun hashCode(): Int {
        var result = accountId.hashCode()
        result = 31 * result + deletedAt.hashCode()
        return result
    }

    override fun sameValueAs(other: Owner): Boolean {
        if (accountId != other.accountId) return false
        if (deletedAt != other.deletedAt) return false
        return true
    }
}
