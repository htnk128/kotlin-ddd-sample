package htnk128.kotlin.ddd.sample.customer.application.command

data class CreateCustomerCommand(
    val name: String,
    val namePronunciation: String,
    val email: String,
    val password: String
)
