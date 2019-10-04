package htnk128.kotlin.spring.boot.ddd.sample.domain.shared

interface ValueObject<T> {

    fun sameValueAs(other: T): Boolean
}
