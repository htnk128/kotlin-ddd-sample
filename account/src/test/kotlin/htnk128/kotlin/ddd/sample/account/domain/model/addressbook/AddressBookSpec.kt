package htnk128.kotlin.ddd.sample.account.domain.model.addressbook

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import java.time.Instant

class AddressBookSpec : StringSpec({

    "有効なアカウントの住所一覧が取得できる" {
        val accountAddressId = AccountAddressId.valueOf("address01")

        val data1 = AccountAddress(accountAddressId, deletedAt = null)
        val data2 = AccountAddress(accountAddressId, deletedAt = Instant.now())
        val data3 = AccountAddress(accountAddressId, deletedAt = null)

        val actual = AddressBook(listOf(data1, data2, data3)).availableAccountAddresses

        actual.contains(data1) shouldBe true
        actual.contains(data2) shouldBe false
        actual.contains(data3) shouldBe true
    }
})
