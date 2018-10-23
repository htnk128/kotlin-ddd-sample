package htnk128.kotlin.spring.boot.ddd.sample.infrastructure.persistence

import htnk128.kotlin.spring.boot.ddd.sample.domain.model.account.Account
import htnk128.kotlin.spring.boot.ddd.sample.domain.model.account.AccountIdentity
import htnk128.kotlin.spring.boot.ddd.sample.domain.model.account.AccountRepository
import htnk128.kotlin.spring.boot.ddd.sample.domain.model.account.Name
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
class AccountExposedRepository : AccountRepository {

    override fun find(accountId: AccountIdentity): Account? =
        AccountTable.select { AccountTable.accountId eq accountId.value }
            .map { it.toAccountRecord() }
            .firstOrNull()

    override fun findAll(): List<Account> = AccountTable.selectAll().map { it.toAccountRecord() }

    override fun create(account: Account) {
        AccountTable.insert {
            it[accountId] = account.accountId.value
            it[name] = account.name.value
        }
    }

    override fun update(account: Account): Int =
        AccountTable.update({ AccountTable.accountId eq account.accountId.value }) {
            it[name] = account.name.value
        }
}

object AccountTable : Table("account") {

    val accountId: Column<String> = varchar("account_id", length = 100).primaryKey()
    val name: Column<String> = varchar("name", length = 100)

    fun rowToModel(row: ResultRow): Account = Account(
        AccountIdentity(row[accountId]),
        Name(row[name])
    )
}

private fun ResultRow.toAccountRecord() =
    AccountTable.rowToModel(this)
