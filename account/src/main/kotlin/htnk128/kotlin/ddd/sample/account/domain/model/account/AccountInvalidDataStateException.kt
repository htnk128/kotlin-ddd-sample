package htnk128.kotlin.ddd.sample.account.domain.model.account

/**
 * アカウント([Account])ドメインが無効なデータ状態の場合に発生する例外。
 */
open class AccountInvalidDataStateException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message = message, cause = cause) {

    val type: String = "invalid_data_state"

    val status: Int = 409
}
