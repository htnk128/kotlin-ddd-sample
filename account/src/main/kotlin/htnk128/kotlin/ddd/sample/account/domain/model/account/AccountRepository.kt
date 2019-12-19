package htnk128.kotlin.ddd.sample.account.domain.model.account

/**
 * アカウントを操作するためのリポジトリを表現する。
 */
interface AccountRepository {

    fun find(accountId: AccountId, lock: Boolean = false): Account

    fun findAll(limit: Int, offset: Int): List<Account>

    fun count(): Int

    fun add(account: Account)

    fun set(account: Account)

    fun remove(account: Account)

    fun nextAccountId(): AccountId = AccountId.generate()
}
