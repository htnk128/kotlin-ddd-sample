package htnk128.kotlin.ddd.sample.address.domain.model.address

import htnk128.kotlin.ddd.sample.address.domain.exception.AddressInvalidDataStateException
import htnk128.kotlin.ddd.sample.address.domain.model.account.AccountId
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import java.time.Instant

class AddressSpec : StringSpec({

    "住所が作成されること" {
        val now = Instant.now()
        val addressId = AddressId.generate()
        val accountId = AccountId.valueOf("account01")
        val fullName = FullName.valueOf("あいうえお")
        val zipCode = ZipCode.valueOf("1234567")
        val stateOrRegion = StateOrRegion.valueOf("かきくけこ")
        val line1 = Line1.valueOf("さしすせそ")
        val line2 = Line2.valueOf("たちつてと")
        val phoneNumber = PhoneNumber.valueOf("11111111111")

        val address = Address.create(
            addressId,
            accountId,
            fullName,
            zipCode,
            stateOrRegion,
            line1,
            line2,
            phoneNumber
        )

        address.addressId shouldBe addressId
        address.accountId shouldBe accountId
        address.fullName shouldBe fullName
        address.zipCode shouldBe zipCode
        address.stateOrRegion shouldBe stateOrRegion
        address.line1 shouldBe line1
        address.line2 shouldBe line2
        address.phoneNumber shouldBe phoneNumber
        (address.createdAt >= now) shouldBe true
        (address.updatedAt >= now) shouldBe true
        val events = address.occurredEvents()

        events.size shouldBe 1
        events[0]
            .also {
                it.type shouldBe AddressEvent.Type.CREATED
                it.address.addressId shouldBe addressId
                it.address.accountId shouldBe accountId
                it.address.fullName shouldBe fullName
                it.address.zipCode shouldBe zipCode
                it.address.stateOrRegion shouldBe stateOrRegion
                it.address.line1 shouldBe line1
                it.address.line2 shouldBe line2
                (it.address.createdAt >= now) shouldBe true
                (it.address.updatedAt >= now) shouldBe true
            }
    }

    "住所が更新されること" {
        val now = Instant.now()
        val fullName2 = FullName.valueOf("あいうえおa")
        val zipCode2 = ZipCode.valueOf("12345678")
        val stateOrRegion2 = StateOrRegion.valueOf("かきくけこb")
        val line12 = Line1.valueOf("さしすせそc")
        val line22 = Line2.valueOf("たちつてとd")
        val phoneNumber2 = PhoneNumber.valueOf("111111111112")

        val created = Address.create(
            AddressId.generate(),
            AccountId.valueOf("account01"),
            FullName.valueOf("あいうえお"),
            ZipCode.valueOf("1234567"),
            StateOrRegion.valueOf("かきくけこ"),
            Line1.valueOf("さしすせそ"),
            Line2.valueOf("たちつてと"),
            PhoneNumber.valueOf("11111111111")
        )

        val updated = created.update(
            fullName2,
            zipCode2,
            stateOrRegion2,
            line12,
            line22,
            phoneNumber2
        )

        updated.accountId shouldBe created.accountId
        updated.fullName shouldBe fullName2
        updated.zipCode shouldBe zipCode2
        updated.stateOrRegion shouldBe stateOrRegion2
        updated.line1 shouldBe line12
        updated.line2 shouldBe line22
        (updated.createdAt >= now) shouldBe true
        (updated.updatedAt >= now) shouldBe true
        val events = updated.occurredEvents()

        events.size shouldBe 2
        events[1]
            .also {
                it.type shouldBe AddressEvent.Type.UPDATED
                it.address.accountId shouldBe created.accountId
                it.address.fullName shouldBe fullName2
                it.address.zipCode shouldBe zipCode2
                it.address.stateOrRegion shouldBe stateOrRegion2
                it.address.line1 shouldBe line12
                it.address.line2 shouldBe line22
                it.address.phoneNumber shouldBe phoneNumber2
                (it.address.createdAt >= now) shouldBe true
                (it.address.updatedAt >= now) shouldBe true
            }
    }

    "任意項目を指定しなかった場合の住所の更新は既存値であること" {
        val now = Instant.now()

        val created = Address.create(
            AddressId.generate(),
            AccountId.valueOf("account01"),
            FullName.valueOf("あいうえお"),
            ZipCode.valueOf("1234567"),
            StateOrRegion.valueOf("かきくけこ"),
            Line1.valueOf("さしすせそ"),
            Line2.valueOf("たちつてと"),
            PhoneNumber.valueOf("11111111111")
        )

        val updated = created.update(
            null,
            null,
            null,
            null,
            null,
            null
        )

        updated.accountId shouldBe created.accountId
        updated.fullName shouldBe created.fullName
        updated.zipCode shouldBe created.zipCode
        updated.stateOrRegion shouldBe created.stateOrRegion
        updated.line1 shouldBe created.line1
        updated.line2 shouldBe null
        (updated.createdAt >= now) shouldBe true
        (updated.updatedAt >= now) shouldBe true
        val events = updated.occurredEvents()

        events.size shouldBe 2
        events[1]
            .also {
                it.type shouldBe AddressEvent.Type.UPDATED
                it.address.accountId shouldBe created.accountId
                it.address.fullName shouldBe created.fullName
                it.address.zipCode shouldBe created.zipCode
                it.address.stateOrRegion shouldBe created.stateOrRegion
                it.address.line1 shouldBe created.line1
                it.address.line2 shouldBe null
                it.address.phoneNumber shouldBe created.phoneNumber
                (it.address.createdAt >= now) shouldBe true
                (it.address.updatedAt >= now) shouldBe true
            }
    }

    "この住所が削除されている場合には例外となること" {
        val deleted = Address.create(
            AddressId.generate(),
            AccountId.valueOf("account01"),
            FullName.valueOf("あいうえお"),
            ZipCode.valueOf("1234567"),
            StateOrRegion.valueOf("かきくけこ"),
            Line1.valueOf("さしすせそ"),
            Line2.valueOf("たちつてと"),
            PhoneNumber.valueOf("11111111111")
        )
            .delete()

        shouldThrow<AddressInvalidDataStateException> {
            deleted.update(
                FullName.valueOf("あいうえおa"),
                ZipCode.valueOf("12345678"),
                StateOrRegion.valueOf("かきくけこb"),
                Line1.valueOf("さしすせそc"),
                Line2.valueOf("たちつてとd"),
                PhoneNumber.valueOf("111111111112")
            )
        }
    }

    "住所が削除されること" {
        val now = Instant.now()

        val created = Address.create(
            AddressId.generate(),
            AccountId.valueOf("account01"),
            FullName.valueOf("あいうえお"),
            ZipCode.valueOf("1234567"),
            StateOrRegion.valueOf("かきくけこ"),
            Line1.valueOf("さしすせそ"),
            Line2.valueOf("たちつてと"),
            PhoneNumber.valueOf("11111111111")
        )

        val deleted = created.delete()

        deleted.accountId shouldBe created.accountId
        deleted.fullName shouldBe created.fullName
        deleted.zipCode shouldBe created.zipCode
        deleted.stateOrRegion shouldBe created.stateOrRegion
        deleted.line1 shouldBe created.line1
        deleted.line2 shouldBe created.line2
        deleted.isDeleted shouldBe true
        (deleted.createdAt >= now) shouldBe true
        (deleted.updatedAt >= now) shouldBe true
        val events = deleted.occurredEvents()

        events.size shouldBe 2
        events[1]
            .also {
                it.type shouldBe AddressEvent.Type.DELETED
                it.address.accountId shouldBe created.accountId
                it.address.fullName shouldBe created.fullName
                it.address.zipCode shouldBe created.zipCode
                it.address.stateOrRegion shouldBe created.stateOrRegion
                it.address.line1 shouldBe created.line1
                it.address.line2 shouldBe created.line2
                it.address.phoneNumber shouldBe created.phoneNumber
                (it.address.createdAt >= now) shouldBe true
                (it.address.updatedAt >= now) shouldBe true
            }
    }

    "この住所が削除済みの場合にはそのままこのアカウントが返却されること" {
        val deleted = Address.create(
            AddressId.generate(),
            AccountId.valueOf("account01"),
            FullName.valueOf("あいうえお"),
            ZipCode.valueOf("1234567"),
            StateOrRegion.valueOf("かきくけこ"),
            Line1.valueOf("さしすせそ"),
            Line2.valueOf("たちつてと"),
            PhoneNumber.valueOf("11111111111")
        )
            .delete()

        val deleted2 = deleted.delete()

        deleted2.accountId shouldBe deleted.accountId
        deleted2.fullName shouldBe deleted.fullName
        deleted2.zipCode shouldBe deleted.zipCode
        deleted2.stateOrRegion shouldBe deleted.stateOrRegion
        deleted2.line1 shouldBe deleted.line1
        deleted2.line2 shouldBe deleted.line2
        deleted2.phoneNumber shouldBe deleted.phoneNumber
        deleted2.createdAt shouldBe deleted.createdAt
        deleted2.updatedAt shouldBe deleted.updatedAt
        deleted2.isDeleted shouldBe true
        deleted2.occurredEvents().size shouldBe 2
    }

    "属性が異なっても一意な識別子が一緒であれば等価となる" {
        val addressId = AddressId.generate()
        val address1 = Address.create(
            addressId,
            AccountId.valueOf("account01"),
            FullName.valueOf("あいうえお"),
            ZipCode.valueOf("1234567"),
            StateOrRegion.valueOf("かきくけこ"),
            Line1.valueOf("さしすせそ"),
            Line2.valueOf("たちつてと"),
            PhoneNumber.valueOf("11111111111")
        )
        val address2 = Address.create(
            addressId,
            AccountId.valueOf("account02"),
            FullName.valueOf("あいうえお1"),
            ZipCode.valueOf("12345678"),
            StateOrRegion.valueOf("かきくけこ2"),
            Line1.valueOf("さしすせそ3"),
            Line2.valueOf("たちつてと4"),
            PhoneNumber.valueOf("111111111115")
        )

        (address1 == address2) shouldBe true
        (address1.sameIdentityAs(address2)) shouldBe true
    }

    "属性が同一でも一意な識別子が異なれば等価とならない" {
        val address1 = Address.create(
            AddressId.generate(),
            AccountId.valueOf("account01"),
            FullName.valueOf("あいうえお"),
            ZipCode.valueOf("1234567"),
            StateOrRegion.valueOf("かきくけこ"),
            Line1.valueOf("さしすせそ"),
            Line2.valueOf("たちつてと"),
            PhoneNumber.valueOf("11111111111")
        )
        val address2 = Address.create(
            AddressId.generate(),
            AccountId.valueOf("account01"),
            FullName.valueOf("あいうえお"),
            ZipCode.valueOf("1234567"),
            StateOrRegion.valueOf("かきくけこ"),
            Line1.valueOf("さしすせそ"),
            Line2.valueOf("たちつてと"),
            PhoneNumber.valueOf("11111111111")
        )

        (address1 == address2) shouldBe false
        (address1.sameIdentityAs(address2)) shouldBe false
    }
})
