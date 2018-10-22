package com.example.kotlin.spring.boot.exposed.sample.domain.model

import com.example.kotlin.spring.boot.exposed.sample.domain.shared.ValueObject

interface Identity<T : Identity<T, V>, V : Comparable<V>> : ValueObject<T> {

    val value: V
}

abstract class StringIdentity<T : StringIdentity<T>> protected constructor() : Identity<T, String> {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        @Suppress("UNCHECKED_CAST")
        other as T
        return sameValueAs(other)
    }

    override fun hashCode(): Int = value.hashCode()

    override fun toString(): String = value

    override fun sameValueAs(other: T): Boolean = value == other.value
}
