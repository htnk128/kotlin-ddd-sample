package htnk128.kotlin.ddd.sample.address.usecase.inputport.command

/**
 * アカウントのすべての住所を取得する際のコマンド情報。
 */
data class FindAllAddressCommand(
    val ownerId: String
)
