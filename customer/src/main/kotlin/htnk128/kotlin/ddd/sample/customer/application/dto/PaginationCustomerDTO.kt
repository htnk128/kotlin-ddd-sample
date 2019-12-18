package htnk128.kotlin.ddd.sample.customer.application.dto

import htnk128.kotlin.ddd.sample.customer.domain.model.customer.Customer

/**
 * 顧客([Customer])を一覧取得した際のDTO。
 */
data class PaginationCustomerDTO(
    val count: Int,
    val hasMore: Boolean,
    val data: List<CustomerDTO>
)
