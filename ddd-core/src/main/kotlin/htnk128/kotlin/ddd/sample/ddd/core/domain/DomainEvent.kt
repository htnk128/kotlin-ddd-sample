package htnk128.kotlin.ddd.sample.ddd.core.domain

import java.time.Instant

/**
 * DDDにおけるドメインイベントの概念。
 */
interface DomainEvent<T : DomainEvent<T>> {

    val occurredOn: Instant

    fun sameEventAs(other: T): Boolean
}
