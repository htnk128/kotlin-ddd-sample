package htnk128.kotlin.spring.boot.ddd.sample.dddcore.domain

interface ValueObject<T> {

    fun sameValueAs(other: T): Boolean
}
