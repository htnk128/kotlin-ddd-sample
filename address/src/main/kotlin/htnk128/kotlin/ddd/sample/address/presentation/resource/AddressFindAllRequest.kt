package htnk128.kotlin.ddd.sample.address.presentation.resource

import io.swagger.annotations.ApiModelProperty

/**
 * 顧客のすべての住所取得時のリクエスト情報。
 */
data class AddressFindAllRequest(
    @ApiModelProperty(
        value = "顧客のID", example = "CUST_c5fb2cec-a77c-4886-b997-ffc2ef060e78", required = true, position = 1
    )
    val customerId: String
)
