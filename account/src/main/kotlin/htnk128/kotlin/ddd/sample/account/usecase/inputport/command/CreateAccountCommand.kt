package htnk128.kotlin.ddd.sample.account.usecase.inputport.command

/**
 * アカウントを作成する際のコマンド情報。
 */
data class CreateAccountCommand(
    val name: String,
    val namePronunciation: String,
    val email: String,
    val password: String
)
