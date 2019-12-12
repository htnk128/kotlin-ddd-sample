package htnk128.kotlin.spring.boot.ddd.sample.address.application.service

import htnk128.kotlin.spring.boot.ddd.sample.address.application.dto.AddressDTO
import htnk128.kotlin.spring.boot.ddd.sample.address.application.exception.AddressNotFoundException
import htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.address.Address
import htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.address.AddressId
import htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.address.AddressRepository
import htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.address.StateOrRegion
import htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.address.FullName
import htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.address.PhoneNumber
import htnk128.kotlin.spring.boot.ddd.sample.shared.UnexpectedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AddressService(private val addressRepository: AddressRepository) {

    @Transactional(readOnly = true)
    fun find(aAddressId: String): AddressDTO {
        val addressId = AddressId.valueOf(aAddressId)

        return addressRepository.find(addressId)
            ?.takeUnless { it.isDeleted }
            ?.toDTO()
            ?: throw AddressNotFoundException(addressId)
    }

    @Transactional(readOnly = true)
    fun findAll(): List<AddressDTO> = addressRepository.findAll()
        .asSequence()
        .filterNot { it.isDeleted }
        .map { it.toDTO() }
        .toList()

    @Transactional(timeout = 10, rollbackFor = [Exception::class])
    fun create(aName: String, aNamePronunciation: String, aEmail: String): AddressDTO = Address
        .create(
            addressRepository.nextAddressId(),
            FullName.valueOf(aName),
            PhoneNumber.valueOf(aNamePronunciation),
            StateOrRegion.valueOf(aEmail)
        )
        .also(addressRepository::add)
        .toDTO()

    @Transactional(timeout = 10, rollbackFor = [Exception::class])
    fun update(aAddressId: String, aName: String?, aNamePronunciation: String?, aEmail: String?): AddressDTO {
        val addressId = AddressId.valueOf(aAddressId)
        val name = aName?.let { FullName.valueOf(it) }
        val namePronunciation = aNamePronunciation?.let { PhoneNumber.valueOf(it) }
        val email = aEmail?.let { StateOrRegion.valueOf(it) }

        return addressRepository
            .find(addressId)
            ?.update(name, namePronunciation, email)
            ?.also { address ->
                addressRepository.set(address)
                    .takeIf { it > 0 }
                    ?: throw UnexpectedException("address update failed.")
            }
            ?.toDTO()
            ?: throw AddressNotFoundException(addressId)
    }

    @Transactional(timeout = 10, rollbackFor = [Exception::class])
    fun delete(aAddressId: String) {
        val addressId = AddressId.valueOf(aAddressId)
        addressRepository
            .find(addressId)
            ?.delete()
            ?.also { address ->
                addressRepository.remove(address)
                    .takeIf { it > 0 }
                    ?: throw UnexpectedException("address update failed.")
            }
            ?: throw AddressNotFoundException(addressId)
    }

    private fun Address.toDTO(): AddressDTO =
        AddressDTO(
            addressId.value,
            fullName.value,
            namePronunciation.value,
            email.value,
            createdAt.toEpochMilli(),
            deletedAt?.toEpochMilli(),
            updatedAt.toEpochMilli()
        )
}
