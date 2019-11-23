package htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer

import htnk128.kotlin.spring.boot.ddd.sample.core.domain.DomainEvent
import htnk128.kotlin.spring.boot.ddd.sample.core.domain.ValueObject
import java.time.Instant

sealed class CustomerEvent<T : CustomerEvent<T>> : DomainEvent<T> {

    abstract val type: Type

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

    enum class Type(private val value: String) : ValueObject<Type> {
        CREATED("customer.created"),
        UPDATED("customer.updated"),
        ;

        override fun sameValueAs(other: Type): Boolean =
            value == other.value
    }
}

class CustomerCreated() : CustomerEvent<CustomerCreated>() {

    override val type: Type = Type.CREATED
}

class CustomerUpdated() : CustomerEvent<CustomerUpdated>() {

    override val type: Type = Type.UPDATED
}
