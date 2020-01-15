package htnk128.kotlin.ddd.sample.address.domain.model.owner

/**
 * 住所の持ち主のドメインモデルが存在しない場合に発生する例外。
 */
class OwnerNotFoundException(
    ownerId: OwnerId,
    override val message: String = "Address owner not found. (ownerId=$ownerId)",
    cause: Throwable? = null
) : RuntimeException(message, cause) {

    val type: String = "not_found_error"
}
