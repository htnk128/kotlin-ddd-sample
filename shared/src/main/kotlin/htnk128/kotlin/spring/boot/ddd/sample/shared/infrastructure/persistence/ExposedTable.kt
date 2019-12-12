package htnk128.kotlin.spring.boot.ddd.sample.shared.infrastructure.persistence

import java.time.Instant
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

abstract class ExposedTable<out M>(name: String) : Table(name) {

    abstract fun toModel(row: ResultRow): M

    protected fun Table.instant(name: String): Column<Instant> = registerColumn(name, InstantColumnType(true))
}
