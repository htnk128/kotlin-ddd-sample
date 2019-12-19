package htnk128.kotlin.ddd.sample.shared.presentation.resource

/**
 * エラーが発生した場合の最終的なエンドユーザ向けエラーレスポンス情報。
 *
 * エラーレスポンス情報には"error"というキーでエラーの詳細情報([Error])が含まれる。
 */
data class ErrorResponse(
    val error: Error
) {
    /**
     * エラーの詳細情報。
     *
     * 次の情報が含まれる。
     * - type
     *    - エラータイプ
     * - status
     *    - HTTPステータスコード
     * - message
     *    - エラーメッセージ
     */
    data class Error(
        val type: String,
        val status: Int,
        val message: String
    )

    companion object {

        fun from(type: String, code: Int, message: String): ErrorResponse =
            ErrorResponse(Error(type, code, message))
    }
}
