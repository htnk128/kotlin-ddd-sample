package htnk128.kotlin.ddd.sample.customer.domain.model.customer

import htnk128.kotlin.ddd.sample.dddcore.domain.DomainEvent
import htnk128.kotlin.ddd.sample.dddcore.domain.ValueObject
import java.time.Instant

/**
 * 顧客のイベントを表現する。
 *
 * @param T [CustomerEvent]
 */
sealed class CustomerEvent<T : CustomerEvent<T>> :
    DomainEvent<T> {

    abstract val type: Type

    abstract val customer: Customer

    val occurredOn: Instant = Instant.now()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        @Suppress("UNCHECKED_CAST")
        other as T
        return sameEventAs(other)
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + occurredOn.hashCode()
        return result
    }

    override fun sameEventAs(other: T): Boolean {
        if (type != other.type) return false
        if (occurredOn != other.occurredOn) return false
        return true
    }

    enum class Type(private val value: String) :
        ValueObject<Type> {
        CREATED("customer.created"),
        UPDATED("customer.updated"),
        DELETED("customer.deleted");

        override fun sameValueAs(other: Type): Boolean =
            value == other.value
    }
}

/**
 * 顧客の作成イベントを表現する。
 */
class CustomerCreated(override val customer: Customer) : CustomerEvent<CustomerCreated>() {

    override val type: Type =
        Type.CREATED
}

/**
 * 顧客の更新イベントを表現する。
 */
class CustomerUpdated(override val customer: Customer) : CustomerEvent<CustomerUpdated>() {

    override val type: Type =
        Type.UPDATED
}

/**
 * 顧客の削除イベントを表現する。
 */
class CustomerDeleted(override val customer: Customer) : CustomerEvent<CustomerDeleted>() {

    override val type: Type =
        Type.DELETED
}
