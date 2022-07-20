package htnk128.kotlin.ddd.sample.ddd.core.domain

/**
 * DDDにおけるエンティティの概念。
 */
interface Entity<T> {

    fun sameIdentityAs(other: T): Boolean
}
