package htnk128.kotlin.ddd.sample.address.domain.model.owner

import io.kotlintest.data.forall
import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.tables.row
import java.time.Instant

class OwnerSpec : StringSpec({

    "住所の持ち主が有効な場合の判定が想定通りであること" {
        forall(
            row(Instant.now(), false),
            row(null, true)
        ) { deletedAt, expected ->
            Owner(
                OwnerId.valueOf("account01"),
                deletedAt
            ).isAvailable shouldBe expected
        }
    }

    "同じ値を持つ場合は等価となる" {
        val ownerId = OwnerId.valueOf("account01")
        val deletedAt = Instant.now()
        val data1 = Owner(
            ownerId,
            deletedAt
        )
        val data2 = Owner(
            ownerId,
            deletedAt
        )

        data1 shouldBe data2
    }

    "同じ値でない場合は等価とならない" {
        val deletedAt = Instant.now()
        val data1 = Owner(
            OwnerId.valueOf("account01"),
            deletedAt
        )
        val data2 = Owner(
            OwnerId.valueOf("account02"),
            deletedAt
        )

        data1 shouldNotBe data2
    }
})
