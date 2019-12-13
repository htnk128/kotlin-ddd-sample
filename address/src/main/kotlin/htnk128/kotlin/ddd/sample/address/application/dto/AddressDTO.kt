package htnk128.kotlin.ddd.sample.address.application.dto

import htnk128.kotlin.ddd.sample.address.domain.model.address.Address

/**
 * 住所([Address])のDTO。
 */
data class AddressDTO(
    val addressId: String,
    val customerId: String,
    val fullName: String,
    val zipCode: String,
    val stateOrRegion: String,
    val line1: String,
    val line2: String?,
    val phoneNumber: String,
    val createdAt: Long,
    val deletedAt: Long?,
    val updatedAt: Long
)
