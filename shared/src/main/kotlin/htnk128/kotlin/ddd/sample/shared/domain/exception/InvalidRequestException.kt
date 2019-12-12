package htnk128.kotlin.ddd.sample.shared.domain.exception

/**
 * 無効なリクエストを受けてドメインモデルへの変換に失敗した場合に発生する例外。
 */
open class InvalidRequestException(
    override val message: String,
    override val cause: Throwable? = null
) : RuntimeException(message, cause) {

    val type: String = "invalid_request_error"

    val status: Int = 400
}
