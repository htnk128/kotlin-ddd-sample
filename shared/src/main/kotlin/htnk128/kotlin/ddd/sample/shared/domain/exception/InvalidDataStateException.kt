package htnk128.kotlin.ddd.sample.shared.domain.exception

/**
 * ドメインモデルが無効なデータ状態の場合に発生する例外。
 */
open class InvalidDataStateException(
    override val message: String,
    override val cause: Throwable? = null
) : RuntimeException(message, cause) {

    val type: String = "invalid_data_state"

    val status: Int = 409
}
