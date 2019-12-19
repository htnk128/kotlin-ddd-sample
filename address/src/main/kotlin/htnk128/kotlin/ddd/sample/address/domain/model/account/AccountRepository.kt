package htnk128.kotlin.ddd.sample.address.domain.model.account

/**
 * アカウントを操作するためのリポジトリを表現する。
 */
interface AccountRepository {

    fun find(accountId: AccountId): Account
}
