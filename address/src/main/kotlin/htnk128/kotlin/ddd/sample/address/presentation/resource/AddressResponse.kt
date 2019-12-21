package htnk128.kotlin.ddd.sample.address.presentation.resource

import htnk128.kotlin.ddd.sample.address.application.dto.AddressDTO
import io.swagger.annotations.ApiModelProperty

/**
 * 住所のレスポンス情報。
 */
data class AddressResponse(
    @ApiModelProperty(
        value = "住所のID", example = "ADDR_c5fb2cec-a77c-4886-b997-ffc2ef060e78", required = true, position = 1
    )
    val addressId: String,
    @ApiModelProperty(
        value = "住所に紐付いているアカウントのID", example = "AC_c5fb2cec-a77c-4886-b997-ffc2ef060e78", required = true, position = 2
    )
    val accountId: String,
    @ApiModelProperty(
        value = "住所の氏名または会社名", example = "あいうえお", required = true, position = 3
    )
    val fullName: String,
    @ApiModelProperty(
        value = "住所の郵便番号", example = "1234567", required = true, position = 4
    )
    val zipCode: String,
    @ApiModelProperty(
        value = "住所の都道府県", example = "東京都", required = true, position = 5
    )
    val stateOrRegion: String,
    @ApiModelProperty(
        value = "住所の住所欄1", example = "かきくけこ", required = true, position = 6
    )
    val line1: String,
    @ApiModelProperty(
        value = "住所の住所欄2", example = "さしすせそ", required = false, position = 7
    )
    val line2: String?,
    @ApiModelProperty(
        value = "住所の電話番号", example = "00000000000", required = true, position = 8
    )
    val phoneNumber: String,
    @ApiModelProperty(
        value = "住所の作成日時", example = "1576120910973", required = true, position = 9
    )
    val createdAt: Long,
    @ApiModelProperty(
        value = "住所の削除日時", example = "1576120910973", required = false, position = 10
    )
    val deletedAt: Long?,
    @ApiModelProperty(
        value = "住所の更新日時", example = "1576120910973", required = true, position = 11
    )
    val updatedAt: Long
) {

    companion object {

        fun from(dto: AddressDTO): AddressResponse =
            AddressResponse(
                dto.addressId,
                dto.accountId,
                dto.fullName,
                dto.zipCode,
                dto.stateOrRegion,
                dto.line1,
                dto.line2,
                dto.phoneNumber,
                dto.createdAt,
                dto.deletedAt,
                dto.updatedAt
            )
    }
}
