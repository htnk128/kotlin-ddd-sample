package htnk128.kotlin.spring.boot.ddd.sample.address.infrastructure.persistence

import htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.address.Address
import htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.address.AddressId
import htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.address.AddressRepository
import htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.address.FullName
import htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.address.Line1
import htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.address.Line2
import htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.address.PhoneNumber
import htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.address.StateOrRegion
import htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.address.ZipCode
import htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.customer.CustomerId
import htnk128.kotlin.spring.boot.ddd.sample.shared.infrastructure.persistence.ExposedTable
import java.time.Instant
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class AddressExposedRepository : AddressRepository {

    override fun find(addressId: AddressId): Address? =
        AddressTable.select { AddressTable.addressId eq addressId.value }
            .firstOrNull()
            ?.let { AddressTable.toModel(it) }

    override fun findAll(customerId: CustomerId): List<Address> =
        AddressTable.select { AddressTable.customerId eq customerId.value }
            .map { AddressTable.toModel(it) }

    override fun add(address: Address) {
        AddressTable.insert {
            it[addressId] = address.addressId.value
            it[customerId] = address.customerId.value
            it[fullName] = address.fullName.value
            it[zipCode] = address.zipCode.value
            it[stateOrRegion] = address.stateOrRegion.value
            it[line1] = address.line1.value
            it[line2] = address.line2?.value
            it[phoneNumber] = address.phoneNumber.value
            it[createdAt] = address.createdAt
            it[deletedAt] = address.deletedAt
            it[updatedAt] = address.updatedAt
        }
    }

    override fun set(address: Address): Int =
        AddressTable.update({ AddressTable.addressId eq address.addressId.value }) {
            it[fullName] = address.fullName.value
            it[zipCode] = address.zipCode.value
            it[stateOrRegion] = address.stateOrRegion.value
            it[line1] = address.line1.value
            it[line2] = address.line2?.value
            it[phoneNumber] = address.phoneNumber.value
            it[updatedAt] = address.updatedAt
        }

    override fun remove(address: Address): Int =
        AddressTable.update({ AddressTable.addressId eq address.addressId.value }) {
            it[deletedAt] = address.deletedAt
            it[updatedAt] = address.updatedAt
        }
}

private object AddressTable : ExposedTable<Address>("address") {

    val addressId: Column<String> = varchar("address_id", length = 64).primaryKey()
    val customerId: Column<String> = varchar("customer_id", length = 64)
    val fullName: Column<String> = varchar("full_name", length = 100)
    val zipCode: Column<String> = varchar("zip_code", length = 50)
    val stateOrRegion: Column<String> = varchar("state_or_region", length = 100)
    val line1: Column<String> = varchar("line1", length = 100)
    val line2: Column<String?> = varchar("line2", length = 100).nullable()
    val phoneNumber: Column<String> = varchar("phone_number", length = 50)
    val createdAt: Column<Instant> = instant("created_at")
    val deletedAt: Column<Instant?> = instant("deleted_at").nullable()
    val updatedAt: Column<Instant> = instant("updated_at")

    override fun toModel(row: ResultRow): Address = Address(
        AddressId.valueOf(row[addressId]),
        CustomerId.valueOf(row[customerId]),
        FullName.valueOf(row[fullName]),
        ZipCode.valueOf(row[zipCode]),
        StateOrRegion.valueOf(row[stateOrRegion]),
        Line1.valueOf(row[line1]),
        row[line2]?.let { Line2.valueOf(it) },
        PhoneNumber.valueOf(row[phoneNumber]),
        row[createdAt],
        row[deletedAt],
        row[updatedAt]
    )
}
