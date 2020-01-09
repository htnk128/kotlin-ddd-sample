package htnk128.kotlin.ddd.sample.address.domain.model.address

/**
 * 住所の持ち主([Owner])のドメインモデルが存在しない場合に発生する例外。
 */
class OwnerNotFoundException(
    accountId: AccountId,
    override val message: String = "Owner not found. (accountId=$accountId)",
    cause: Throwable? = null
) : RuntimeException(message, cause) {

    val type: String = "not_found_error"
}
