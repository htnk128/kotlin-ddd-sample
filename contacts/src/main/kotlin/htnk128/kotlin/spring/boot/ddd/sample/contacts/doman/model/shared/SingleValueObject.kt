package htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.shared

abstract class SingleValueObject<T : SingleValueObject<T, V>, V : Comparable<V>> : ValueObject<T> {

    abstract val value: V

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        @Suppress("UNCHECKED_CAST")
        other as T
        return sameValueAs(other)
    }

    override fun hashCode(): Int = value.hashCode()

    override fun sameValueAs(other: T): Boolean = value == other.value

    override fun toString(): String = "$value"
}
