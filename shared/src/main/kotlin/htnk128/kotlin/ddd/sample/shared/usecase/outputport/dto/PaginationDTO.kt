package htnk128.kotlin.ddd.sample.shared.usecase.outputport.dto

/**
 * 各オブジェクトを一覧取得した際のDTO。
 */
data class PaginationDTO<T>(
    val count: Int,
    val limit: Int,
    val offset: Int,
    val data: List<T>
) {
    val hasMore = (count > limit + (limit * offset))
}
