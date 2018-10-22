package com.example.kotlin.spring.boot.exposed.sample.domain.shared

interface ValueObject<T> {

    fun sameValueAs(other: T): Boolean
}
