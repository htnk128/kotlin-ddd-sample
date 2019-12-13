package htnk128.kotlin.ddd.sample.dddcore.domain

/**
 * DDDにおける値オブジェクトの概念。
 *
 * @param T 値オブジェクトの型
 */
interface ValueObject<T> {

    fun sameValueAs(other: T): Boolean
}
