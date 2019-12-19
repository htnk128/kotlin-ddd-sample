package htnk128.kotlin.ddd.sample.account.application.command

/**
 * アカウントを削除する際のコマンド情報。
 */
data class DeleteAccountCommand(
    val accountId: String
)
