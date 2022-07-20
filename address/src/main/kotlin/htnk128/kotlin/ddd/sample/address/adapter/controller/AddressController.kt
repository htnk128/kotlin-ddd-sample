package htnk128.kotlin.ddd.sample.address.adapter.controller

import htnk128.kotlin.ddd.sample.address.adapter.controller.resource.AddressResponse
import htnk128.kotlin.ddd.sample.address.adapter.controller.resource.AddressResponses
import htnk128.kotlin.ddd.sample.address.usecase.inputport.AddressUseCase
import htnk128.kotlin.ddd.sample.address.usecase.inputport.command.CreateAddressCommand
import htnk128.kotlin.ddd.sample.address.usecase.inputport.command.DeleteAddressCommand
import htnk128.kotlin.ddd.sample.address.usecase.inputport.command.FindAddressCommand
import htnk128.kotlin.ddd.sample.address.usecase.inputport.command.FindAllAddressCommand
import htnk128.kotlin.ddd.sample.address.usecase.inputport.command.UpdateAddressCommand
import htnk128.kotlin.ddd.sample.address.usecase.outputport.AddressPresenter
import htnk128.kotlin.ddd.sample.address.usecase.outputport.dto.AddressDTO
import java.util.stream.Collectors
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AddressController(
    private val addressUseCase: AddressUseCase,
    private val addressPresenter: AddressPresenter
) {

    fun find(
        addressId: String
    ): Mono<AddressResponse> {
        val command = FindAddressCommand(addressId)

        return addressUseCase.find(command)
            .map { addressPresenter.toDTO(it) }
            .map { it.toResponse() }
    }

    fun findAll(
        ownerId: String
    ): Mono<AddressResponses> {
        val command = FindAllAddressCommand(ownerId)

        return addressUseCase.findAll(command)
            .map { addressPresenter.toDTO(it) }
            .map { it.toResponse() }
            .collect(Collectors.toList())
            .map { AddressResponses(it) }
    }

    fun create(
        ownerId: String,
        fullName: String,
        zipCode: String,
        stateOrRegion: String,
        line1: String,
        line2: String?,
        phoneNumber: String
    ): Mono<AddressResponse> {
        val command = CreateAddressCommand(
            ownerId,
            fullName,
            zipCode,
            stateOrRegion,
            line1,
            line2,
            phoneNumber
        )

        return addressUseCase.create(command)
            .map { addressPresenter.toDTO(it) }
            .map { it.toResponse() }
    }

    fun update(
        addressId: String,
        fullName: String?,
        zipCode: String?,
        stateOrRegion: String?,
        line1: String?,
        line2: String?,
        phoneNumber: String?
    ): Mono<AddressResponse> {
        val command = UpdateAddressCommand(
            addressId,
            fullName,
            zipCode,
            stateOrRegion,
            line1,
            line2,
            phoneNumber
        )

        return addressUseCase.update(command)
            .map { addressPresenter.toDTO(it) }
            .map { it.toResponse() }
    }

    fun delete(
        addressId: String
    ): Mono<AddressResponse> {
        val command = DeleteAddressCommand(addressId)

        return addressUseCase.delete(command)
            .map { addressPresenter.toDTO(it) }
            .map { it.toResponse() }
    }

    private fun AddressDTO.toResponse() =
        AddressResponse.from(this)
}
