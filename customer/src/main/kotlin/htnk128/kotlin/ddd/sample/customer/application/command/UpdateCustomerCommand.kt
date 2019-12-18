package htnk128.kotlin.ddd.sample.customer.application.command

data class UpdateCustomerCommand(
    val customerId: String,
    val name: String?,
    val namePronunciation: String?,
    val email: String?
)
