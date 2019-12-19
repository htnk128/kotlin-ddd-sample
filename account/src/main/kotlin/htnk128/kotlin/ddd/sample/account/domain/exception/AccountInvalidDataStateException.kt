package htnk128.kotlin.ddd.sample.account.domain.exception

import htnk128.kotlin.ddd.sample.account.domain.model.account.Account
import htnk128.kotlin.ddd.sample.shared.domain.exception.InvalidDataStateException

/**
 * アカウント([Account])ドメインが無効なデータ状態の場合に発生する例外。
 */
open class AccountInvalidDataStateException(
    message: String,
    cause: Throwable? = null
) : InvalidDataStateException(message = message, cause = cause)
