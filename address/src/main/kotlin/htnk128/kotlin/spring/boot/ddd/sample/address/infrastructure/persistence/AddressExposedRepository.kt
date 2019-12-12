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
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Repository
@Transactional
class AddressExposedRepository : AddressRepository {

    override fun find(addressId: AddressId): Address? =
        AddressTable.select { AddressTable.addressId eq addressId.value }
            .firstOrNull()
            ?.rowToModel()

    override fun findAll(customerId: CustomerId): List<Address> =
        AddressTable.select { AddressTable.customerId eq customerId.value }
            .map { it.rowToModel() }

    override fun add(address: Address) {
        AddressTable.insert {
            it[addressId] = address.addressId.value
            it[fullName] = address.fullName.value
            it[zipCode] = address.zipCode.value
            it[stateOrRegion] = address.stateOrRegion.value
            it[line1] = address.line1.value
            it[line2] = address.line2?.value
            it[phoneNumber] = address.phoneNumber.value.toInt()
            it[createdAt] = address.createdAt.toEpochMilli()
            it[deletedAt] = address.deletedAt?.toEpochMilli()
            it[updatedAt] = address.updatedAt.toEpochMilli()
        }
    }

    override fun set(address: Address): Int =
        AddressTable.update({ AddressTable.addressId eq address.addressId.value }) {
            it[fullName] = address.fullName.value
            it[zipCode] = address.zipCode.value
            it[stateOrRegion] = address.stateOrRegion.value
            it[line1] = address.line1.value
            it[line2] = address.line2?.value
            it[phoneNumber] = address.phoneNumber.value.toInt()
            it[updatedAt] = address.updatedAt.toEpochMilli()
        }

    override fun remove(address: Address): Int =
        AddressTable.update({ AddressTable.addressId eq address.addressId.value }) {
            it[deletedAt] = address.deletedAt?.toEpochMilli()
            it[updatedAt] = address.updatedAt.toEpochMilli()
        }
}

private object AddressTable : Table("address") {

    val addressId: Column<String> = varchar("address_id", length = 64).primaryKey()
    val customerId: Column<String> = varchar("customer_id", length = 64)
    val fullName: Column<String> = varchar("full_name", length = 100)
    val zipCode: Column<String> = varchar("zip_code", length = 50)
    val stateOrRegion: Column<String> = varchar("state_or_region", length = 100)
    val line1: Column<String> = varchar("line1", length = 100)
    val line2: Column<String?> = varchar("line2", length = 100).nullable()
    val phoneNumber: Column<Int> = integer("phone_number")
    val createdAt: Column<Long> = long("created_at")
    val deletedAt: Column<Long?> = long("deleted_at").nullable()
    val updatedAt: Column<Long> = long("updated_at")
}

private fun ResultRow.rowToModel() =
    Address(
        AddressId.valueOf(this[AddressTable.addressId]),
        CustomerId.valueOf(this[AddressTable.customerId]),
        FullName.valueOf(this[AddressTable.fullName]),
        ZipCode.valueOf(this[AddressTable.zipCode]),
        StateOrRegion.valueOf(this[AddressTable.stateOrRegion]),
        Line1.valueOf(this[AddressTable.line1]),
        this[AddressTable.line2]?.let { Line2.valueOf(it) },
        PhoneNumber.valueOf(this[AddressTable.phoneNumber]),
        Instant.ofEpochMilli(this[AddressTable.createdAt]),
        this[AddressTable.deletedAt]?.let(Instant::ofEpochMilli),
        Instant.ofEpochMilli(this[AddressTable.updatedAt])
    )
