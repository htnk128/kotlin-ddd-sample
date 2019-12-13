package htnk128.kotlin.ddd.sample.shared.infrastructure.persistence

import java.time.Instant
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

abstract class ExposedTable<out M>(name: String) : Table(name) {

    protected fun instant(name: String): Column<Instant> = registerColumn(name, InstantColumnType(true))
}
