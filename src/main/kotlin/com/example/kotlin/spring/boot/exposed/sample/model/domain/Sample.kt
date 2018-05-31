package com.example.kotlin.spring.boot.exposed.sample.model.domain

import com.example.kotlin.spring.boot.exposed.sample.model.exposed.SampleRecord
import java.util.*

data class Sample(val id: UUID, val name: String)

fun SampleRecord.toSample(): Sample = Sample(
		id = id,
		name = name
)
