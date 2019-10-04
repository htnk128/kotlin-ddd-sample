package htnk128.kotlin.spring.boot.ddd.sample.domain.shared

interface Entity<T> {

    fun sameIdentityAs(other: T): Boolean
}
