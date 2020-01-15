package htnk128.kotlin.ddd.sample.address.adapter.persistence

import htnk128.kotlin.ddd.sample.address.domain.model.address.Address
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressId
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressNotFoundException
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressOwnerId
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressRepository
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressUpdateFailedException
import htnk128.kotlin.ddd.sample.address.domain.model.address.FullName
import htnk128.kotlin.ddd.sample.address.domain.model.address.Line1
import htnk128.kotlin.ddd.sample.address.domain.model.address.Line2
import htnk128.kotlin.ddd.sample.address.domain.model.address.PhoneNumber
import htnk128.kotlin.ddd.sample.address.domain.model.address.StateOrRegion
import htnk128.kotlin.ddd.sample.address.domain.model.address.ZipCode
import htnk128.kotlin.ddd.sample.shared.adapter.persistence.ExposedTable
import java.time.Instant
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository

@Repository
class AddressExposedRepository : AddressRepository {

    override fun find(addressId: AddressId, lock: Boolean): Address =
        AddressTable.select { AddressTable.addressId eq addressId.id() }
            .run { if (lock) this.forUpdate() else this }
            .firstOrNull()
            ?.rowToModel()
            ?: throw AddressNotFoundException(addressId)

    override fun findAll(addressOwnerId: AddressOwnerId): List<Address> =
        AddressTable.select { AddressTable.addressOwnerId eq addressOwnerId.id() }
            .map { it.rowToModel() }

    override fun add(address: Address) {
        AddressTable.insert {
            it[addressId] = address.addressId.id()
            it[addressOwnerId] = address.addressOwnerId.id()
            it[fullName] = address.fullName.value()
            it[zipCode] = address.zipCode.value()
            it[stateOrRegion] = address.stateOrRegion.value()
            it[line1] = address.line1.value()
            it[line2] = address.line2?.value()
            it[phoneNumber] = address.phoneNumber.value()
            it[createdAt] = address.createdAt
            it[deletedAt] = address.deletedAt
            it[updatedAt] = address.updatedAt
        }
    }

    override fun set(address: Address) {
        AddressTable.update({ AddressTable.addressId eq address.addressId.id() }) {
            it[fullName] = address.fullName.value()
            it[zipCode] = address.zipCode.value()
            it[stateOrRegion] = address.stateOrRegion.value()
            it[line1] = address.line1.value()
            it[line2] = address.line2?.value()
            it[phoneNumber] = address.phoneNumber.value()
            it[updatedAt] = address.updatedAt
        }
            .takeIf { it > 0 }
            ?: throw AddressUpdateFailedException(address.addressId)
    }

    override fun remove(address: Address) {
        AddressTable.update({ AddressTable.addressId eq address.addressId.id() }) {
            it[deletedAt] = address.deletedAt
            it[updatedAt] = address.updatedAt
        }
            .takeIf { it > 0 }
            ?: throw AddressUpdateFailedException(address.addressId)
    }

    private fun ResultRow.rowToModel() =
        Address(
            AddressId.valueOf(this[AddressTable.addressId]),
            AddressOwnerId.valueOf(this[AddressTable.addressOwnerId]),
            FullName.valueOf(this[AddressTable.fullName]),
            ZipCode.valueOf(this[AddressTable.zipCode]),
            StateOrRegion.valueOf(this[AddressTable.stateOrRegion]),
            Line1.valueOf(this[AddressTable.line1]),
            this[AddressTable.line2]?.let { Line2.valueOf(it) },
            PhoneNumber.valueOf(this[AddressTable.phoneNumber]),
            this[AddressTable.createdAt],
            this[AddressTable.deletedAt],
            this[AddressTable.updatedAt]
        )
}

private object AddressTable : ExposedTable<Address>("address") {

    val addressId: Column<String> = varchar("address_id", length = 64).primaryKey()
    val addressOwnerId: Column<String> = varchar("address_owner_id", length = 64)
    val fullName: Column<String> = varchar("full_name", length = 100)
    val zipCode: Column<String> = varchar("zip_code", length = 50)
    val stateOrRegion: Column<String> = varchar("state_or_region", length = 100)
    val line1: Column<String> = varchar("line1", length = 100)
    val line2: Column<String?> = varchar("line2", length = 100).nullable()
    val phoneNumber: Column<String> = varchar("phone_number", length = 50)
    val createdAt: Column<Instant> = instant("created_at")
    val deletedAt: Column<Instant?> = instant("deleted_at").nullable()
    val updatedAt: Column<Instant> = instant("updated_at")
}
