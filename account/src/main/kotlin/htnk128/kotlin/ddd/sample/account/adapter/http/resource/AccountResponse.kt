package htnk128.kotlin.ddd.sample.account.adapter.http.resource

import htnk128.kotlin.ddd.sample.account.application.dto.AccountDTO
import io.swagger.annotations.ApiModelProperty

/**
 * アカウントのレスポンス情報。
 */
data class AccountResponse(
    @ApiModelProperty(
        value = "アカウントのID", example = "AC_c5fb2cec-a77c-4886-b997-ffc2ef060e78", required = true, position = 1
    )
    val accountId: String,
    @ApiModelProperty(
        value = "アカウントの氏名または会社名", example = "あいうえお", required = true, position = 2
    )
    val name: String,
    @ApiModelProperty(
        value = "アカウントの発音", example = "アイウエオ", required = true, position = 3
    )
    val namePronunciation: String,
    @ApiModelProperty(
        value = "アカウントのメールアドレス", example = "example@example.com", required = true, position = 4
    )
    val email: String,
    @ApiModelProperty(
        value = "アカウントのパスワード", required = true, position = 5
    )
    val password: String,
    @ApiModelProperty(
        value = "アカウントの作成日時", example = "1576120910973", required = true, position = 6
    )
    val createdAt: Long,
    @ApiModelProperty(
        value = "アカウントの削除日時", example = "1576120910973", required = false, position = 7
    )
    val deletedAt: Long?,
    @ApiModelProperty(
        value = "アカウントの更新日時", example = "1576120910973", required = true, position = 8
    )
    val updatedAt: Long
) {

    companion object {

        fun from(dto: AccountDTO): AccountResponse =
            AccountResponse(
                dto.accountId,
                dto.name,
                dto.namePronunciation,
                dto.email,
                dto.password,
                dto.createdAt,
                dto.deletedAt,
                dto.updatedAt
            )
    }
}
