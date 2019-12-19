package htnk128.kotlin.ddd.sample.address.domain.model.address

/**
 * 住所([Address])ドメインが無効なデータ状態の場合に発生する例外。
 */
open class AddressInvalidDataStateException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message = message, cause = cause) {

    val type: String = "invalid_data_state"

    val status: Int = 409
}
