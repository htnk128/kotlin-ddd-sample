package htnk128.kotlin.ddd.sample.customer.infrastructure.persistence

import htnk128.kotlin.ddd.sample.customer.domain.model.customer.Customer
import htnk128.kotlin.ddd.sample.customer.domain.model.customer.CustomerId
import htnk128.kotlin.ddd.sample.customer.domain.model.customer.CustomerRepository
import htnk128.kotlin.ddd.sample.customer.domain.model.customer.Email
import htnk128.kotlin.ddd.sample.customer.domain.model.customer.Name
import htnk128.kotlin.ddd.sample.customer.domain.model.customer.NamePronunciation
import htnk128.kotlin.ddd.sample.shared.infrastructure.persistence.ExposedTable
import java.time.Instant
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class CustomerExposedRepository : CustomerRepository {

    override fun find(customerId: CustomerId, lock: Boolean): Customer? =
        CustomerTable.select { CustomerTable.customerId eq customerId.value and CustomerTable.deletedAt.isNull() }
            .run { if (lock) this.forUpdate() else this }
            .firstOrNull()
            ?.rowToModel()

    override fun findAll(limit: Int, offset: Int): List<Customer> =
        CustomerTable.select { CustomerTable.deletedAt.isNull() }
            .limit(limit, offset)
            .map { it.rowToModel() }

    override fun count(): Int =
        CustomerTable.select { CustomerTable.deletedAt.isNull() }
            .count()

    override fun add(customer: Customer) {
        CustomerTable.insert {
            it[customerId] = customer.customerId.value
            it[name] = customer.name.value
            it[namePronunciation] = customer.namePronunciation.value
            it[email] = customer.email.value
            it[createdAt] = customer.createdAt
            it[deletedAt] = customer.deletedAt
            it[updatedAt] = customer.updatedAt
        }
    }

    override fun set(customer: Customer): Int =
        CustomerTable.update({ CustomerTable.customerId eq customer.customerId.value and CustomerTable.deletedAt.isNull() }) {
            it[name] = customer.name.value
            it[namePronunciation] = customer.namePronunciation.value
            it[email] = customer.email.value
            it[updatedAt] = customer.updatedAt
        }

    override fun remove(customer: Customer): Int =
        CustomerTable.update({ CustomerTable.customerId eq customer.customerId.value and CustomerTable.deletedAt.isNull() }) {
            it[deletedAt] = customer.deletedAt
            it[updatedAt] = customer.updatedAt
        }

    private fun ResultRow.rowToModel(): Customer =
        Customer(
            CustomerId.valueOf(this[CustomerTable.customerId]),
            Name.valueOf(this[CustomerTable.name]),
            NamePronunciation.valueOf(this[CustomerTable.namePronunciation]),
            Email.valueOf(this[CustomerTable.email]),
            this[CustomerTable.createdAt],
            this[CustomerTable.deletedAt],
            this[CustomerTable.updatedAt]
        )
}

private object CustomerTable : ExposedTable<Customer>("customer") {

    val customerId: Column<String> = varchar("customer_id", length = 64).primaryKey()
    val name: Column<String> = varchar("name", length = 100)
    val namePronunciation: Column<String> = varchar("name_pronunciation", length = 100)
    val email: Column<String> = varchar("email", length = 100)
    val createdAt: Column<Instant> = instant("created_at")
    val deletedAt: Column<Instant?> = instant("deleted_at").nullable()
    val updatedAt: Column<Instant> = instant("updated_at")
}
