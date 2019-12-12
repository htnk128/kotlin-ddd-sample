package htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.exception

import htnk128.kotlin.spring.boot.ddd.sample.customer.domain.model.customer.Customer
import htnk128.kotlin.spring.boot.ddd.sample.shared.domain.exception.InvalidRequestException

/**
 * 無効なリクエストを受けて顧客([Customer])のドメインモデルへの変換に失敗した場合に発生する例外。
 */
class CustomerInvalidRequestException(
    message: String,
    cause: Throwable? = null
) : InvalidRequestException(message = message, cause = cause)
