package htnk128.kotlin.ddd.sample.customer.application.dto

import htnk128.kotlin.ddd.sample.customer.domain.model.customer.Customer

/**
 * 顧客([Customer])のDTO。
 */
data class CustomerDTO(
    val customerId: String,
    val name: String,
    val namePronunciation: String,
    val email: String,
    val password: String,
    val createdAt: Long,
    val deletedAt: Long?,
    val updatedAt: Long
)
