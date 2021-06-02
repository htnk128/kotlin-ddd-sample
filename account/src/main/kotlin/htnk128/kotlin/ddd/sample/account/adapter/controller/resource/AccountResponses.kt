package htnk128.kotlin.ddd.sample.account.adapter.controller.resource

import io.swagger.annotations.ApiModelProperty

/**
 * 複数のアカウントのレスポンス情報。
 *
 * 複数のアカウントのレスポンス情報には以下の情報が含まれる。
 * - count
 *   アカウントの件数
 * - hasMore
 *   取得できていないアカウントがあるかどうか
 * - data
 *   アカウントのレスポンス情報([AccountResponse])のリスト
 */
data class AccountResponses(
    @ApiModelProperty(
        value = "アカウントの件数", required = true, position = 1
    )
    val count: Int,
    @ApiModelProperty(
        value = "取得できていないアカウントがあるかどうか", required = true, position = 2
    )
    val hasMore: Boolean,
    @ApiModelProperty(
        value = "アカウントのレスポンス情報のリスト", required = true, position = 3
    )
    val data: List<AccountResponse>
)
