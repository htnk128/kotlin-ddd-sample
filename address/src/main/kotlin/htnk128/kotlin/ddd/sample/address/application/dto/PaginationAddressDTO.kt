package htnk128.kotlin.ddd.sample.address.application.dto

import htnk128.kotlin.ddd.sample.address.domain.model.address.Address

/**
 * 住所([Address])を一覧取得した際のDTO。
 */
data class PaginationAddressDTO(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val data: List<AddressDTO>
) {
    val hasMore = (count > limit + (limit * offset))
}
