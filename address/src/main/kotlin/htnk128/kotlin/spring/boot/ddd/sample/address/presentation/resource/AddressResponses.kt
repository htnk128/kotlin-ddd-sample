package htnk128.kotlin.spring.boot.ddd.sample.address.presentation.resource

import io.swagger.annotations.ApiModelProperty

/**
 * 複数の住所のレスポンス情報。
 *
 * 複数の住所のレスポンス情報には"data"というキーで住所のレスポンス情報([AddressResponse])が含まれる。
 */
data class AddressResponses(
    @ApiModelProperty(
        value = "住所のレスポンス情報のリスト", required = true, position = 1
    )
    val data: List<AddressResponse>
)
