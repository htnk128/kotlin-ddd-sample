package htnk128.kotlin.ddd.sample.address.adapter.presenter

import htnk128.kotlin.ddd.sample.address.domain.model.address.Address
import htnk128.kotlin.ddd.sample.address.usecase.outputport.AddressPresenter
import htnk128.kotlin.ddd.sample.address.usecase.outputport.dto.AddressDTO
import org.springframework.stereotype.Component

@Component
class AddressPresenter : AddressPresenter {

    override fun toDTO(address: Address): AddressDTO {
        return AddressDTO(
            address.addressId.id(),
            address.ownerId.id(),
            address.fullName.value(),
            address.zipCode.value(),
            address.stateOrRegion.value(),
            address.line1.value(),
            address.line2?.value(),
            address.phoneNumber.value(),
            address.createdAt.toEpochMilli(),
            address.deletedAt?.toEpochMilli(),
            address.updatedAt.toEpochMilli()
        )
    }
}
