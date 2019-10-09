package htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.shared

interface ValueObject<T> {

    fun sameValueAs(other: T): Boolean
}
