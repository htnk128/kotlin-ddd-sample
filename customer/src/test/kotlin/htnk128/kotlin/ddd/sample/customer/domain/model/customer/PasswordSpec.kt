package htnk128.kotlin.ddd.sample.customer.domain.model.customer

import htnk128.kotlin.ddd.sample.customer.domain.exception.CustomerInvalidRequestException
import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class PasswordSpec : StringSpec({

    val customerId = CustomerId.generate()

    "正しい値の場合インスタンスを生成できる" {
        val rows = (6..100).map { row("a".repeat(it)) } +
            (6..100).map { row("1".repeat(it)) } +
            (6..100).map { row("あ".repeat(it)) }

        forall(*rows.toTypedArray()) { value ->
            Password.valueOf(value, customerId).toValue().length shouldBe 64
        }
    }

    "不正な値の場合例外となること" {
        forall(
            row(""),
            row("a".repeat(5)),
            row("a".repeat(101))
        ) { value ->
            shouldThrow<CustomerInvalidRequestException> {
                Password.valueOf(value, customerId)
            }
        }
    }

    "顧客を外部向けのフォーマットに変換は必ず同じ値を返すこと" {
        Password.valueOf("a".repeat(100), customerId).format() shouldBe "*****"
    }
})
