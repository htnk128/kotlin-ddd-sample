package htnk128.kotlin.ddd.sample.address.application.service

import htnk128.kotlin.ddd.sample.address.application.command.CreateAddressCommand
import htnk128.kotlin.ddd.sample.address.application.command.DeleteAddressCommand
import htnk128.kotlin.ddd.sample.address.application.command.FindAddressCommand
import htnk128.kotlin.ddd.sample.address.application.command.FindAllAddressCommand
import htnk128.kotlin.ddd.sample.address.application.command.UpdateAddressCommand
import htnk128.kotlin.ddd.sample.address.application.dto.AddressDTO
import htnk128.kotlin.ddd.sample.address.domain.model.address.Address
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressEvent
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressId
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressRepository
import htnk128.kotlin.ddd.sample.address.domain.model.address.FullName
import htnk128.kotlin.ddd.sample.address.domain.model.address.Line1
import htnk128.kotlin.ddd.sample.address.domain.model.address.Line2
import htnk128.kotlin.ddd.sample.address.domain.model.address.PhoneNumber
import htnk128.kotlin.ddd.sample.address.domain.model.address.StateOrRegion
import htnk128.kotlin.ddd.sample.address.domain.model.address.ZipCode
import htnk128.kotlin.ddd.sample.address.domain.model.owner.OwnerId
import htnk128.kotlin.ddd.sample.address.domain.model.owner.OwnerNotFoundException
import htnk128.kotlin.ddd.sample.address.domain.model.owner.OwnerService
import htnk128.kotlin.ddd.sample.dddcore.domain.DomainEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * 住所([Address])ドメインの操作を提供するアプリケーションサービス。
 */
@Service
class AddressService(
    private val addressRepository: AddressRepository,
    private val ownerService: OwnerService,
    private val domainEventPublisher: DomainEventPublisher<AddressEvent<*>>
) {

    @Transactional(readOnly = true)
    fun find(command: FindAddressCommand): Mono<AddressDTO> = runCatching {
        val addressId = AddressId.valueOf(command.addressId)

        Mono.just(addressRepository.find(addressId).toDTO())
            .onErrorResume { Mono.error(it.error()) }
    }
        .getOrElse { Mono.error(it.error()) }

    @Transactional(readOnly = true)
    fun findAll(command: FindAllAddressCommand): Flux<AddressDTO> = runCatching {
        val ownerId = OwnerId.valueOf(command.ownerId)

        Flux.fromIterable(addressRepository.findAll(ownerId).map { it.toDTO() })
            .onErrorResume { Flux.error(it.error()) }
    }
        .getOrElse { Flux.error(it.error()) }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    fun create(command: CreateAddressCommand): Mono<AddressDTO> = runCatching {
        val ownerId = OwnerId.valueOf(command.ownerId)
        val fullName = FullName.valueOf(command.fullName)
        val zipCode = ZipCode.valueOf(command.zipCode)
        val stateOrRegion = StateOrRegion.valueOf(command.stateOrRegion)
        val line1 = Line1.valueOf(command.line1)
        val line2 = command.line2?.let { Line2.valueOf(it) }
        val phoneNumber = PhoneNumber.valueOf(command.phoneNumber)
        val addressId = addressRepository.nextAddressId()

        val created = Address
            .create(addressId, ownerId, fullName, zipCode, stateOrRegion, line1, line2, phoneNumber)

        ownerService.find(ownerId)
            .map { if (!it.isAvailable) throw OwnerNotFoundException(it.ownerId) }
            .zipWith(Mono.just(addressRepository.add(created)))
            .map { created.toDTO() }
            .also { created.publish() }
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

        val updated = addressRepository
            .find(addressId, lock = true)
            .update(fullName, zipCode, stateOrRegion, line1, line2, phoneNumber)
            .also { addressRepository.set(it) }

        Mono.just(updated.toDTO())
            .also { updated.publish() }
            .onErrorResume { Mono.error(it.error()) }
    }
        .getOrElse { Mono.error(it.error()) }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    fun delete(command: DeleteAddressCommand): Mono<AddressDTO> = runCatching {
        val addressId = AddressId.valueOf(command.addressId)

        val deleted = addressRepository
            .find(addressId, lock = true)
            .delete()
            .also { addressRepository.remove(it) }

        Mono.just(deleted.toDTO())
            .also { deleted.publish() }
            .onErrorResume { Mono.error(it.error()) }
    }
        .getOrElse { Mono.error(it.error()) }

    private fun Address.publish() {
        occurredEvents().forEach { domainEventPublisher.publish(it) }
    }

    private companion object {

        const val TRANSACTIONAL_TIMEOUT_SECONDS: Int = 10
    }
}
