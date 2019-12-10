package htnk128.kotlin.spring.boot.ddd.sample.customer.infrastructure.persistence

import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer.Customer
import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer.CustomerId
import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer.CustomerRepository
import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer.Email
import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer.Name
import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer.NamePronunciation
import java.time.Instant
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
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
            ?.rowToModel()

    override fun findAll(): List<Customer> =
        CustomerTable.selectAll()
            .map { it.rowToModel() }

    override fun add(customer: Customer) {
        CustomerTable.insert {
            it[customerId] = customer.customerId.value
            it[name] = customer.name.value
            it[namePronunciation] = customer.namePronunciation.value
            it[email] = customer.email.value
            it[createdAt] = customer.createdAt.toEpochMilli()
            it[deletedAt] = customer.deletedAt?.toEpochMilli()
            it[updatedAt] = customer.updatedAt.toEpochMilli()
        }
    }

    override fun set(customer: Customer): Int =
        CustomerTable.update({ CustomerTable.customerId eq customer.customerId.value }) {
            it[name] = customer.name.value
            it[namePronunciation] = customer.namePronunciation.value
            it[email] = customer.email.value
            it[updatedAt] = customer.updatedAt.toEpochMilli()
        }

    override fun remove(customer: Customer): Int =
        CustomerTable.update({ CustomerTable.customerId eq customer.customerId.value }) {
            it[deletedAt] = customer.deletedAt?.toEpochMilli()
            it[updatedAt] = customer.updatedAt.toEpochMilli()
        }
}

private object CustomerTable : Table("customer") {

    val customerId: Column<String> = varchar("customer_id", length = 64).primaryKey()
    val name: Column<String> = varchar("name", length = 100)
    val namePronunciation: Column<String> = varchar("name_pronunciation", length = 100)
    val email: Column<String> = varchar("email", length = 100)
    val createdAt: Column<Long> = long("created_at")
    val deletedAt: Column<Long?> = long("deleted_at").nullable()
    val updatedAt: Column<Long> = long("updated_at")
}

private fun ResultRow.rowToModel() =
    Customer(
        CustomerId.valueOf(this[CustomerTable.customerId]),
        Name.valueOf(this[CustomerTable.name]),
        NamePronunciation.valueOf(this[CustomerTable.namePronunciation]),
        Email.valueOf(this[CustomerTable.email]),
        Instant.ofEpochMilli(this[CustomerTable.createdAt]),
        this[CustomerTable.deletedAt]?.let(Instant::ofEpochMilli),
        Instant.ofEpochMilli(this[CustomerTable.updatedAt])
    )
