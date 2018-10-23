package htnk128.kotlin.spring.boot.ddd.sample.domain.model.account

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonValue
import htnk128.kotlin.spring.boot.ddd.sample.domain.model.Identity
import java.util.UUID

class AccountIdentity(
    @get:JsonValue
    @get:JsonIgnore
    override val value: String
) : Identity<AccountIdentity, String> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as AccountIdentity
        return sameValueAs(other)
    }

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value

    override fun sameValueAs(other: AccountIdentity): Boolean = value == other.value

    companion object {

        fun generate(): AccountIdentity = AccountIdentity(UUID.randomUUID().toString())
    }
}
