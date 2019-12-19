package htnk128.kotlin.ddd.sample.address.presentation.resource

import io.swagger.annotations.ApiModelProperty

/**
 * アカウントのすべての住所取得時のリクエスト情報。
 */
data class AddressFindAllRequest(
    @ApiModelProperty(
        value = "アカウントのID", example = "CUST_c5fb2cec-a77c-4886-b997-ffc2ef060e78", required = true, position = 1
    )
    val accountId: String
)
