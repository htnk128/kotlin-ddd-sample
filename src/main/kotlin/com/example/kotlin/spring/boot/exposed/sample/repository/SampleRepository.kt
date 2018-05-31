package com.example.kotlin.spring.boot.exposed.sample.repository

import com.example.kotlin.spring.boot.exposed.sample.model.exposed.SampleRecord
import com.example.kotlin.spring.boot.exposed.sample.model.exposed.SampleTable
import com.example.kotlin.spring.boot.exposed.sample.model.exposed.SampleTable.id
import com.example.kotlin.spring.boot.exposed.sample.model.exposed.rowToSampleRecord
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@Transactional
class SampleRepository {

	fun insert(record: SampleRecord): SampleRecord {
		try {
			SampleTable.insert({
				it[id] = record.id
				it[name] = record.name
			})
		} catch (all: Throwable) {
			all.printStackTrace()

			throw all
		}
		return record
	}

	fun update(record: SampleRecord): SampleRecord {
		SampleTable.update({ id eq record.id }) {
			it[name] = record.name
		}
		return record
	}

	fun findAll() = SampleTable.selectAll().map { it.toSampleRecord() }

	fun findOneById(id: UUID): SampleRecord? =
			SampleTable.select { SampleTable.id eq id }
					.limit(1)
					.map { it.toSampleRecord() }
					.firstOrNull()

	operator fun get(id: UUID): SampleRecord = findOneById(id)
			?: throw RuntimeException("SampleRecord NOT FOUND ! (id=$id)")

}

private fun ResultRow.toSampleRecord() = SampleTable.rowToSampleRecord(this)
