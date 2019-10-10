package htnk128.kotlin.spring.boot.ddd.sample.core.domain

interface Identity<T : Identity<T, V>, V : Comparable<V>> : ValueObject<T> {

    val value: V
}
