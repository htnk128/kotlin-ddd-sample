package htnk128.kotlin.ddd.sample.address.usecase.inputport.command

/**
 * 住所を更新する際のコマンド情報。
 */
data class UpdateAddressCommand(
    val addressId: String,
    val fullName: String?,
    val zipCode: String?,
    val stateOrRegion: String?,
    val line1: String?,
    val line2: String?,
    val phoneNumber: String?
)
