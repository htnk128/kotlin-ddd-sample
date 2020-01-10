package htnk128.kotlin.ddd.sample.account.adapter.http.resource

import io.swagger.annotations.ApiModelProperty

/**
 * アカウント更新時のリクエスト情報。
 */
data class AccountUpdateRequest(
    @ApiModelProperty(
        value = "アカウントの氏名または会社名", example = "あいうえお", required = false, position = 1
    )
    val name: String?,
    @ApiModelProperty(
        value = "アカウントの発音", example = "アイウエオ", required = false, position = 2
    )
    val namePronunciation: String?,
    @ApiModelProperty(
        value = "アカウントのメールアドレス", example = "example@example.com", required = false, position = 3
    )
    val email: String?,
    @ApiModelProperty(
        value = "アカウントのパスワード", required = false, position = 4
    )
    val password: String?
)
