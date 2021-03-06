package htnk128.kotlin.ddd.sample.address.application.service

import htnk128.kotlin.ddd.sample.address.application.dto.AddressDTO
import htnk128.kotlin.ddd.sample.address.domain.model.address.Address
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressInvalidDataStateException
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressInvalidRequestException
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressNotFoundException
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressUpdateFailedException
import htnk128.kotlin.ddd.sample.address.domain.model.owner.OwnerInvalidRequestException
import htnk128.kotlin.ddd.sample.address.domain.model.owner.OwnerNotFoundException
import htnk128.kotlin.ddd.sample.shared.application.exception.ApplicationException

fun Address.toDTO(): AddressDTO =
    AddressDTO(
        addressId.id(),
        ownerId.id(),
        fullName.value(),
        zipCode.value(),
        stateOrRegion.value(),
        line1.value(),
        line2?.value(),
        phoneNumber.value(),
        createdAt.toEpochMilli(),
        deletedAt?.toEpochMilli(),
        updatedAt.toEpochMilli()
    )

fun Throwable.error(): Throwable =
    when (this) {
        is AddressInvalidRequestException -> ApplicationException(type, 400, message, this)
        is OwnerInvalidRequestException -> ApplicationException(type, 400, message, this)
        is AddressNotFoundException -> ApplicationException(type, 404, message, this)
        is OwnerNotFoundException -> ApplicationException(type, 404, message, this)
        is AddressInvalidDataStateException -> ApplicationException(type, 409, message, this)
        is AddressUpdateFailedException -> ApplicationException(type, 500, message, this)
        else -> ApplicationException(message, this)
    }
