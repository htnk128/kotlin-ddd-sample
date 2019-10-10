package htnk128.kotlin.spring.boot.ddd.sample.core.domain

interface ValueObject<T> {

    fun sameValueAs(other: T): Boolean
}
