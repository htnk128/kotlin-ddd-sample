package htnk128.kotlin.ddd.sample.account.domain.model.account

import htnk128.kotlin.ddd.sample.dddcore.domain.Entity
import java.time.Instant

/**
 * アカウントを表現する。
 *
 * 氏名または会社名、発音などの情報を指定して作成することが可能である。
 * また、アカウントの作成後は更新、削除が可能である。
 */
class Account(
    val accountId: AccountId,
    val name: Name,
    val namePronunciation: NamePronunciation,
    val email: Email,
    val password: Password,
    val createdAt: Instant,
    val deletedAt: Instant? = null,
    val updatedAt: Instant
) : Entity<Account> {

    private val events = mutableListOf<AccountEvent<*>>()

    /**
     * このアカウントが削除されている場合に`true`を返す。
     */
    val isDeleted: Boolean = deletedAt != null

    /**
     * アカウントを更新する。
     *
     * 氏名または会社名、発音、メールアドレス、パスワードを更新可能で、すべて任意指定が可能であり指定しなかった場合は現在の値のままとなる。
     * 更新後にイベント([AccountUpdated])を生成する。
     *
     * また、このアカウントが削除されている場合には例外となる。
     *
     * @return 更新されたアカウント
     * @throws AccountInvalidDataStateException
     */
    fun update(
        name: Name?,
        namePronunciation: NamePronunciation?,
        email: Email?,
        password: Password?
    ): Account {
        if (isDeleted) throw AccountInvalidDataStateException(
            "Account has been deleted."
        )

        return Account(
            accountId,
            name = name ?: this.name,
            namePronunciation = namePronunciation ?: this.namePronunciation,
            email = email ?: this.email,
            password = password ?: this.password,
            createdAt = this.createdAt,
            updatedAt = Instant.now()
        )
            .addEvent(AccountEvent.Type.UPDATED, events.toList())
    }

    /**
     * アカウントを削除する。
     *
     * 削除日時([deletedAt])に現在日付が設定されことによって論理削除状態となる。
     * 更新後にイベント([AccountDeleted])を生成する。
     *
     * また、このアカウントが削除済みの場合にはそのままこのアカウントが返却される。
     *
     *  @return 削除されたアカウント
     */
    fun delete(): Account = if (isDeleted) this else with(Instant.now()) {
        Account(
            accountId,
            name = name,
            namePronunciation = namePronunciation,
            email = email,
            password = password,
            createdAt = createdAt,
            deletedAt = this,
            updatedAt = this
        )
            .addEvent(AccountEvent.Type.DELETED, events.toList())
    }

    /**
     * 発生したイベントを返す。
     *
     * @return 発生したイベントのリスト
     */
    fun occurredEvents(): List<AccountEvent<*>> = events.toList()

    private fun addEvent(type: AccountEvent.Type, events: List<AccountEvent<*>> = emptyList()): Account = this
        .also {
            this.events += events
            this.events += when (type) {
                AccountEvent.Type.CREATED -> AccountCreated(this)
                AccountEvent.Type.UPDATED -> AccountUpdated(this)
                AccountEvent.Type.DELETED -> AccountDeleted(this)
            }
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Account
        return sameIdentityAs(other)
    }

    override fun hashCode(): Int = accountId.hashCode()

    override fun sameIdentityAs(other: Account): Boolean = accountId == other.accountId

    override fun toString(): String {
        return buildString {
            append("accountId=$accountId, ")
            append("name=$name, ")
            append("namePronunciation=$namePronunciation, ")
            append("email=$email, ")
            append("password=$password, ")
            append("createdAt=$createdAt, ")
            append("deletedAt=$deletedAt, ")
            append("updatedAt=$updatedAt")
        }
    }

    companion object {

        /**
         * アカウントを作成する。
         *
         * 名前、発音、メールアドレス、パスワードすべての項目が必須指定となる。
         *
         * また、作成後にイベント([AccountCreated])を生成する。
         *
         * @return 作成されたアカウント
         */
        fun create(
            accountId: AccountId,
            name: Name,
            namePronunciation: NamePronunciation,
            email: Email,
            password: Password
        ): Account = with(Instant.now()) {
            Account(
                accountId,
                name,
                namePronunciation,
                email,
                password,
                createdAt = this,
                updatedAt = this
            )
        }
            .addEvent(AccountEvent.Type.CREATED)
    }
}
