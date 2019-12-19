package htnk128.kotlin.ddd.sample.address.domain.model.account

import htnk128.kotlin.ddd.sample.dddcore.domain.Entity
import java.time.Instant

/**
 * アカウントのコンテキストマッピングを表現する。
 */
class Account(
    val accountId: AccountId,
    val createdAt: Instant,
    val deletedAt: Instant?,
    val updatedAt: Instant
) : Entity<Account> {

    /**
     * このアカウントが削除されている場合に`true`を返す。
     */
    val isDeleted: Boolean = deletedAt != null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Account
        return sameIdentityAs(other)
    }

    override fun hashCode(): Int = accountId.hashCode()

    override fun sameIdentityAs(other: Account): Boolean = accountId == other.accountId
}
