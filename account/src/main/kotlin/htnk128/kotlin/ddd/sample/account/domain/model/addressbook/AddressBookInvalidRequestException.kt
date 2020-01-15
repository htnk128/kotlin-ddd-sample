package htnk128.kotlin.ddd.sample.account.domain.model.addressbook

/**
 * 無効なリクエストを受けてアカウントの住所録のドメインモデルへの変換に失敗した場合に発生する例外。
 */
class AddressBookInvalidRequestException(
    override val message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause) {

    val type: String = "invalid_request_error"
}
