package htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model

import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.shared.ValueObject

interface Identity<T : Identity<T, V>, V : Comparable<V>> : ValueObject<T> {

    val value: V
}
