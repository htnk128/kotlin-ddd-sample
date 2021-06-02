package htnk128.kotlin.ddd.sample.address.usecase.outputport

import htnk128.kotlin.ddd.sample.address.domain.model.address.Address
import htnk128.kotlin.ddd.sample.address.usecase.outputport.dto.AddressDTO

interface AddressPresenter {

    fun toDTO(address: Address): AddressDTO
}
