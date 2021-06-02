package htnk128.kotlin.ddd.sample.address.usecase.inputport

import htnk128.kotlin.ddd.sample.address.domain.model.address.Address
import htnk128.kotlin.ddd.sample.address.usecase.inputport.command.CreateAddressCommand
import htnk128.kotlin.ddd.sample.address.usecase.inputport.command.DeleteAddressCommand
import htnk128.kotlin.ddd.sample.address.usecase.inputport.command.FindAddressCommand
import htnk128.kotlin.ddd.sample.address.usecase.inputport.command.FindAllAddressCommand
import htnk128.kotlin.ddd.sample.address.usecase.inputport.command.UpdateAddressCommand
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface AddressUseCase {

    fun find(command: FindAddressCommand): Mono<Address>

    fun findAll(command: FindAllAddressCommand): Flux<Address>

    fun create(command: CreateAddressCommand): Mono<Address>

    fun update(command: UpdateAddressCommand): Mono<Address>

    fun delete(command: DeleteAddressCommand): Mono<Address>
}
