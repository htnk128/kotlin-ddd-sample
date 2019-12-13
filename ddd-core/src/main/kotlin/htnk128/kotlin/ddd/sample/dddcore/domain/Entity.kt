package htnk128.kotlin.ddd.sample.dddcore.domain

/**
 * DDDにおけるエンティティの概念。
 */
interface Entity<T> {

    fun sameIdentityAs(other: T): Boolean
}
