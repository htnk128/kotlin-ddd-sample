package htnk128.kotlin.ddd.sample.account.usecase.inputport.command

/**
 * アカウントを更新する際のコマンド情報。
 */
data class UpdateAccountCommand(
    val accountId: String,
    val name: String?,
    val namePronunciation: String?,
    val email: String?,
    val password: String?
)
