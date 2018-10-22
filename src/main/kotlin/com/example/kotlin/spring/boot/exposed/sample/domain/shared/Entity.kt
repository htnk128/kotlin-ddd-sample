package com.example.kotlin.spring.boot.exposed.sample.domain.shared

interface Entity<T> {

    fun sameIdentityAs(other: T): Boolean
}
