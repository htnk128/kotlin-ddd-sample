package htnk128.kotlin.spring.boot.ddd.sample.dddcore.domain

interface Identity<T : Identity<T, V>, V : Comparable<V>> :
    ValueObject<T> {

    val value: V
}
