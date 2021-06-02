package htnk128.kotlin.ddd.sample.account.usecase.inputport.command

/**
 * アカウントを取得する際のコマンド情報。
 */
data class FindAccountCommand(
    val accountId: String
)
