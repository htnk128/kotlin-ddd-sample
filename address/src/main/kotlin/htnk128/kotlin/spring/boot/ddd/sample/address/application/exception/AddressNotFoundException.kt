package htnk128.kotlin.spring.boot.ddd.sample.address.application.exception

import htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.address.Address
import htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.address.AddressId
import htnk128.kotlin.spring.boot.ddd.sample.shared.application.exception.NotFoundException

/**
 * 住所([Address])のドメインモデルが存在しない場合に発生する例外。
 */
class AddressNotFoundException(
    addressId: AddressId,
    message: String = "Address not found. (addressId=$addressId)",
    cause: Throwable? = null
) : NotFoundException(message = message, cause = cause)
