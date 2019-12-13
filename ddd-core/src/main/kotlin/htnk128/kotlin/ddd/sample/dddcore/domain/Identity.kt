package htnk128.kotlin.ddd.sample.dddcore.domain

/**
 * 何らかのドメインを識別するIDを表現した値オブジェクトの概念。
 */
interface Identity<T : Identity<T, V>, V : Comparable<V>> : ValueObject<T> {

    val value: V
}
