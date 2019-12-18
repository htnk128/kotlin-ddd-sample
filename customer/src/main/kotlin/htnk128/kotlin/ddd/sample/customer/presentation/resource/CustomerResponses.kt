package htnk128.kotlin.ddd.sample.customer.presentation.resource

import io.swagger.annotations.ApiModelProperty

/**
 * 複数の顧客のレスポンス情報。
 *
 * 複数の顧客のレスポンス情報には以下の情報が含まれる。
 * - count
 *   顧客の件数
 * - hasMore
 *   取得できていない顧客があるかどうか
 * - data
 *   顧客のレスポンス情報([CustomerResponse])のリスト
 */
data class CustomerResponses(
    @ApiModelProperty(
        value = "顧客の件数", required = true, position = 1
    )
    val count: Int,
    @ApiModelProperty(
        value = "取得できていない顧客があるかどうか", required = true, position = 2
    )
    val hasMore: Boolean,
    @ApiModelProperty(
        value = "顧客のレスポンス情報のリスト", required = true, position = 3
    )
    val data: List<CustomerResponse>
)
