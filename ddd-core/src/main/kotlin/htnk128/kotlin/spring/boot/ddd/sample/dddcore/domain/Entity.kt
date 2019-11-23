package htnk128.kotlin.spring.boot.ddd.sample.dddcore.domain

interface Entity<T> {

    fun sameIdentityAs(other: T): Boolean
}
