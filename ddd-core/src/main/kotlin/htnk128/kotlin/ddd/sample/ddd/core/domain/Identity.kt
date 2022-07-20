package htnk128.kotlin.ddd.sample.ddd.core.domain

/**
 * 何らかのドメインを識別するIDを表現した値オブジェクトの概念。
 */
interface Identity<T : Identity<T>> : ValueObject<T> {

    fun id(): String
}
