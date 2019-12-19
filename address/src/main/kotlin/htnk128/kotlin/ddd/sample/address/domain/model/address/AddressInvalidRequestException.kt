package htnk128.kotlin.ddd.sample.address.domain.model.address

/**
 * 無効なリクエストを受けて住所([Address])のドメインモデルへの変換に失敗した場合に発生する例外。
 */
class AddressInvalidRequestException(
    override val message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause) {

    val type: String = "invalid_request_error"
}
