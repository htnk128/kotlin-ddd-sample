package htnk128.kotlin.ddd.sample.address.domain.model.account

/**
 * アカウント([Account])のドメインモデルが存在しない場合に発生する例外。
 */
class AccountNotFoundException(
    accountId: AccountId,
    message: String = "Account not found. (accountId=$accountId)",
    cause: Throwable? = null
) : RuntimeException(message = message, cause = cause) {

    val type: String = "not_found_error"

    val status: Int = 404
}
