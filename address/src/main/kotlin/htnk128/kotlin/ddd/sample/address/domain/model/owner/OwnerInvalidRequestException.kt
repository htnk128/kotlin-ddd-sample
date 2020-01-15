package htnk128.kotlin.ddd.sample.address.domain.model.owner

/**
 * 無効なリクエストを受けて住所の持ち主のドメインモデルへの変換に失敗した場合に発生する例外。
 */
class OwnerInvalidRequestException(
    override val message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause) {

    val type: String = "invalid_request_error"
}
