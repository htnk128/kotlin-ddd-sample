package htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.exception

import htnk128.kotlin.spring.boot.ddd.sample.address.domain.model.customer.Customer
import htnk128.kotlin.spring.boot.ddd.sample.shared.domain.exception.InvalidRequestException

/**
 * 顧客([Customer])のコンテキストマッピングへの変換に失敗した場合に発生する例外。
 */
class CustomerInvalidRequestException(
    message: String,
    cause: Throwable? = null
) : InvalidRequestException(message = message, cause = cause)
