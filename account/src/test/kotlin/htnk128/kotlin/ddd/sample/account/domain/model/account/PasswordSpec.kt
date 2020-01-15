package htnk128.kotlin.ddd.sample.account.domain.model.account

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class PasswordSpec : StringSpec({

    val accountId = AccountId.generate()

    "正しい値の場合インスタンスを生成できる" {
        val rows = (6..100).map { row("a".repeat(it)) } +
            (6..100).map { row("1".repeat(it)) } +
            (6..100).map { row("あ".repeat(it)) }

        forall(*rows.toTypedArray()) { value ->
            Password.valueOf(value, accountId).value().length shouldBe 64
        }
    }

    "不正な値の場合例外となること" {
        forall(
            row(""),
            row("a".repeat(5)),
            row("a".repeat(101))
        ) { value ->
            shouldThrow<AccountInvalidRequestException> {
                Password.valueOf(value, accountId)
            }
        }
    }

    "アカウントを外部向けのフォーマットに変換は必ず同じ値を返すこと" {
        Password.valueOf("a".repeat(100), accountId).format() shouldBe "*****"
    }
})
