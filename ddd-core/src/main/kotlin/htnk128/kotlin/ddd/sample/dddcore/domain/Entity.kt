package htnk128.kotlin.ddd.sample.dddcore.domain

interface Entity<T> {

    fun sameIdentityAs(other: T): Boolean
}
