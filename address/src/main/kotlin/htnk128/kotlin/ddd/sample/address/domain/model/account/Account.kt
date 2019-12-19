package htnk128.kotlin.ddd.sample.address.domain.model.account

import htnk128.kotlin.ddd.sample.dddcore.domain.Entity
import java.time.Instant

/**
 * アカウントのコンテキストマッピングを表現する。
 */
class Account(
    val accountId: AccountId,
    val deletedAt: Instant?
) : Entity<Account> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Account
        return sameIdentityAs(other)
    }

    override fun hashCode(): Int = accountId.hashCode()

    override fun sameIdentityAs(other: Account): Boolean = accountId == other.accountId
}
