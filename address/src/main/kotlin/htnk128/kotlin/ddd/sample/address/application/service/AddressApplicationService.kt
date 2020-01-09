package htnk128.kotlin.ddd.sample.address.application.service

import htnk128.kotlin.ddd.sample.address.application.command.CreateAddressCommand
import htnk128.kotlin.ddd.sample.address.application.command.DeleteAddressCommand
import htnk128.kotlin.ddd.sample.address.application.command.FindAddressCommand
import htnk128.kotlin.ddd.sample.address.application.command.FindAllAddressCommand
import htnk128.kotlin.ddd.sample.address.application.command.UpdateAddressCommand
import htnk128.kotlin.ddd.sample.address.application.dto.AddressDTO
import htnk128.kotlin.ddd.sample.address.domain.model.address.Address
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressId
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressOwnerId
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressOwnerNotFoundException
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressRepository
import htnk128.kotlin.ddd.sample.address.domain.model.address.FullName
import htnk128.kotlin.ddd.sample.address.domain.model.address.Line1
import htnk128.kotlin.ddd.sample.address.domain.model.address.Line2
import htnk128.kotlin.ddd.sample.address.domain.model.address.PhoneNumber
import htnk128.kotlin.ddd.sample.address.domain.model.address.StateOrRegion
import htnk128.kotlin.ddd.sample.address.domain.model.address.ZipCode
import htnk128.kotlin.ddd.sample.address.domain.service.AddressOwnerDomainService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * 住所([Address])ドメインの操作を提供するアプリケーションサービス。
 */
@Service
class AddressApplicationService(
    private val addressRepository: AddressRepository,
    private val addressOwnerDomainService: AddressOwnerDomainService
) {

    @Transactional(readOnly = true)
    fun find(command: FindAddressCommand): Mono<AddressDTO> = runCatching {
        Mono.just(addressRepository.find(AddressId.valueOf(command.addressId)).toDTO())
            .onErrorResume { Mono.error(it.error()) }
    }
        .getOrElse { Mono.error(it.error()) }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    fun lock(addressId: AddressId): Mono<Address> =
        Mono.just(addressRepository.find(addressId, lock = true))
            .onErrorResume { Mono.error(it.error()) }

    @Transactional(readOnly = true)
    fun findAll(command: FindAllAddressCommand): Flux<AddressDTO> = runCatching {
        addressOwnerDomainService.findOwner(AddressOwnerId.valueOf(command.addressOwnerId))
            .flux()
            .flatMap { _ ->
                Flux.fromIterable(
                    addressRepository
                        .findAll(AddressOwnerId.valueOf(command.addressOwnerId))
                        .map { it.toDTO() }
                )
            }
            .onErrorResume { Mono.error(it.error()) }
    }
        .getOrElse { Flux.error(it.error()) }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    fun create(command: CreateAddressCommand): Mono<AddressDTO> = runCatching {
        val addressOwnerId = AddressOwnerId.valueOf(command.addressOwnerId)
        val fullName = FullName.valueOf(command.fullName)
        val zipCode = ZipCode.valueOf(command.zipCode)
        val stateOrRegion = StateOrRegion.valueOf(command.stateOrRegion)
        val line1 = Line1.valueOf(command.line1)
        val line2 = command.line2?.let { Line2.valueOf(it) }
        val phoneNumber = PhoneNumber.valueOf(command.phoneNumber)

        return addressOwnerDomainService.findOwner(addressOwnerId)
            .map { owner ->
                if (!owner.isAvailable) throw AddressOwnerNotFoundException(owner.addressOwnerId)

                Address
                    .create(
                        addressRepository.nextAddressId(),
                        owner.addressOwnerId,
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
            .onErrorResume { Mono.error(it.error()) }
    }
        .getOrElse { Mono.error(it.error()) }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    fun update(command: UpdateAddressCommand): Mono<AddressDTO> = runCatching {
        val addressId = AddressId.valueOf(command.addressId)
        val fullName = command.fullName?.let { FullName.valueOf(it) }
        val zipCode = command.zipCode?.let { ZipCode.valueOf(it) }
        val stateOrRegion = command.stateOrRegion?.let { StateOrRegion.valueOf(it) }
        val line1 = command.line1?.let { Line1.valueOf(it) }
        val line2 = command.line2?.let { Line2.valueOf(it) }
        val phoneNumber = command.phoneNumber?.let { PhoneNumber.valueOf(it) }

        return lock(addressId)
            .map { address ->
                address.update(fullName, zipCode, stateOrRegion, line1, line2, phoneNumber)
                    .also { updated -> addressRepository.set(updated) }
                    .toDTO()
            }
            .onErrorResume { Mono.error(it.error()) }
    }
        .getOrElse { Mono.error(it.error()) }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    fun delete(command: DeleteAddressCommand): Mono<AddressDTO> = runCatching {
        val addressId = AddressId.valueOf(command.addressId)

        return lock(addressId)
            .map { address ->
                address.delete()
                    .also { deleted -> addressRepository.remove(deleted) }
                    .toDTO()
            }
            .onErrorResume { Mono.error(it.error()) }
    }
        .getOrElse { Mono.error(it.error()) }

    private companion object {

        const val TRANSACTIONAL_TIMEOUT_SECONDS: Int = 10
    }
}
