package htnk128.kotlin.ddd.sample.customer.presentation.resource

import io.swagger.annotations.ApiModelProperty

/**
 * 顧客更新時のリクエスト情報。
 */
data class CustomerUpdateRequest(
    @ApiModelProperty(
        value = "顧客の氏名または会社名", example = "あいうえお", required = false, position = 1
    )
    val name: String?,
    @ApiModelProperty(
        value = "顧客の発音", example = "アイウエオ", required = false, position = 2
    )
    val namePronunciation: String?,
    @ApiModelProperty(
        value = "顧客のメールアドレス", example = "example@example.com", required = false, position = 3
    )
    val email: String?
)
