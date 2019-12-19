package htnk128.kotlin.ddd.sample.account.presentation.resource

import io.swagger.annotations.ApiModelProperty

/**
 * アカウント作成時のリクエスト情報。
 */
data class AccountCreateRequest(
    @ApiModelProperty(
        value = "アカウントの氏名または会社名", example = "あいうえお", required = true, position = 1
    )
    val name: String,
    @ApiModelProperty(
        value = "アカウントの発音", example = "アイウエオ", required = true, position = 2
    )
    val namePronunciation: String,
    @ApiModelProperty(
        value = "アカウントのメールアドレス", example = "example@example.com", required = true, position = 3
    )
    val email: String,
    @ApiModelProperty(
        value = "アカウントのパスワード", required = true, position = 4
    )
    val password: String
)
