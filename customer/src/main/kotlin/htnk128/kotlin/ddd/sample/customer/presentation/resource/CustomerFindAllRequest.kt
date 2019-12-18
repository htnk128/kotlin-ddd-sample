package htnk128.kotlin.ddd.sample.customer.presentation.resource

import io.swagger.annotations.ApiModelProperty

data class CustomerFindAllRequest(
    @ApiModelProperty(
        value = "取得するデータ数の最大値", example = "10", required = false, allowableValues = "range[1, 100]", position = 1
    )
    val limit: Int = 10,
    @ApiModelProperty(
        value = "基準点からのデータ取得を行う開始位置", example = "0", required = false, allowableValues = "range[0, 1000]", position = 2
    )
    val offset: Int = 0
)
