package htnk128.kotlin.ddd.sample.address.application.command

data class CreateAddressCommand(
    val customerId: String,
    val fullName: String,
    val zipCode: String,
    val stateOrRegion: String,
    val line1: String,
    val line2: String?,
    val phoneNumber: String
)
