package htnk128.kotlin.spring.boot.ddd.sample.contacts.infrastructure.persistence

import htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.contactdetails.ContactDetails
import htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.contactdetails.ContactDetailsIdentity
import htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.contactdetails.ContactDetailsRepository
import htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.contactdetails.CustomerIdentity
import htnk128.kotlin.spring.boot.ddd.sample.contacts.doman.model.contactdetails.TelephoneNumber
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
class ContactDetailsExposedRepository : ContactDetailsRepository {

    override fun find(contactDetailsId: ContactDetailsIdentity): ContactDetails? =
        ContactDetailsTable.select { ContactDetailsTable.contactDetailsId eq contactDetailsId.value }
            .map { it.toContactDetailsRecord() }
            .firstOrNull()

    override fun findAll(): List<ContactDetails> = ContactDetailsTable.selectAll().map { it.toContactDetailsRecord() }

    override fun create(contactDetails: ContactDetails) {
        ContactDetailsTable.insert {
            it[contactDetailsId] = contactDetails.contactDetailsId.value
            it[customerId] = contactDetails.customerId.value
            it[telephoneNumber] = contactDetails.telephoneNumber.value
        }
    }

    override fun update(contactDetails: ContactDetails): Int =
        ContactDetailsTable.update({ ContactDetailsTable.contactDetailsId eq contactDetails.contactDetailsId.value }) {
            it[telephoneNumber] = contactDetails.telephoneNumber.value
        }
}

object ContactDetailsTable : Table("contact_details") {

    val contactDetailsId: Column<String> = varchar("contact_details_id", length = 64).primaryKey()
    val customerId: Column<String> = varchar("customer_id", length = 64)
    val telephoneNumber: Column<String> = varchar("telephone_number", length = 50)

    fun rowToModel(row: ResultRow): ContactDetails =
        ContactDetails(
            ContactDetailsIdentity(row[contactDetailsId]),
            CustomerIdentity(row[customerId]),
            TelephoneNumber(row[telephoneNumber])
        )
}

private fun ResultRow.toContactDetailsRecord() =
    ContactDetailsTable.rowToModel(this)
