package htnk128.kotlin.ddd.sample.account.domain.model.account

/**
 * アカウントのドメインモデルの更新に失敗した場合に発生する例外。
 */
class AccountUpdateFailedException(
    accountId: AccountId,
    override val message: String = "Account update failure. (accountId=$accountId)",
    cause: Throwable? = null
) : RuntimeException(message, cause) {

    val type: String = "update_failure_error"
}
