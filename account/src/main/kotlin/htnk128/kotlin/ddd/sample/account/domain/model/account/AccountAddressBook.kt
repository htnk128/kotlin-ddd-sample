package htnk128.kotlin.ddd.sample.account.domain.model.account

import htnk128.kotlin.ddd.sample.dddcore.domain.ValueObject

/**
 * アカウントの住所録を表現する。
 */
class AccountAddressBook(
    private val allAccountAddresses: List<AccountAddress>
) : ValueObject<AccountAddressBook> {

    val availableAccountAddresses = allAccountAddresses
        .filter { it.isAvailable }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as AccountAddressBook
        return sameValueAs(other)
    }

    override fun hashCode(): Int = allAccountAddresses.hashCode()

    override fun sameValueAs(other: AccountAddressBook): Boolean = allAccountAddresses == other.allAccountAddresses
}
