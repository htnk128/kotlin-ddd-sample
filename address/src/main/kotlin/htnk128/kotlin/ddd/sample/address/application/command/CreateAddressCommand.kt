package htnk128.kotlin.ddd.sample.address.application.command

/**
 * 住所を作成する際のコマンド情報。
 */
data class CreateAddressCommand(
    val addressOwnerId: String,
    val fullName: String,
    val zipCode: String,
    val stateOrRegion: String,
    val line1: String,
    val line2: String?,
    val phoneNumber: String
)
