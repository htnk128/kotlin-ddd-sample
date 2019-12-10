package htnk128.kotlin.spring.boot.ddd.sample.customer.application.dto

data class CustomerDTO(
    val customerId: String,
    val name: String,
    val namePronunciation: String,
    val email: String,
    val createdAt: Long,
    val deletedAt: Long?,
    val updatedAt: Long
)
