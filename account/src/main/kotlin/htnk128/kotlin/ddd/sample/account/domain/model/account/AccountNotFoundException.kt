package htnk128.kotlin.ddd.sample.account.domain.model.account

/**
 * アカウント([Account])のドメインモデルが存在しない場合に発生する例外。
 */
class AccountNotFoundException(
    accountId: AccountId,
    override val message: String = "Account not found. (accountId=$accountId)",
    cause: Throwable? = null
) : RuntimeException(message, cause) {

    val type: String = "not_found_error"
}
