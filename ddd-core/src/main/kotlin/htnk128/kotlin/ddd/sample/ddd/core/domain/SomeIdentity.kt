package htnk128.kotlin.ddd.sample.ddd.core.domain

/**
 * 何らかのドメインを識別するIDを表現した値オブジェクト。
 *
 * @param T 値オブジェクトの型
 */
abstract class SomeIdentity<T : SomeIdentity<T>>(private val id: String) : Identity<T> {

    override fun id(): String = id

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        @Suppress("UNCHECKED_CAST")
        other as T
        return sameValueAs(other)
    }

    override fun hashCode(): Int = id().hashCode()

    override fun sameValueAs(other: T): Boolean = id() == other.id()

    override fun toString(): String = id()

    protected companion object {

        val LENGTH_RANGE = (1..64)
        val PATTERN = "[\\p{Alnum}-_]*".toRegex()
    }
}
