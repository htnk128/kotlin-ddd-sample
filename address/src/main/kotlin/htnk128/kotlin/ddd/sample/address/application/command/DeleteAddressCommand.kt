package htnk128.kotlin.ddd.sample.address.application.command

/**
 * 住所を削除する際のコマンド情報。
 */
data class DeleteAddressCommand(
    val addressId: String
)
