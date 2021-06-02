package htnk128.kotlin.ddd.sample.address.adapter.controller.resource

import io.swagger.annotations.ApiModelProperty

/**
 * アカウントのすべての住所取得時のリクエスト情報。
 */
data class AddressFindAllRequest(
    @ApiModelProperty(
        value = "住所の持ち主のID", example = "AC_c5fb2cec-a77c-4886-b997-ffc2ef060e78", required = true, position = 1
    )
    val ownerId: String
)
