package htnk128.kotlin.ddd.sample.address.usecase.interactor

import htnk128.kotlin.ddd.sample.address.domain.model.address.Address
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressEvent
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressId
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressInvalidDataStateException
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressInvalidRequestException
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressNotFoundException
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressUpdateFailedException
import htnk128.kotlin.ddd.sample.address.domain.model.address.FullName
import htnk128.kotlin.ddd.sample.address.domain.model.address.Line1
import htnk128.kotlin.ddd.sample.address.domain.model.address.Line2
import htnk128.kotlin.ddd.sample.address.domain.model.address.PhoneNumber
import htnk128.kotlin.ddd.sample.address.domain.model.address.StateOrRegion
import htnk128.kotlin.ddd.sample.address.domain.model.address.ZipCode
import htnk128.kotlin.ddd.sample.address.domain.model.owner.OwnerId
import htnk128.kotlin.ddd.sample.address.domain.model.owner.OwnerInvalidRequestException
import htnk128.kotlin.ddd.sample.address.domain.model.owner.OwnerNotFoundException
import htnk128.kotlin.ddd.sample.address.domain.model.owner.OwnerService
import htnk128.kotlin.ddd.sample.address.domain.repository.AddressRepository
import htnk128.kotlin.ddd.sample.address.usecase.inputport.AddressUseCase
import htnk128.kotlin.ddd.sample.address.usecase.inputport.command.CreateAddressCommand
import htnk128.kotlin.ddd.sample.address.usecase.inputport.command.DeleteAddressCommand
import htnk128.kotlin.ddd.sample.address.usecase.inputport.command.FindAddressCommand
import htnk128.kotlin.ddd.sample.address.usecase.inputport.command.FindAllAddressCommand
import htnk128.kotlin.ddd.sample.address.usecase.inputport.command.UpdateAddressCommand
import htnk128.kotlin.ddd.sample.dddcore.domain.DomainEventPublisher
import htnk128.kotlin.ddd.sample.shared.usecase.ApplicationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * 住所([Address])ドメインの操作を提供するアプリケーションサービス。
 */
@Service
class AddressInteractor(
    private val addressRepository: AddressRepository,
    private val ownerService: OwnerService,
    private val domainEventPublisher: DomainEventPublisher<AddressEvent<*>>
) : AddressUseCase {

    @Transactional(readOnly = true)
    override fun find(command: FindAddressCommand): Mono<Address> = runCatching {
        val addressId = AddressId.valueOf(command.addressId)

        Mono.just(addressRepository.find(addressId))
            .onErrorResume { Mono.error(it.error()) }
    }
        .getOrElse { Mono.error(it.error()) }

    @Transactional(readOnly = true)
    override fun findAll(command: FindAllAddressCommand): Flux<Address> = runCatching {
        val ownerId = OwnerId.valueOf(command.ownerId)

        Flux.fromIterable(addressRepository.findAll(ownerId))
            .onErrorResume { Flux.error(it.error()) }
    }
        .getOrElse { Flux.error(it.error()) }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    override fun create(command: CreateAddressCommand): Mono<Address> = runCatching {
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
            .map { created }
            .also { created.publish() }
            .onErrorResume { Mono.error(it.error()) }
    }
        .getOrElse { Mono.error(it.error()) }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    override fun update(command: UpdateAddressCommand): Mono<Address> = runCatching {
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

        Mono.just(updated)
            .also { updated.publish() }
            .onErrorResume { Mono.error(it.error()) }
    }
        .getOrElse { Mono.error(it.error()) }

    @Transactional(timeout = TRANSACTIONAL_TIMEOUT_SECONDS, rollbackFor = [Exception::class])
    override fun delete(command: DeleteAddressCommand): Mono<Address> = runCatching {
        val addressId = AddressId.valueOf(command.addressId)

        val deleted = addressRepository
            .find(addressId, lock = true)
            .delete()
            .also { addressRepository.remove(it) }

        Mono.just(deleted)
            .also { deleted.publish() }
            .onErrorResume { Mono.error(it.error()) }
    }
        .getOrElse { Mono.error(it.error()) }

    private fun Address.publish() {
        occurredEvents().forEach { domainEventPublisher.publish(it) }
    }

    private fun Throwable.error(): Throwable =
        when (this) {
            is AddressInvalidRequestException -> ApplicationException(type, 400, message, this)
            is OwnerInvalidRequestException -> ApplicationException(type, 400, message, this)
            is AddressNotFoundException -> ApplicationException(type, 404, message, this)
            is OwnerNotFoundException -> ApplicationException(type, 404, message, this)
            is AddressInvalidDataStateException -> ApplicationException(type, 409, message, this)
            is AddressUpdateFailedException -> ApplicationException(type, 500, message, this)
            else -> ApplicationException(message, this)
        }

    private companion object {

        const val TRANSACTIONAL_TIMEOUT_SECONDS: Int = 10
    }
}
