package htnk128.kotlin.ddd.sample.customer.application.command

data class FindAllCustomerCommand(
    val limit: Int,
    val offset: Int
)
