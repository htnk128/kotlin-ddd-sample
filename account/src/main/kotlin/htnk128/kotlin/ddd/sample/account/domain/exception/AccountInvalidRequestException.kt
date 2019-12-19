package htnk128.kotlin.ddd.sample.account.domain.exception

import htnk128.kotlin.ddd.sample.account.domain.model.account.Account
import htnk128.kotlin.ddd.sample.shared.domain.exception.InvalidRequestException

/**
 * 無効なリクエストを受けてアカウント([Account])のドメインモデルへの変換に失敗した場合に発生する例外。
 */
class AccountInvalidRequestException(
    message: String,
    cause: Throwable? = null
) : InvalidRequestException(message = message, cause = cause)
