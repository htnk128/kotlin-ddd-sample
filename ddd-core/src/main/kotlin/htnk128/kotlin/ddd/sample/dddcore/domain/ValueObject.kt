package htnk128.kotlin.ddd.sample.dddcore.domain

interface ValueObject<T> {

    fun sameValueAs(other: T): Boolean
}
