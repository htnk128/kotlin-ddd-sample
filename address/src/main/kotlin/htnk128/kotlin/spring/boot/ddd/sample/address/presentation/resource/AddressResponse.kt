package htnk128.kotlin.spring.boot.ddd.sample.address.presentation.resource

import htnk128.kotlin.spring.boot.ddd.sample.address.application.dto.AddressDTO
import io.swagger.annotations.ApiModelProperty

/**
 * 住所のレスポンス情報。
 */
data class AddressResponse(
    @ApiModelProperty(
        value = "住所のID", example = "CUS_c5fb2cec-a77c-4886-b997-ffc2ef060e78", required = true, position = 1
    )
    val addressId: String,
    @ApiModelProperty(
        value = "住所の名前", example = "あいうえお", required = true, position = 2
    )
    val name: String,
    @ApiModelProperty(
        value = "住所の発音", example = "アイウエオ", required = true, position = 3
    )
    val namePronunciation: String,
    @ApiModelProperty(
        value = "住所のメールアドレス", example = "example@example.com", required = true, position = 4
    )
    val email: String,
    @ApiModelProperty(
        value = "住所の作成日時", example = "1576120910973", required = true, position = 5
    )
    val createdAt: Long,
    @ApiModelProperty(
        value = "住所の更新日時", example = "1576120910973", required = true, position = 6
    )
    val updatedAt: Long
) {

    companion object {

        fun from(dto: AddressDTO): AddressResponse = AddressResponse(
            dto.addressId,
            dto.name,
            dto.namePronunciation,
            dto.email,
            dto.createdAt,
            dto.updatedAt
        )
    }
}
