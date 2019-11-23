package htnk128.kotlin.spring.boot.ddd.sample.core.domain

interface DomainEvent<T : DomainEvent<T>> {

    fun sameEventAs(other: T): Boolean
}
