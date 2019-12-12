package htnk128.kotlin.spring.boot.ddd.sample.address.application.dto

data class AddressDTO(
    val addressId: String,
    val name: String,
    val namePronunciation: String,
    val email: String,
    val createdAt: Long,
    val deletedAt: Long?,
    val updatedAt: Long
)
