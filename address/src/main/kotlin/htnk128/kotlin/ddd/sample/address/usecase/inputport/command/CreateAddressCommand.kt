package htnk128.kotlin.ddd.sample.address.usecase.inputport.command

/**
 * 住所を作成する際のコマンド情報。
 */
data class CreateAddressCommand(
    val ownerId: String,
    val fullName: String,
    val zipCode: String,
    val stateOrRegion: String,
    val line1: String,
    val line2: String?,
    val phoneNumber: String
)
