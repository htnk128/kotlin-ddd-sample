package htnk128.kotlin.ddd.sample.customer.domain.exception

import htnk128.kotlin.ddd.sample.customer.domain.model.address.Address
import htnk128.kotlin.ddd.sample.shared.domain.exception.InvalidRequestException

/**
 * 住所([Address])のコンテキストマッピングへの変換に失敗した場合に発生する例外。
 */
class AddressInvalidRequestException(
    message: String,
    cause: Throwable? = null
) : InvalidRequestException(message = message, cause = cause)
