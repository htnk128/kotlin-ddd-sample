package htnk128.kotlin.spring.boot.ddd.sample.address.presentation.resource

import io.swagger.annotations.ApiModelProperty

/**
 * 住所更新時のリクエスト情報。
 */
data class AddressUpdateRequest(
    @ApiModelProperty(
        value = "住所の名前", example = "あいうえお", required = false, position = 1
    )
    val name: String?,
    @ApiModelProperty(
        value = "住所の発音", example = "アイウエオ", required = false, position = 2
    )
    val namePronunciation: String?,
    @ApiModelProperty(
        value = "住所のメールアドレス", example = "example@example.com", required = false, position = 3
    )
    val email: String?
)
