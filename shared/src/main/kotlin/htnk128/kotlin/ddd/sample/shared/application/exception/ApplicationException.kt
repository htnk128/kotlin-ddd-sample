package htnk128.kotlin.ddd.sample.shared.application.exception

/**
 * アプリケーションレイヤーにおいて問題が生じた場合に発生する例外。
 */
class ApplicationException(
    val type: String,
    val status: Int,
    override val message: String,
    override val cause: Throwable? = null
) : RuntimeException(message, cause) {

    constructor(message: String?, cause: Throwable? = null) : this(
        type = "server_error",
        status = 500,
        message = message ?: "internal server error.",
        cause = cause
    )
}
