package htnk128.kotlin.ddd.sample.address.domain.model.address

/**
 * 住所([Address])のドメインモデルの更新に失敗した場合に発生する例外。
 */
class AddressUpdateFailedException(
    addressId: AddressId,
    override val message: String = "Address update failure. (addressId=$addressId)",
    cause: Throwable? = null
) : RuntimeException(message, cause) {

    val type: String = "update_failure_error"
}
