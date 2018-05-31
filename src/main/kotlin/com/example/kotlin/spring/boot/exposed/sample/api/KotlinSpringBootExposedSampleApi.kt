package com.example.kotlin.spring.boot.exposed.sample.api

import com.example.kotlin.spring.boot.exposed.sample.model.domain.*
import com.example.kotlin.spring.boot.exposed.sample.model.exposed.SampleRecord
import com.example.kotlin.spring.boot.exposed.sample.repository.SampleRepository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class KotlinSpringBootExposedSampleApi(private val sampleRepository: SampleRepository) {

    @GetMapping("")
    fun findAll(): List<Sample> = sampleRepository.findAll().map { it.toSample() }

    @PostMapping("/create", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun create(@ModelAttribute req: CreateSampleRequest): Sample =
            req.toRecord(UUID.randomUUID())
                    .let { sampleRepository.insert(it) }
                    .also { println("INSERT DB ENTITY: $it") }
                    .toSample()
}

data class CreateSampleRequest(val name: String)

fun CreateSampleRequest.toRecord(id: UUID): SampleRecord = SampleRecord(id = id, name = name)

