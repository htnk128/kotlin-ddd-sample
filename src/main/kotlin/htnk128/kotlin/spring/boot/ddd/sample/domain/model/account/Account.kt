package htnk128.kotlin.spring.boot.ddd.sample.domain.model.account

import htnk128.kotlin.spring.boot.ddd.sample.domain.shared.Entity

class Account(val accountId: AccountIdentity, val name: Name) : Entity<Account> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Account
        return sameIdentityAs(other)
    }

    override fun hashCode(): Int = accountId.hashCode()

    override fun sameIdentityAs(other: Account): Boolean = accountId == other.accountId
}
