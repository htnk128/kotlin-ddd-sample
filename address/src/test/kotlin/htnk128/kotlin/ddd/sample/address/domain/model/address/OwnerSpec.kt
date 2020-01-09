package htnk128.kotlin.ddd.sample.address.domain.model.address

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row
import java.time.Instant

class OwnerSpec : StringSpec({

    "この住所の持ち主が有効な場合の判定が想定通りであること" {
        forall(
            row(Instant.now(), false),
            row(null, true)
        ) { deletedAt, expected ->
            Owner(
                AccountId.valueOf("account01"),
                deletedAt
            ).isAvailable shouldBe expected
        }
    }

    "同じ値を持つ場合は等価となる" {
        val accountId = AccountId.valueOf("account01")
        val deletedAt = Instant.now()
        val data1 = Owner(
            accountId,
            deletedAt
        )
        val data2 = Owner(
            accountId,
            deletedAt
        )

        data1 shouldBe data2
    }

    "同じ値でない場合は等価とならない" {
        val deletedAt = Instant.now()
        val data1 = Owner(
            AccountId.valueOf("account01"),
            deletedAt
        )
        val data2 = Owner(
            AccountId.valueOf("account02"),
            deletedAt
        )

        data1 shouldNotBe data2
    }
})
