package htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.customer

import htnk128.kotlin.spring.boot.ddd.sample.dddcore.domain.Entity
import java.time.Instant

/**
 * 顧客のコンテキストマッピングを表現する。
 */
class Customer(
    val customerId: CustomerId,
    val name: Name,
    val namePronunciation: NamePronunciation,
    val email: Email,
    val createdAt: Instant,
    val updatedAt: Instant
) : Entity<Customer> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Customer
        return sameIdentityAs(other)
    }

    override fun hashCode(): Int = customerId.hashCode()

    override fun sameIdentityAs(other: Customer): Boolean = customerId == other.customerId
}
