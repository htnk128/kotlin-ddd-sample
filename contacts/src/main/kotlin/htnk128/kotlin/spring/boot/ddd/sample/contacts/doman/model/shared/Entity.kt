package htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.shared

interface Entity<T> {

    fun sameIdentityAs(other: T): Boolean
}
