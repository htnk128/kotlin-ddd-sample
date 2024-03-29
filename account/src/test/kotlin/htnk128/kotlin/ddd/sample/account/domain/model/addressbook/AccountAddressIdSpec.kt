package htnk128.kotlin.ddd.sample.account.domain.model.addressbook

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row

class AccountAddressIdSpec : StringSpec({

    "正しい値の場合インスタンスを生成できる" {
        forall(
            row("a".repeat(64)),
            row("a_b-c-d-e"),
            row("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789")
        ) { value ->
            AccountAddressId.valueOf(value).id() shouldBe value
        }
    }

    "不正な値の場合例外となること" {
        forall(
            row(""),
            row("a".repeat(65)),
            row("あ")
        ) { value ->
            shouldThrow<AddressBookInvalidRequestException> {
                AccountAddressId.valueOf(value)
            }
        }
    }
})
