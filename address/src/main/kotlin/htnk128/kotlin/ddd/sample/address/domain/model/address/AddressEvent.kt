package htnk128.kotlin.ddd.sample.address.domain.model.address

import htnk128.kotlin.ddd.sample.dddcore.domain.DomainEvent
import htnk128.kotlin.ddd.sample.dddcore.domain.ValueObject
import java.time.Instant

/**
 * 住所のイベントを表現する。
 *
 * @param T [AddressEvent]
 */
sealed class AddressEvent<T : AddressEvent<T>> :
    DomainEvent<T> {

    abstract val type: Type

    abstract val address: Address

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
        CREATED("address.created"),
        UPDATED("address.updated"),
        DELETED("address.deleted");

        override fun sameValueAs(other: Type): Boolean =
            value == other.value
    }
}

/**
 * 住所の作成イベントを表現する。
 */
class AddressCreated(override val address: Address) : AddressEvent<AddressCreated>() {

    override val type: Type =
        Type.CREATED
}

/**
 * 住所の更新イベントを表現する。
 */
class AddressUpdated(override val address: Address) : AddressEvent<AddressUpdated>() {

    override val type: Type =
        Type.UPDATED
}

/**
 * 住所の削除イベントを表現する。
 */
class AddressDeleted(override val address: Address) : AddressEvent<AddressDeleted>() {

    override val type: Type =
        Type.DELETED
}
