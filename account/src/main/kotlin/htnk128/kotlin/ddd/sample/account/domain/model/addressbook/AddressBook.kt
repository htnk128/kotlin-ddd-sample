package htnk128.kotlin.ddd.sample.account.domain.model.addressbook

import htnk128.kotlin.ddd.sample.ddd.core.domain.ValueObject

/**
 * アカウントの住所録を表現する。
 */
class AddressBook(
    private val allAccountAddresses: List<AccountAddress>
) : ValueObject<AddressBook> {

    /**
     * 有効なアカウントの住所一覧。
     *
     * 有効とはアカウントの住所が有効([AccountAddress.isAvailable]=`true`)なものである。
     */
    val availableAccountAddresses = allAccountAddresses
        .filter { it.isAvailable }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as AddressBook
        return sameValueAs(other)
    }

    override fun hashCode(): Int = allAccountAddresses.hashCode()

    override fun sameValueAs(other: AddressBook): Boolean = allAccountAddresses == other.allAccountAddresses
}
