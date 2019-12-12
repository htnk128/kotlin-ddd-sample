package htnk128.kotlin.ddd.sample.customer.domain.model.customer

import htnk128.kotlin.ddd.sample.dddcore.domain.Entity
import java.time.Instant

/**
 * 顧客を表現する。
 *
 * 氏名または会社名、発音などの情報を指定して作成することが可能である。
 * また、顧客の作成後は更新、削除が可能である。
 */
class Customer(
    val customerId: CustomerId,
    val name: Name,
    val namePronunciation: NamePronunciation,
    val email: Email,
    val createdAt: Instant,
    val deletedAt: Instant? = null,
    val updatedAt: Instant
) : Entity<Customer> {

    private val events = mutableListOf<CustomerEvent<*>>()

    /**
     * この顧客が削除されている場合に`true`を返す。
     */
    val isDeleted: Boolean = deletedAt != null

    /**
     * 顧客を更新する。
     *
     * 氏名または会社名、発音、メールアドレスを更新可能で、すべて任意指定が可能であり指定しなかった場合は現在の値のままとなる。
     *
     * また、更新後にイベント([CustomerUpdated])を生成する。
     *
     * @return 更新された顧客
     */
    fun update(
        name: Name?,
        namePronunciation: NamePronunciation?,
        email: Email?
    ): Customer = Customer(
        customerId,
        name = name ?: this.name,
        namePronunciation = namePronunciation ?: this.namePronunciation,
        email = email ?: this.email,
        createdAt = this.createdAt,
        updatedAt = Instant.now()
    )
        .addEvent(CustomerEvent.Type.UPDATED)

    /**
     * 顧客を削除する。
     *
     * 削除日時([deletedAt])に現在日付が設定されことによって論理削除状態となる。
     *
     * また、更新後にイベント([CustomerDeleted])を生成する。
     *
     * @return 削除された顧客
     */
    fun delete(): Customer = with(Instant.now()) {
        Customer(
            customerId,
            name = name,
            namePronunciation = namePronunciation,
            email = email,
            createdAt = createdAt,
            deletedAt = this,
            updatedAt = this
        )
    }

    /**
     * 発生したイベントを返す。
     *
     * @return 発生したイベントのリスト
     */
    fun occurredEvents(): List<CustomerEvent<*>> = events.toList()

    private fun addEvent(type: CustomerEvent.Type): Customer = this
        .also {
            events += when (type) {
                CustomerEvent.Type.CREATED -> CustomerCreated(
                    this
                )
                CustomerEvent.Type.UPDATED -> CustomerUpdated(
                    this
                )
                CustomerEvent.Type.DELETED -> CustomerDeleted(
                    this
                )
            }
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Customer
        return sameIdentityAs(other)
    }

    override fun hashCode(): Int = customerId.hashCode()

    override fun sameIdentityAs(other: Customer): Boolean = customerId == other.customerId

    companion object {

        /**
         * 顧客を作成する。
         *
         * 名前、発音、メールアドレスすべての項目が必須指定となる。
         *
         * また、作成後にイベント([CustomerCreated])を生成する。
         *
         * @return 作成された顧客
         */
        fun create(
            customerId: CustomerId,
            name: Name,
            namePronunciation: NamePronunciation,
            email: Email
        ): Customer = with(Instant.now()) {
            Customer(
                customerId,
                name,
                namePronunciation,
                email,
                createdAt = this,
                updatedAt = this
            )
        }
            .addEvent(CustomerEvent.Type.CREATED)
    }
}
