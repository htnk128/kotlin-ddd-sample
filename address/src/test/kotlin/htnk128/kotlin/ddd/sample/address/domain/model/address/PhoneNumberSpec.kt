package htnk128.kotlin.ddd.sample.address.domain.model.address

import htnk128.kotlin.ddd.sample.address.domain.exception.AddressInvalidRequestException
import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class PhoneNumberSpec : StringSpec({

    "正しい値の場合インスタンスを生成できる" {
        forall(
            row("1".repeat(50)),
            row("12345667890")
        ) { value ->
            PhoneNumber.valueOf(value).toValue() shouldBe value
        }
    }

    "不正な値の場合例外となること" {
        forall(
            row(""),
            row("1".repeat(51)),
            row("あ")
        ) { value ->
            shouldThrow<AddressInvalidRequestException> {
                PhoneNumber.valueOf(value)
            }
        }
    }
})
