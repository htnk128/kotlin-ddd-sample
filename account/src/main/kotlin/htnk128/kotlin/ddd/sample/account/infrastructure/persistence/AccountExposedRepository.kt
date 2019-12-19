package htnk128.kotlin.ddd.sample.account.infrastructure.persistence

import htnk128.kotlin.ddd.sample.account.domain.model.account.Account
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountId
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountNotFoundException
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountRepository
import htnk128.kotlin.ddd.sample.account.domain.model.account.Email
import htnk128.kotlin.ddd.sample.account.domain.model.account.Name
import htnk128.kotlin.ddd.sample.account.domain.model.account.NamePronunciation
import htnk128.kotlin.ddd.sample.account.domain.model.account.Password
import htnk128.kotlin.ddd.sample.shared.infrastructure.persistence.ExposedTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Repository
@Transactional
class AccountExposedRepository : AccountRepository {

    override fun find(accountId: AccountId, lock: Boolean): Account =
        AccountTable.select { AccountTable.accountId eq accountId.id() }
            .run { if (lock) this.forUpdate() else this }
            .firstOrNull()
            ?.rowToModel()
            ?: throw AccountNotFoundException(accountId)

    override fun findAll(limit: Int, offset: Int): List<Account> =
        AccountTable.selectAll()
            .orderBy(AccountTable.createdAt)
            .limit(limit, offset = offset * limit)
            .map { it.rowToModel() }

    override fun count(): Int =
        AccountTable.selectAll()
            .count()

    override fun add(account: Account) {
        AccountTable.insert {
            it[accountId] = account.accountId.id()
            it[name] = account.name.toValue()
            it[namePronunciation] = account.namePronunciation.toValue()
            it[email] = account.email.toValue()
            it[password] = account.password.toValue()
            it[createdAt] = account.createdAt
            it[deletedAt] = account.deletedAt
            it[updatedAt] = account.updatedAt
        }
    }

    override fun set(account: Account): Int =
        AccountTable.update({ AccountTable.accountId eq account.accountId.id() }) {
            it[name] = account.name.toValue()
            it[namePronunciation] = account.namePronunciation.toValue()
            it[email] = account.email.toValue()
            it[password] = account.password.toValue()
            it[updatedAt] = account.updatedAt
        }

    override fun remove(account: Account): Int =
        AccountTable.update({ AccountTable.accountId eq account.accountId.id() }) {
            it[deletedAt] = account.deletedAt
            it[updatedAt] = account.updatedAt
        }

    private fun ResultRow.rowToModel(): Account =
        Account(
            AccountId.valueOf(this[AccountTable.accountId]),
            Name.valueOf(this[AccountTable.name]),
            NamePronunciation.valueOf(this[AccountTable.namePronunciation]),
            Email.valueOf(this[AccountTable.email]),
            Password.from(this[AccountTable.password]),
            this[AccountTable.createdAt],
            this[AccountTable.deletedAt],
            this[AccountTable.updatedAt]
        )
}

private object AccountTable : ExposedTable<Account>("account") {

    val accountId: Column<String> = varchar("account_id", length = 64).primaryKey()
    val name: Column<String> = varchar("name", length = 100)
    val namePronunciation: Column<String> = varchar("name_pronunciation", length = 100)
    val email: Column<String> = varchar("email", length = 100)
    val password: Column<String> = varchar("password", length = 64)
    val createdAt: Column<Instant> = instant("created_at")
    val deletedAt: Column<Instant?> = instant("deleted_at").nullable()
    val updatedAt: Column<Instant> = instant("updated_at")
}
