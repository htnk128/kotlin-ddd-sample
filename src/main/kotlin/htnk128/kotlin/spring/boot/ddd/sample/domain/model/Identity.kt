package htnk128.kotlin.spring.boot.ddd.sample.domain.model

import htnk128.kotlin.spring.boot.ddd.sample.domain.shared.ValueObject

interface Identity<T : Identity<T, V>, V : Comparable<V>> : ValueObject<T> {

    val value: V
}
