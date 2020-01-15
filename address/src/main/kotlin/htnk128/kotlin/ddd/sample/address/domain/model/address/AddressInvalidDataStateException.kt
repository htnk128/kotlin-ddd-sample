package htnk128.kotlin.ddd.sample.address.domain.model.address

/**
 * 住所のドメインが無効なデータ状態にある場合に発生する例外。
 */
class AddressInvalidDataStateException(
    override val message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause) {

    val type: String = "invalid_data_state"
}
