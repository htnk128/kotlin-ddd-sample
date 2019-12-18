package htnk128.kotlin.ddd.sample.customer.presentation.resource

import htnk128.kotlin.ddd.sample.customer.application.dto.CustomerDTO
import io.swagger.annotations.ApiModelProperty

/**
 * 顧客のレスポンス情報。
 */
data class CustomerResponse(
    @ApiModelProperty(
        value = "顧客のID", example = "CUS_c5fb2cec-a77c-4886-b997-ffc2ef060e78", required = true, position = 1
    )
    val customerId: String,
    @ApiModelProperty(
        value = "顧客の氏名または会社名", example = "あいうえお", required = true, position = 2
    )
    val name: String,
    @ApiModelProperty(
        value = "顧客の発音", example = "アイウエオ", required = true, position = 3
    )
    val namePronunciation: String,
    @ApiModelProperty(
        value = "顧客のメールアドレス", example = "example@example.com", required = true, position = 4
    )
    val email: String,
    @ApiModelProperty(
        value = "顧客の作成日時", example = "1576120910973", required = true, position = 5
    )
    val createdAt: Long,
    @ApiModelProperty(
        value = "顧客の削除日時", example = "1576120910973", required = false, position = 6
    )
    val deletedAt: Long?,
    @ApiModelProperty(
        value = "顧客の更新日時", example = "1576120910973", required = true, position = 7
    )
    val updatedAt: Long
) {

    companion object {

        fun from(dto: CustomerDTO): CustomerResponse =
            CustomerResponse(
                dto.customerId,
                dto.name,
                dto.namePronunciation,
                dto.email,
                dto.createdAt,
                dto.deletedAt,
                dto.updatedAt
            )
    }
}
