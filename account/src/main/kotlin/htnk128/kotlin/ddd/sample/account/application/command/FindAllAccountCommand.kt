package htnk128.kotlin.ddd.sample.account.application.command

/**
 * すべてのアカウントを取得する際のコマンド情報。
 */
data class FindAllAccountCommand(
    val limit: Int,
    val offset: Int
)
