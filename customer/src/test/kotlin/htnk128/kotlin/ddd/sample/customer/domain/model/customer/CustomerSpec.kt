package htnk128.kotlin.ddd.sample.customer.domain.model.customer

import htnk128.kotlin.ddd.sample.customer.domain.exception.CustomerInvalidDataStateException
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import java.time.Instant

class CustomerSpec : StringSpec({

    "顧客が作成されること" {
        val now = Instant.now()
        val customerId = CustomerId.generate()
        val name = Name.valueOf("あいうえお")
        val namePronunciation = NamePronunciation.valueOf("アイウエオ")
        val email = Email.valueOf("example@example.com")
        val password = Password.valueOf("a".repeat(100), customerId)

        val customer = Customer.create(
            customerId,
            name,
            namePronunciation,
            email,
            password
        )

        customer.customerId shouldBe customerId
        customer.name shouldBe name
        customer.namePronunciation shouldBe namePronunciation
        customer.email shouldBe email
        customer.password shouldBe password
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
                it.customer.password shouldBe password
                (it.customer.createdAt >= now) shouldBe true
                (it.customer.updatedAt >= now) shouldBe true
            }
    }

    "顧客が更新されること" {
        val now = Instant.now()
        val customerId = CustomerId.generate()
        val name2 = Name.valueOf("あいうえおa")
        val namePronunciation2 = NamePronunciation.valueOf("アイウエオb")
        val email2 = Email.valueOf("example@example.comc")
        val password2 = Password.valueOf("b".repeat(100), customerId)

        val created = Customer.create(
            customerId,
            Name.valueOf("あいうえお"),
            NamePronunciation.valueOf("アイウエオ"),
            Email.valueOf("example@example.com"),
            Password.valueOf("a".repeat(100), customerId)
        )

        val updated = created.update(
            name2,
            namePronunciation2,
            email2,
            password2
        )

        updated.customerId shouldBe created.customerId
        updated.name shouldBe name2
        updated.namePronunciation shouldBe namePronunciation2
        updated.email shouldBe email2
        updated.password shouldBe password2
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
                it.customer.password shouldBe password2
                (it.customer.createdAt >= now) shouldBe true
                (it.customer.updatedAt >= now) shouldBe true
            }
    }

    "任意項目を指定しなかった場合の顧客の更新は既存値であること" {
        val now = Instant.now()
        val customerId = CustomerId.generate()

        val created = Customer.create(
            customerId,
            Name.valueOf("あいうえお"),
            NamePronunciation.valueOf("アイウエオ"),
            Email.valueOf("example@example.com"),
            Password.valueOf("a".repeat(100), customerId)
        )

        val updated = created.update(
            null,
            null,
            null,
            null
        )

        updated.customerId shouldBe created.customerId
        updated.name shouldBe created.name
        updated.namePronunciation shouldBe created.namePronunciation
        updated.email shouldBe created.email
        updated.password shouldBe created.password
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
                it.customer.password shouldBe created.password
                (it.customer.createdAt >= now) shouldBe true
                (it.customer.updatedAt >= now) shouldBe true
            }
    }

    "この顧客が削除されている場合には例外となること" {
        val customerId = CustomerId.generate()
        val deleted = Customer.create(
            customerId,
            Name.valueOf("あいうえお"),
            NamePronunciation.valueOf("アイウエオ"),
            Email.valueOf("example@example.com"),
            Password.valueOf("a".repeat(100), customerId)
        )
            .delete()

        shouldThrow<CustomerInvalidDataStateException> {
            deleted.update(
                Name.valueOf("あいうえおa"),
                NamePronunciation.valueOf("アイウエオb"),
                Email.valueOf("example@example.comc"),
                Password.valueOf("b".repeat(100), customerId)
            )
        }
    }

    "顧客が削除されること" {
        val now = Instant.now()
        val customerId = CustomerId.generate()

        val created = Customer.create(
            customerId,
            Name.valueOf("あいうえお"),
            NamePronunciation.valueOf("アイウエオ"),
            Email.valueOf("example@example.com"),
            Password.valueOf("a".repeat(100), customerId)
        )

        val deleted = created.delete()

        deleted.customerId shouldBe created.customerId
        deleted.name shouldBe created.name
        deleted.namePronunciation shouldBe created.namePronunciation
        deleted.email shouldBe created.email
        deleted.password shouldBe created.password
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
                it.customer.password shouldBe created.password
                (it.customer.createdAt >= now) shouldBe true
                (it.customer.updatedAt >= now) shouldBe true
            }
    }

    "この顧客が削除済みの場合にはそのままこの顧客が返却されること" {
        val customerId = CustomerId.generate()
        val deleted = Customer.create(
            customerId,
            Name.valueOf("あいうえお"),
            NamePronunciation.valueOf("アイウエオ"),
            Email.valueOf("example@example.com"),
            Password.valueOf("a".repeat(100), customerId)
        )
            .delete()

        val deleted2 = deleted.delete()

        deleted2.customerId shouldBe deleted.customerId
        deleted2.name shouldBe deleted.name
        deleted2.namePronunciation shouldBe deleted.namePronunciation
        deleted2.email shouldBe deleted.email
        deleted2.password shouldBe deleted.password
        deleted2.createdAt shouldBe deleted.createdAt
        deleted2.updatedAt shouldBe deleted.updatedAt
        deleted2.isDeleted shouldBe true
        deleted2.occurredEvents().size shouldBe 2
    }

    "属性が異なっても一意な識別子が一緒であれば等価となる" {
        val customerId = CustomerId.generate()
        val customer1 = Customer.create(
            customerId,
            Name.valueOf("あいうえお"),
            NamePronunciation.valueOf("アイウエオ"),
            Email.valueOf("example@example.com"),
            Password.valueOf("a".repeat(100), customerId)
        )
        val customer2 = Customer.create(
            customerId,
            Name.valueOf("あいうえお1"),
            NamePronunciation.valueOf("アイウエオ2"),
            Email.valueOf("example@example.com3"),
            Password.valueOf("b".repeat(100), customerId)
        )

        (customer1 == customer2) shouldBe true
        (customer1.sameIdentityAs(customer2)) shouldBe true
    }

    "属性が同一でも一意な識別子が異なれば等価とならない" {
        val customerId1 = CustomerId.generate()
        val customerId2 = CustomerId.generate()
        val password = Password.valueOf("a".repeat(100), customerId1)
        val customer1 = Customer.create(
            customerId1,
            Name.valueOf("あいうえお"),
            NamePronunciation.valueOf("アイウエオ"),
            Email.valueOf("example@example.com"),
            password
        )
        val customer2 = Customer.create(
            customerId2,
            Name.valueOf("あいうえお"),
            NamePronunciation.valueOf("アイウエオ"),
            Email.valueOf("example@example.com"),
            password
        )

        (customer1 == customer2) shouldBe false
        (customer1.sameIdentityAs(customer2)) shouldBe false
    }
})
