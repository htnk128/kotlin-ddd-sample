package htnk128.kotlin.ddd.sample.address.infrastructure.persistence

import htnk128.kotlin.ddd.sample.address.domain.model.address.AccountId
import htnk128.kotlin.ddd.sample.address.domain.model.address.Address
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressId
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressNotFoundException
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressRepository
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressUpdateFailedException
import htnk128.kotlin.ddd.sample.address.domain.model.address.FullName
import htnk128.kotlin.ddd.sample.address.domain.model.address.Line1
import htnk128.kotlin.ddd.sample.address.domain.model.address.Line2
import htnk128.kotlin.ddd.sample.address.domain.model.address.PhoneNumber
import htnk128.kotlin.ddd.sample.address.domain.model.address.StateOrRegion
import htnk128.kotlin.ddd.sample.address.domain.model.address.ZipCode
import htnk128.kotlin.ddd.sample.shared.infrastructure.persistence.ExposedTable
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

    override fun find(addressId: AddressId, lock: Boolean): Address =
        AddressTable.select { AddressTable.addressId eq addressId.id() }
            .run { if (lock) this.forUpdate() else this }
            .firstOrNull()
            ?.rowToModel()
            ?: throw AddressNotFoundException(addressId)

    override fun findAll(accountId: AccountId): List<Address> =
        AddressTable.select { AddressTable.accountId eq accountId.id() }
            .map { it.rowToModel() }

    override fun add(address: Address) {
        AddressTable.insert {
            it[addressId] = address.addressId.id()
            it[accountId] = address.accountId.id()
            it[fullName] = address.fullName.toValue()
            it[zipCode] = address.zipCode.toValue()
            it[stateOrRegion] = address.stateOrRegion.toValue()
            it[line1] = address.line1.toValue()
            it[line2] = address.line2?.toValue()
            it[phoneNumber] = address.phoneNumber.toValue()
            it[createdAt] = address.createdAt
            it[deletedAt] = address.deletedAt
            it[updatedAt] = address.updatedAt
        }
    }

    override fun set(address: Address) {
        AddressTable.update({ AddressTable.addressId eq address.addressId.id() }) {
            it[fullName] = address.fullName.toValue()
            it[zipCode] = address.zipCode.toValue()
            it[stateOrRegion] = address.stateOrRegion.toValue()
            it[line1] = address.line1.toValue()
            it[line2] = address.line2?.toValue()
            it[phoneNumber] = address.phoneNumber.toValue()
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
            AccountId.valueOf(this[AddressTable.accountId]),
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
    val accountId: Column<String> = varchar("account_id", length = 64)
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
