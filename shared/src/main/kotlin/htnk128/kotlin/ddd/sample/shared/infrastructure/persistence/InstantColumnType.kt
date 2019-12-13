package htnk128.kotlin.ddd.sample.shared.infrastructure.persistence

import java.time.Instant
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.DateColumnType
import org.joda.time.DateTime

class InstantColumnType(time: Boolean) : ColumnType() {
    private val delegate = DateColumnType(time)

    override fun sqlType(): String = delegate.sqlType()

    override fun nonNullValueToString(value: Any): String = when (value) {
        is Instant -> delegate.nonNullValueToString(value.toDateTime())
        else -> delegate.nonNullValueToString(value)
    }

    override fun valueFromDB(value: Any): Any {
        val fromDb = when (value) {
            is Instant -> delegate.valueFromDB(value.toDateTime())
            else -> delegate.valueFromDB(value)
        }
        return when (fromDb) {
            is DateTime -> Instant.ofEpochMilli(fromDb.millis)
            else -> error("failed to convert value to Instant")
        }
    }

    override fun notNullValueToDB(value: Any): Any = when (value) {
        is Instant -> delegate.notNullValueToDB(value.toDateTime())
        else -> delegate.notNullValueToDB(value)
    }

    private fun Instant.toDateTime() = DateTime(this.toEpochMilli())
}
