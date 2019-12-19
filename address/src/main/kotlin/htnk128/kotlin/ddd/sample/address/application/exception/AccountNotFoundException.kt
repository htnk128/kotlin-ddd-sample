package htnk128.kotlin.ddd.sample.address.application.exception

import htnk128.kotlin.ddd.sample.address.domain.model.account.Account
import htnk128.kotlin.ddd.sample.address.domain.model.account.AccountId
import htnk128.kotlin.ddd.sample.shared.application.exception.NotFoundException

/**
 * アカウント([Account])のドメインモデルが存在しない場合に発生する例外。
 */
class AccountNotFoundException(
    accountId: AccountId,
    message: String = "Account not found. (accountId=$accountId)",
    cause: Throwable? = null
) : NotFoundException(message = message, cause = cause)
