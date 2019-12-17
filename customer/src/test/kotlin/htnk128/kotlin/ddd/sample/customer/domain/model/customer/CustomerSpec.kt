package htnk128.kotlin.ddd.sample.customer.domain.model.customer

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.time.Instant

class CustomerSpec : StringSpec({

    "顧客が作成されること" {
        val now = Instant.now()
        val customerId = CustomerId.generate()
        val name = Name.valueOf("あいうえお")
        val namePronunciation = NamePronunciation.valueOf("アイウエオ")
        val email = Email.valueOf("example@example.com")

        val customer = Customer.create(
            customerId,
            name,
            namePronunciation,
            email
        )

        customer.customerId shouldBe customerId
        customer.name shouldBe name
        customer.namePronunciation shouldBe namePronunciation
        customer.email shouldBe email
        (customer.createdAt >= now) shouldBe true
        (customer.updatedAt >= now) shouldBe true
        val events = customer.occurredEvents()

        events.size shouldBe 1
        events[0]
            .also {
                it.type shouldBe CustomerEvent.Type.CREATED
                it.customer.customerId shouldBe customerId
                it.customer.name shouldBe name
                it.customer.namePronunciation shouldBe namePronunciation
                it.customer.email shouldBe email
                (it.customer.createdAt >= now) shouldBe true
                (it.customer.updatedAt >= now) shouldBe true
            }
    }

    "顧客が更新されること" {
        val now = Instant.now()
        val name2 = Name.valueOf("あいうえおa")
        val namePronunciation2 = NamePronunciation.valueOf("アイウエオb")
        val email2 = Email.valueOf("example@example.comc")

        val created = Customer.create(
            CustomerId.generate(),
            Name.valueOf("あいうえお"),
            NamePronunciation.valueOf("アイウエオ"),
            Email.valueOf("example@example.com")
        )

        val updated = created.update(
            name2,
            namePronunciation2,
            email2
        )

        updated.customerId shouldBe created.customerId
        updated.name shouldBe name2
        updated.namePronunciation shouldBe namePronunciation2
        updated.email shouldBe email2
        (updated.createdAt >= now) shouldBe true
        (updated.updatedAt >= now) shouldBe true
        val events = updated.occurredEvents()

        events.size shouldBe 2
        events[1]
            .also {
                it.type shouldBe CustomerEvent.Type.UPDATED
                it.customer.customerId shouldBe created.customerId
                it.customer.name shouldBe name2
                it.customer.namePronunciation shouldBe namePronunciation2
                it.customer.email shouldBe email2
                (it.customer.createdAt >= now) shouldBe true
                (it.customer.updatedAt >= now) shouldBe true
            }
    }

    "任意項目を指定しなかった場合の顧客の更新は既存値であること" {
        val now = Instant.now()

        val created = Customer.create(
            CustomerId.generate(),
            Name.valueOf("あいうえお"),
            NamePronunciation.valueOf("アイウエオ"),
            Email.valueOf("example@example.com")
        )

        val updated = created.update(
            null,
            null,
            null
        )

        updated.customerId shouldBe created.customerId
        updated.name shouldBe created.name
        updated.namePronunciation shouldBe created.namePronunciation
        updated.email shouldBe created.email
        (updated.createdAt >= now) shouldBe true
        (updated.updatedAt >= now) shouldBe true
        val events = updated.occurredEvents()

        events.size shouldBe 2
        events[1]
            .also {
                it.type shouldBe CustomerEvent.Type.UPDATED
                it.customer.customerId shouldBe created.customerId
                it.customer.name shouldBe created.name
                it.customer.namePronunciation shouldBe created.namePronunciation
                it.customer.email shouldBe created.email
                (it.customer.createdAt >= now) shouldBe true
                (it.customer.updatedAt >= now) shouldBe true
            }
    }

    "顧客が削除されること" {
        val now = Instant.now()

        val created = Customer.create(
            CustomerId.generate(),
            Name.valueOf("あいうえお"),
            NamePronunciation.valueOf("アイウエオ"),
            Email.valueOf("example@example.com")
        )

        val deleted = created.delete()

        deleted.customerId shouldBe created.customerId
        deleted.name shouldBe created.name
        deleted.namePronunciation shouldBe created.namePronunciation
        deleted.email shouldBe created.email
        (deleted.createdAt >= now) shouldBe true
        (deleted.updatedAt >= now) shouldBe true
        deleted.isDeleted shouldBe true
        val events = deleted.occurredEvents()

        events.size shouldBe 2
        events[1]
            .also {
                it.type shouldBe CustomerEvent.Type.DELETED
                it.customer.customerId shouldBe created.customerId
                it.customer.name shouldBe created.name
                it.customer.namePronunciation shouldBe created.namePronunciation
                it.customer.email shouldBe created.email
                (it.customer.createdAt >= now) shouldBe true
                (it.customer.updatedAt >= now) shouldBe true
            }
    }

    "属性が異なっても一意な識別子が一緒であれば等価となる" {
        val customerId = CustomerId.generate()
        val customer1 = Customer.create(
            customerId,
            Name.valueOf("あいうえお"),
            NamePronunciation.valueOf("アイウエオ"),
            Email.valueOf("example@example.com")
        )
        val customer2 = Customer.create(
            customerId,
            Name.valueOf("あいうえお1"),
            NamePronunciation.valueOf("アイウエオ2"),
            Email.valueOf("example@example.com3")
        )

        (customer1 == customer2) shouldBe true
        (customer1.sameIdentityAs(customer2)) shouldBe true
    }

    "属性が同一でも一意な識別子が異なれば等価とならない" {
        val customer1 = Customer.create(
            CustomerId.generate(),
            Name.valueOf("あいうえお"),
            NamePronunciation.valueOf("アイウエオ"),
            Email.valueOf("example@example.com")
        )
        val customer2 = Customer.create(
            CustomerId.generate(),
            Name.valueOf("あいうえお"),
            NamePronunciation.valueOf("アイウエオ"),
            Email.valueOf("example@example.com")
        )

        (customer1 == customer2) shouldBe false
        (customer1.sameIdentityAs(customer2)) shouldBe false
    }
})
