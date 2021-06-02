package htnk128.kotlin.ddd.sample.address.usecase.inputport.command

/**
 * 住所を取得する際のコマンド情報。
 */
data class FindAddressCommand(
    val addressId: String
)
