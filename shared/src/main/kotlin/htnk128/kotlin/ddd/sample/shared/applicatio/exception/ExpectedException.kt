package htnk128.kotlin.ddd.sample.shared.applicatio.exception

/**
 * 予想された問題が発生した場合に発生する例外。
 */
open class ExpectedException(
    val type: String,
    val status: Int,
    override val message: String,
    override val cause: Throwable? = null
) : RuntimeException(message, cause)
