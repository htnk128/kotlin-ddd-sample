package htnk128.kotlin.ddd.sample.account.domain.model.account

/**
 * アカウント([Account])ドメインが無効なデータ状態の場合に発生する例外。
 */
class AccountInvalidDataStateException(
    override val message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause) {

    val type: String = "invalid_data_state"
}
