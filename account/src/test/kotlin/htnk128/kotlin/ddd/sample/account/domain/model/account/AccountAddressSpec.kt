package htnk128.kotlin.ddd.sample.account.domain.model.account

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row
import java.time.Instant

class AccountAddressSpec : StringSpec({

    "アカウントの住所が有効な場合の判定が想定通りであること" {
        forall(
            row(Instant.now(), false),
            row(null, true)
        ) { deletedAt, expected ->
            AccountAddress(
                AccountAddressId.valueOf("address01"),
                deletedAt
            ).isAvailable shouldBe expected
        }
    }

    "同じ値を持つ場合は等価となる" {
        val addressOwnerId = AccountAddressId.valueOf("address01")
        val deletedAt = Instant.now()
        val data1 = AccountAddress(
            addressOwnerId,
            deletedAt
        )
        val data2 = AccountAddress(
            addressOwnerId,
            deletedAt
        )

        data1 shouldBe data2
    }

    "同じ値でない場合は等価とならない" {
        val deletedAt = Instant.now()
        val data1 = AccountAddress(
            AccountAddressId.valueOf("address01"),
            deletedAt
        )
        val data2 = AccountAddress(
            AccountAddressId.valueOf("address02"),
            deletedAt
        )

        data1 shouldNotBe data2
    }
})
