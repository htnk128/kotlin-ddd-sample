package htnk128.kotlin.spring.boot.ddd.sample.shared

/**
 * 予期せぬ問題が発生した場合に発生する例外。
 */
open class UnexpectedException(
    override val message: String,
    override val cause: Throwable? = null
) : RuntimeException(message, cause) {

    val type: String = "server_error"

    val status: Int = 500
}
