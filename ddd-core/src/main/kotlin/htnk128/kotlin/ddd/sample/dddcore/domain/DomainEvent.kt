package htnk128.kotlin.ddd.sample.dddcore.domain

/**
 * DDDにおけるドメインイベントの概念。
 */
interface DomainEvent<T : DomainEvent<T>> {

    fun sameEventAs(other: T): Boolean
}
