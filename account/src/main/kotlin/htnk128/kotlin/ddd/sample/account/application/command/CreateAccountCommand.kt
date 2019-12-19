package htnk128.kotlin.ddd.sample.account.application.command

data class CreateAccountCommand(
    val name: String,
    val namePronunciation: String,
    val email: String,
    val password: String
)
