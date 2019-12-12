package htnk128.kotlin.spring.boot.ddd.sample.customer.infrastructure.persistence

import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer.Customer
import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer.CustomerId
import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer.CustomerRepository
import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer.Email
import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer.Name
import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer.NamePronunciation
import htnk128.kotlin.spring.boot.ddd.sample.shared.infrastructure.persistence.ExposedTable
import java.time.Instant
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class CustomerExposedRepository : CustomerRepository {

    override fun find(customerId: CustomerId): Customer? =
        CustomerTable.select { CustomerTable.customerId eq customerId.value }
            .firstOrNull()
            ?.let { CustomerTable.toModel(it) }

    override fun findAll(): List<Customer> =
        CustomerTable.selectAll()
            .map { CustomerTable.toModel(it) }

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
        CustomerTable.update({ CustomerTable.customerId eq customer.customerId.value }) {
            it[name] = customer.name.value
            it[namePronunciation] = customer.namePronunciation.value
            it[email] = customer.email.value
            it[updatedAt] = customer.updatedAt
        }

    override fun remove(customer: Customer): Int =
        CustomerTable.update({ CustomerTable.customerId eq customer.customerId.value }) {
            it[deletedAt] = customer.deletedAt
            it[updatedAt] = customer.updatedAt
        }
}

private object CustomerTable : ExposedTable<Customer>("customer") {

    val customerId: Column<String> = varchar("customer_id", length = 64).primaryKey()
    val name: Column<String> = varchar("name", length = 100)
    val namePronunciation: Column<String> = varchar("name_pronunciation", length = 100)
    val email: Column<String> = varchar("email", length = 100)
    val createdAt: Column<Instant> = instant("created_at")
    val deletedAt: Column<Instant?> = instant("deleted_at").nullable()
    val updatedAt: Column<Instant> = instant("updated_at")

    override fun toModel(row: ResultRow): Customer = Customer(
        CustomerId.valueOf(row[customerId]),
        Name.valueOf(row[name]),
        NamePronunciation.valueOf(row[namePronunciation]),
        Email.valueOf(row[email]),
        row[createdAt],
        row[deletedAt],
        row[updatedAt]
    )
}
