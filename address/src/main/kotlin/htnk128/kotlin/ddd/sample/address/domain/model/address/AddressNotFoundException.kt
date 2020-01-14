package htnk128.kotlin.ddd.sample.address.domain.model.address

/**
 * 住所のドメインモデルが存在しない場合に発生する例外。
 */
class AddressNotFoundException(
    addressId: AddressId,
    override val message: String = "Address not found. (addressId=$addressId)",
    cause: Throwable? = null
) : RuntimeException(message, cause) {

    val type: String = "not_found_error"
}
