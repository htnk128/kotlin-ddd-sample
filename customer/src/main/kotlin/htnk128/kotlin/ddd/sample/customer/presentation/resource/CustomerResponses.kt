package htnk128.kotlin.ddd.sample.customer.presentation.resource

import io.swagger.annotations.ApiModelProperty

/**
 * 複数の顧客のレスポンス情報。
 *
 * 複数の顧客のレスポンス情報には"data"というキーで顧客のレスポンス情報([CustomerResponse])が含まれる。
 */
data class CustomerResponses(
    @ApiModelProperty(
        value = "顧客のレスポンス情報のリスト", required = true, position = 1
    )
    val data: List<CustomerResponse>
)
