package htnk128.kotlin.ddd.sample.account.domain.model.account

/**
 * アカウントのドメインモデルが無効なデータ状態にある場合に発生する例外。
 */
class AccountInvalidDataStateException(
    override val message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause) {

    val type: String = "invalid_data_state"
}
