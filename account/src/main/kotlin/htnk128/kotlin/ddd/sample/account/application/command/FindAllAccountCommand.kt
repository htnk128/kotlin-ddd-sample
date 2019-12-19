package htnk128.kotlin.ddd.sample.account.application.command

data class FindAllAccountCommand(
    val limit: Int,
    val offset: Int
)
