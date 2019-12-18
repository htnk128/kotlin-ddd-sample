package htnk128.kotlin.ddd.sample.address.domain.exception

import htnk128.kotlin.ddd.sample.address.domain.model.address.Address
import htnk128.kotlin.ddd.sample.shared.domain.exception.InvalidDataStateException

/**
 * 住所([Address])ドメインが無効なデータ状態の場合に発生する例外。
 */
open class AddressInvalidDataStateException(
    message: String,
    cause: Throwable? = null
) : InvalidDataStateException(message = message, cause = cause)
