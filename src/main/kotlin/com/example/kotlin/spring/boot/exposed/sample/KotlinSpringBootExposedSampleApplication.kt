package com.example.kotlin.spring.boot.exposed.sample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class KotlinSpringBootExposedSampleApplication

fun main(args: Array<String>) {
    runApplication<KotlinSpringBootExposedSampleApplication>(*args)
}
