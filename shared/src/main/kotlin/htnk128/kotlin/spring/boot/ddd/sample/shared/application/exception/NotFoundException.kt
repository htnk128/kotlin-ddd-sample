package htnk128.kotlin.spring.boot.ddd.sample.shared.application.exception

/**
 * ドメインモデルが存在しない場合に発生する例外。
 */
abstract class NotFoundException(
    override val message: String,
    override val cause: Throwable? = null
) : RuntimeException(message, cause) {

    val type: String = "not_found_error"

    val status: Int = 404
}
