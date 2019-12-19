package htnk128.kotlin.ddd.sample.address.domain.model.account

/**
 * アカウント([Account])のコンテキストマッピングへの変換に失敗した場合に発生する例外。
 */
class AccountInvalidRequestException(
    override val message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause) {

    val type: String = "invalid_request_error"
}
