package htnk128.kotlin.ddd.sample.customer.domain.exception

import htnk128.kotlin.ddd.sample.customer.domain.model.customer.Customer
import htnk128.kotlin.ddd.sample.shared.domain.exception.InvalidDataStateException

/**
 * 顧客([Customer])ドメインが無効なデータ状態の場合に発生する例外。
 */
open class CustomerInvalidDataStateException(
    message: String,
    cause: Throwable? = null
) : InvalidDataStateException(message = message, cause = cause)
