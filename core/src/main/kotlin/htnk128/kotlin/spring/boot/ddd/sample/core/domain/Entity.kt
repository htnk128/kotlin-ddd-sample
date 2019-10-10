package htnk128.kotlin.spring.boot.ddd.sample.core.domain

interface Entity<T> {

    fun sameIdentityAs(other: T): Boolean
}
