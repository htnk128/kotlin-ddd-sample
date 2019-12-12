package htnk128.kotlin.ddd.sample.customer.presentation.resource

import io.swagger.annotations.ApiModelProperty

/**
 * 顧客作成時のリクエスト情報。
 */
data class CustomerCreateRequest(
    @ApiModelProperty(
        value = "顧客の氏名または会社名", example = "あいうえお", required = true, position = 1
    )
    val name: String,
    @ApiModelProperty(
        value = "顧客の発音", example = "アイウエオ", required = true, position = 2
    )
    val namePronunciation: String,
    @ApiModelProperty(
        value = "顧客のメールアドレス", example = "example@example.com", required = true, position = 3
    )
    val email: String
)
