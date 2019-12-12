package htnk128.kotlin.ddd.sample.dddcore.domain

interface DomainEvent<T : DomainEvent<T>> {

    fun sameEventAs(other: T): Boolean
}
