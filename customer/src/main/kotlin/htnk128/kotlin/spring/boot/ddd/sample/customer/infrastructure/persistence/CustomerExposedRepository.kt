package htnk128.kotlin.spring.boot.ddd.sample.customer.infrastructure.persistence

import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer.Customer
import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer.CustomerIdentity
import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer.CustomerRepository
import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer.Name
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
class CustomerExposedRepository :
    CustomerRepository {

    override fun find(customerId: CustomerIdentity): Customer? =
        CustomerTable.select { CustomerTable.customerId eq customerId.value }
            .map { it.toCustomerRecord() }
            .firstOrNull()

    override fun findAll(): List<Customer> = CustomerTable.selectAll().map { it.toCustomerRecord() }

    override fun create(customer: Customer) {
        CustomerTable.insert {
            it[customerId] = customer.customerId.value
            it[name] = customer.name.value
        }
    }

    override fun update(customer: Customer): Int =
        CustomerTable.update({ CustomerTable.customerId eq customer.customerId.value }) {
            it[name] = customer.name.value
        }
}

object CustomerTable : Table("customer") {

    val customerId: Column<String> = varchar("customer_id", length = 64).primaryKey()
    val name: Column<String> = varchar("name", length = 100)

    fun rowToModel(row: ResultRow): Customer =
        Customer(
            CustomerIdentity(row[customerId]),
            Name(row[name])
        )
}

private fun ResultRow.toCustomerRecord() =
    CustomerTable.rowToModel(this)
