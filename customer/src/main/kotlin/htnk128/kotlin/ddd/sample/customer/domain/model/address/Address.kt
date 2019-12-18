package htnk128.kotlin.ddd.sample.customer.domain.model.address

import htnk128.kotlin.ddd.sample.dddcore.domain.Entity

/**
 * 顧客のコンテキストマッピングを表現する。
 */
class Address(
    val addressId: AddressId
) : Entity<Address> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Address
        return sameIdentityAs(other)
    }

    override fun hashCode(): Int = addressId.hashCode()

    override fun sameIdentityAs(other: Address): Boolean = addressId == other.addressId
}
