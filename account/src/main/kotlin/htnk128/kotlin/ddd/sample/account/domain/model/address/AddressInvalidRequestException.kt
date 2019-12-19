package htnk128.kotlin.ddd.sample.account.domain.model.address

/**
 * 住所([Address])のコンテキストマッピングへの変換に失敗した場合に発生する例外。
 */
class AddressInvalidRequestException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message = message, cause = cause) {

    val type: String = "invalid_request_error"

    val status: Int = 400
}
