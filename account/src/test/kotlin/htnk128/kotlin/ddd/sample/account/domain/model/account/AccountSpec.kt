package htnk128.kotlin.ddd.sample.account.domain.model.account

import htnk128.kotlin.ddd.sample.account.domain.exception.AccountInvalidDataStateException
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.kotlintest.specs.StringSpec
import java.time.Instant

class AccountSpec : StringSpec({

    "アカウントが作成されること" {
        val now = Instant.now()
        val accountId = AccountId.generate()
        val name = Name.valueOf("あいうえお")
        val namePronunciation = NamePronunciation.valueOf("アイウエオ")
        val email = Email.valueOf("example@example.com")
        val password = Password.valueOf("a".repeat(100), accountId)

        val account = Account.create(
            accountId,
            name,
            namePronunciation,
            email,
            password
        )

        account.accountId shouldBe accountId
        account.name shouldBe name
        account.namePronunciation shouldBe namePronunciation
        account.email shouldBe email
        account.password shouldBe password
        (account.createdAt >= now) shouldBe true
        (account.updatedAt >= now) shouldBe true
        val events = account.occurredEvents()

        events.size shouldBe 1
        events[0]
            .also {
                it.type shouldBe AccountEvent.Type.CREATED
                it.account.accountId shouldBe accountId
                it.account.name shouldBe name
                it.account.namePronunciation shouldBe namePronunciation
                it.account.email shouldBe email
                it.account.password shouldBe password
                (it.account.createdAt >= now) shouldBe true
                (it.account.updatedAt >= now) shouldBe true
            }
    }

    "アカウントが更新されること" {
        val now = Instant.now()
        val accountId = AccountId.generate()
        val name2 = Name.valueOf("あいうえおa")
        val namePronunciation2 = NamePronunciation.valueOf("アイウエオb")
        val email2 = Email.valueOf("example@example.comc")
        val password2 = Password.valueOf("b".repeat(100), accountId)

        val created = Account.create(
            accountId,
            Name.valueOf("あいうえお"),
            NamePronunciation.valueOf("アイウエオ"),
            Email.valueOf("example@example.com"),
            Password.valueOf("a".repeat(100), accountId)
        )

        val updated = created.update(
            name2,
            namePronunciation2,
            email2,
            password2
        )

        updated.accountId shouldBe created.accountId
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
                it.type shouldBe AccountEvent.Type.UPDATED
                it.account.accountId shouldBe created.accountId
                it.account.name shouldBe name2
                it.account.namePronunciation shouldBe namePronunciation2
                it.account.email shouldBe email2
                it.account.password shouldBe password2
                (it.account.createdAt >= now) shouldBe true
                (it.account.updatedAt >= now) shouldBe true
            }
    }

    "任意項目を指定しなかった場合のアカウントの更新は既存値であること" {
        val now = Instant.now()
        val accountId = AccountId.generate()

        val created = Account.create(
            accountId,
            Name.valueOf("あいうえお"),
            NamePronunciation.valueOf("アイウエオ"),
            Email.valueOf("example@example.com"),
            Password.valueOf("a".repeat(100), accountId)
        )

        val updated = created.update(
            null,
            null,
            null,
            null
        )

        updated.accountId shouldBe created.accountId
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
                it.type shouldBe AccountEvent.Type.UPDATED
                it.account.accountId shouldBe created.accountId
                it.account.name shouldBe created.name
                it.account.namePronunciation shouldBe created.namePronunciation
                it.account.email shouldBe created.email
                it.account.password shouldBe created.password
                (it.account.createdAt >= now) shouldBe true
                (it.account.updatedAt >= now) shouldBe true
            }
    }

    "このアカウントが削除されている場合には例外となること" {
        val accountId = AccountId.generate()
        val deleted = Account.create(
            accountId,
            Name.valueOf("あいうえお"),
            NamePronunciation.valueOf("アイウエオ"),
            Email.valueOf("example@example.com"),
            Password.valueOf("a".repeat(100), accountId)
        )
            .delete()

        shouldThrow<AccountInvalidDataStateException> {
            deleted.update(
                Name.valueOf("あいうえおa"),
                NamePronunciation.valueOf("アイウエオb"),
                Email.valueOf("example@example.comc"),
                Password.valueOf("b".repeat(100), accountId)
            )
        }
    }

    "アカウントが削除されること" {
        val now = Instant.now()
        val accountId = AccountId.generate()

        val created = Account.create(
            accountId,
            Name.valueOf("あいうえお"),
            NamePronunciation.valueOf("アイウエオ"),
            Email.valueOf("example@example.com"),
            Password.valueOf("a".repeat(100), accountId)
        )

        val deleted = created.delete()

        deleted.accountId shouldBe created.accountId
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
                it.type shouldBe AccountEvent.Type.DELETED
                it.account.accountId shouldBe created.accountId
                it.account.name shouldBe created.name
                it.account.namePronunciation shouldBe created.namePronunciation
                it.account.email shouldBe created.email
                it.account.password shouldBe created.password
                (it.account.createdAt >= now) shouldBe true
                (it.account.updatedAt >= now) shouldBe true
            }
    }

    "このアカウントが削除済みの場合にはそのままこのアカウントが返却されること" {
        val accountId = AccountId.generate()
        val deleted = Account.create(
            accountId,
            Name.valueOf("あいうえお"),
            NamePronunciation.valueOf("アイウエオ"),
            Email.valueOf("example@example.com"),
            Password.valueOf("a".repeat(100), accountId)
        )
            .delete()

        val deleted2 = deleted.delete()

        deleted2.accountId shouldBe deleted.accountId
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
        val accountId = AccountId.generate()
        val account1 = Account.create(
            accountId,
            Name.valueOf("あいうえお"),
            NamePronunciation.valueOf("アイウエオ"),
            Email.valueOf("example@example.com"),
            Password.valueOf("a".repeat(100), accountId)
        )
        val account2 = Account.create(
            accountId,
            Name.valueOf("あいうえお1"),
            NamePronunciation.valueOf("アイウエオ2"),
            Email.valueOf("example@example.com3"),
            Password.valueOf("b".repeat(100), accountId)
        )

        (account1 == account2) shouldBe true
        (account1.sameIdentityAs(account2)) shouldBe true
    }

    "属性が同一でも一意な識別子が異なれば等価とならない" {
        val accountId1 = AccountId.generate()
        val accountId2 = AccountId.generate()
        val password = Password.valueOf("a".repeat(100), accountId1)
        val account1 = Account.create(
            accountId1,
            Name.valueOf("あいうえお"),
            NamePronunciation.valueOf("アイウエオ"),
            Email.valueOf("example@example.com"),
            password
        )
        val account2 = Account.create(
            accountId2,
            Name.valueOf("あいうえお"),
            NamePronunciation.valueOf("アイウエオ"),
            Email.valueOf("example@example.com"),
            password
        )

        (account1 == account2) shouldBe false
        (account1.sameIdentityAs(account2)) shouldBe false
    }
})
