package htnk128.kotlin.ddd.sample.address.application.service

import htnk128.kotlin.ddd.sample.address.application.dto.AddressDTO
import htnk128.kotlin.ddd.sample.address.application.exception.AddressNotFoundException
import htnk128.kotlin.ddd.sample.address.application.exception.CustomerNotFoundException
import htnk128.kotlin.ddd.sample.address.domain.model.address.Address
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressId
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressRepository
import htnk128.kotlin.ddd.sample.address.domain.model.address.FullName
import htnk128.kotlin.ddd.sample.address.domain.model.address.Line1
import htnk128.kotlin.ddd.sample.address.domain.model.address.Line2
import htnk128.kotlin.ddd.sample.address.domain.model.address.PhoneNumber
import htnk128.kotlin.ddd.sample.address.domain.model.address.StateOrRegion
import htnk128.kotlin.ddd.sample.address.domain.model.address.ZipCode
import htnk128.kotlin.ddd.sample.address.domain.model.customer.CustomerId
import htnk128.kotlin.ddd.sample.address.domain.model.customer.CustomerRepository
import htnk128.kotlin.ddd.sample.shared.UnexpectedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 住所([Address])ドメインの操作を提供するアプリケーションサービス。
 */
@Service
class AddressService(
    private val addressRepository: AddressRepository,
    private val customerRepository: CustomerRepository
) {

    @Transactional(readOnly = true)
    fun find(aAddressId: String): AddressDTO {
        val addressId = AddressId.valueOf(aAddressId)

        return addressRepository.find(addressId)
            ?.takeUnless { it.isDeleted }
            ?.toDTO()
            ?: throw AddressNotFoundException(addressId)
    }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT, rollbackFor = [Exception::class])
    fun lock(addressId: AddressId): Address = addressRepository.find(addressId, lock = true)
        ?.takeUnless { it.isDeleted }
        ?: throw AddressNotFoundException(addressId)

    @Transactional(readOnly = true)
    fun findAll(aCustomerId: String): List<AddressDTO> = addressRepository
        .findAll(CustomerId.valueOf(aCustomerId))
        .asSequence()
        .filterNot { it.isDeleted }
        .map { it.toDTO() }
        .toList()

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT, rollbackFor = [Exception::class])
    fun create(
        aCustomerId: String,
        aFullName: String,
        aZipCode: String,
        aStateOrRegion: String,
        aLine1: String,
        aLine2: String?,
        aPhoneNumber: String
    ): AddressDTO {
        val customerId = CustomerId.valueOf(aCustomerId)
        val fullName = FullName.valueOf(aFullName)
        val zipCode = ZipCode.valueOf(aZipCode)
        val stateOrRegion = StateOrRegion.valueOf(aStateOrRegion)
        val line1 = Line1.valueOf(aLine1)
        val line2 = aLine2?.let { Line2.valueOf(it) }
        val phoneNumber = PhoneNumber.valueOf(aPhoneNumber)

        val customer = customerRepository.find(customerId) ?: throw CustomerNotFoundException(customerId)

        return Address
            .create(
                addressRepository.nextAddressId(),
                customer.customerId,
                fullName,
                zipCode,
                stateOrRegion,
                line1,
                line2,
                phoneNumber
            )
            .also(addressRepository::add)
            .toDTO()
    }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT, rollbackFor = [Exception::class])
    fun update(
        aAddressId: String,
        aFullName: String?,
        aZipCode: String?,
        aStateOrRegion: String?,
        aLine1: String?,
        aLine2: String?,
        aPhoneNumber: String?
    ): AddressDTO {
        val addressId = AddressId.valueOf(aAddressId)
        val fullName = aFullName?.let { FullName.valueOf(it) }
        val zipCode = aZipCode?.let { ZipCode.valueOf(it) }
        val stateOrRegion = aStateOrRegion?.let { StateOrRegion.valueOf(it) }
        val line1 = aLine1?.let { Line1.valueOf(it) }
        val line2 = aLine2?.let { Line2.valueOf(it) }
        val phoneNumber = aPhoneNumber?.let { PhoneNumber.valueOf(it) }

        return lock(addressId)
            .update(fullName, zipCode, stateOrRegion, line1, line2, phoneNumber)
            .also { address ->
                addressRepository.set(address)
                    .takeIf { it > 0 }
                    ?: throw UnexpectedException("Address update failed.")
            }
            .toDTO()
    }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT, rollbackFor = [Exception::class])
    fun delete(aAddressId: String) {
        val addressId = AddressId.valueOf(aAddressId)

        lock(addressId)
            .delete()
            .also { address ->
                addressRepository.remove(address)
                    .takeIf { it > 0 }
                    ?: throw UnexpectedException("Address update failed.")
            }
    }

    private fun Address.toDTO(): AddressDTO =
        AddressDTO(
            addressId.value,
            customerId.value,
            fullName.value,
            zipCode.value,
            stateOrRegion.value,
            line1.value,
            line2?.value,
            phoneNumber.value,
            createdAt.toEpochMilli(),
            deletedAt?.toEpochMilli(),
            updatedAt.toEpochMilli()
        )

    private companion object {

        const val TRANSACTIONAL_TIMEOUT: Int = 30
    }
}
