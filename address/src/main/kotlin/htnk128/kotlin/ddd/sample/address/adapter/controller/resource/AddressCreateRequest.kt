package htnk128.kotlin.ddd.sample.address.adapter.controller.resource

import io.swagger.annotations.ApiModelProperty

/**
 * 住所作成時のリクエスト情報。
 */
data class AddressCreateRequest(
    @ApiModelProperty(
        value = "住所の持ち主のID", example = "AC_c5fb2cec-a77c-4886-b997-ffc2ef060e78", required = true, position = 1
    )
    val ownerId: String,
    @ApiModelProperty(
        value = "住所の氏名または会社名", example = "あいうえお", required = true, position = 2
    )
    val fullName: String,
    @ApiModelProperty(
        value = "住所の郵便番号", example = "1234567", required = true, position = 3
    )
    val zipCode: String,
    @ApiModelProperty(
        value = "住所の都道府県", example = "東京都", required = true, position = 4
    )
    val stateOrRegion: String,
    @ApiModelProperty(
        value = "住所の住所欄1", example = "かきくけこ", required = true, position = 5
    )
    val line1: String,
    @ApiModelProperty(
        value = "住所の住所欄2", example = "さしすせそ", required = false, position = 6
    )
    val line2: String?,
    @ApiModelProperty(
        value = "住所の電話番号", example = "00000000000", required = true, position = 7
    )
    val phoneNumber: String
)
