package htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model

import htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.shared.ValueObject

interface Identity<T : Identity<T, V>, V : Comparable<V>> : ValueObject<T> {

    val value: V
}
