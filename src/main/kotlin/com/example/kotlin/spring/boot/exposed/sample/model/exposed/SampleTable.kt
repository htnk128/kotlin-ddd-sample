package com.example.kotlin.spring.boot.exposed.sample.model.exposed

import org.jetbrains.exposed.sql.*
import java.util.*

object SampleTable : Table("sample") {
	val id = uuid("id").primaryKey()
	val name = varchar("name", length = 100)
}

data class SampleRecord(val id: UUID, val name: String)

fun SampleTable.rowToSampleRecord(row: ResultRow): SampleRecord = SampleRecord(
		id = row[id],
		name = row[name]
)
