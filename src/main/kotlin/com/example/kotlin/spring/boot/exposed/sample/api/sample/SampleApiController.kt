package com.example.kotlin.spring.boot.exposed.sample.api.sample

import com.example.kotlin.spring.boot.exposed.sample.model.domain.Sample
import com.example.kotlin.spring.boot.exposed.sample.model.domain.toSample
import com.example.kotlin.spring.boot.exposed.sample.model.exposed.SampleRecord
import com.example.kotlin.spring.boot.exposed.sample.repository.SampleRepository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/sample")
class SampleApiController(private val sampleRepository: SampleRepository) {

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

