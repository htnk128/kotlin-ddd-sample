package htnk128.kotlin.ddd.sample.address.adapter.controller.resource

import io.swagger.annotations.ApiModelProperty

/**
 * 住所更新時のリクエスト情報。
 */
data class AddressUpdateRequest(
    @ApiModelProperty(
        value = "住所の氏名または会社名", example = "あいうえお", required = false, position = 1
    )
    val fullName: String?,
    @ApiModelProperty(
        value = "住所の郵便番号", example = "1234567", required = false, position = 2
    )
    val zipCode: String?,
    @ApiModelProperty(
        value = "住所の都道府県", example = "東京都", required = false, position = 3
    )
    val stateOrRegion: String?,
    @ApiModelProperty(
        value = "住所の住所欄1", example = "かきくけこ", required = false, position = 4
    )
    val line1: String?,
    @ApiModelProperty(
        value = "住所の住所欄2", example = "さしすせそ", required = false, position = 5
    )
    val line2: String?,
    @ApiModelProperty(
        value = "住所の電話番号", example = "00000000000", required = false, position = 6
    )
    val phoneNumber: String?
)
