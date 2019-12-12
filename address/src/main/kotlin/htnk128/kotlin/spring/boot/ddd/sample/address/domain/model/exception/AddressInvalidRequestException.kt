package htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.exception

import htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.address.Address
import htnk128.kotlin.spring.boot.ddd.sample.shared.domain.exception.InvalidRequestException

/**
 * 無効なリクエストを受けて住所([Address])のドメインモデルへの変換に失敗した場合に発生する例外。
 */
class AddressInvalidRequestException(
    message: String,
    cause: Throwable? = null
) : InvalidRequestException(message = message, cause = cause)
