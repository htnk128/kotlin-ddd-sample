package htnk128.kotlin.ddd.sample.account.usecase.inputport.command

/**
 * アカウントを削除する際のコマンド情報。
 */
data class DeleteAccountCommand(
    val accountId: String
)
