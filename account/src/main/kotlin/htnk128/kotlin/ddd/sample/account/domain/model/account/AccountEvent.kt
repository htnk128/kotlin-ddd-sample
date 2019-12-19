package htnk128.kotlin.ddd.sample.account.domain.model.account

import htnk128.kotlin.ddd.sample.dddcore.domain.DomainEvent
import htnk128.kotlin.ddd.sample.dddcore.domain.ValueObject
import java.time.Instant

/**
 * アカウントのイベントを表現する。
 *
 * @param T [AccountEvent]
 */
sealed class AccountEvent<T : AccountEvent<T>> : DomainEvent<T> {

    abstract val type: Type

    abstract val account: Account

    override val occurredOn: Instant = Instant.now()

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
        CREATED("account.created"),
        UPDATED("account.updated"),
        DELETED("account.deleted");

        override fun sameValueAs(other: Type): Boolean = value == other.value
    }
}

/**
 * アカウントの作成イベントを表現する。
 */
class AccountCreated(override val account: Account) : AccountEvent<AccountCreated>() {

    override val type: Type = Type.CREATED
}

/**
 * アカウントの更新イベントを表現する。
 */
class AccountUpdated(override val account: Account) : AccountEvent<AccountUpdated>() {

    override val type: Type = Type.UPDATED
}

/**
 * アカウントの削除イベントを表現する。
 */
class AccountDeleted(override val account: Account) : AccountEvent<AccountDeleted>() {

    override val type: Type = Type.DELETED
}
