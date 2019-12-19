package htnk128.kotlin.ddd.sample.account.application.dto

import htnk128.kotlin.ddd.sample.account.domain.model.account.Account

/**
 * アカウント([Account])を一覧取得した際のDTO。
 */
data class PaginationAccountDTO(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val data: List<AccountDTO>
) {
    val hasMore = (count > limit + (limit * offset))
}
