package htnk128.kotlin.ddd.sample.address.domain.model.address

/**
 * 住所の持ち主のドメインモデルが存在しない場合に発生する例外。
 */
class AddressOwnerNotFoundException(
    addressOwnerId: AddressOwnerId,
    override val message: String = "Address owner not found. (addressOwnerId=$addressOwnerId)",
    cause: Throwable? = null
) : RuntimeException(message, cause) {

    val type: String = "not_found_error"
}
