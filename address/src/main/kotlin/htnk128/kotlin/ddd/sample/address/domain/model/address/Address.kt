package htnk128.kotlin.ddd.sample.address.domain.model.address

import htnk128.kotlin.ddd.sample.dddcore.domain.Entity
import java.time.Instant

/**
 * 住所を表現する。
 *
 * 氏名または会社名、郵便番号などの情報を指定して作成することが可能である。
 * また、住所の作成後は更新、削除が可能である。
 */
class Address(
    val addressId: AddressId,
    val addressOwnerId: AddressOwnerId,
    val fullName: FullName,
    val zipCode: ZipCode,
    val stateOrRegion: StateOrRegion,
    val line1: Line1,
    val line2: Line2?,
    val phoneNumber: PhoneNumber,
    val createdAt: Instant,
    val deletedAt: Instant? = null,
    val updatedAt: Instant
) : Entity<Address> {

    private val events = mutableListOf<AddressEvent<*>>()

    /**
     * この住所が削除されている場合に`true`を返す。
     */
    val isDeleted: Boolean = deletedAt != null

    /**
     * 住所を更新する。
     *
     * 氏名または会社名、郵便番号、都道府県、住所欄1、住所欄2、電話番号を更新可能で、すべて任意指定が可能であり指定しなかった場合は現在の値のままとなる。
     * ただし、住所欄2は`null`での更新が可能となる。
     * 更新後にイベント([AddressUpdated])を生成する。
     *
     * また、この住所が削除されている場合には例外となる。
     *
     * @return 更新された住所
     * @throws AddressInvalidDataStateException
     */
    fun update(
        fullName: FullName?,
        zipCode: ZipCode?,
        stateOrRegion: StateOrRegion?,
        line1: Line1?,
        line2: Line2?,
        phoneNumber: PhoneNumber?
    ): Address {
        if (isDeleted) throw AddressInvalidDataStateException("Address has been deleted.")

        return Address(
            addressId,
            addressOwnerId,
            fullName = fullName ?: this.fullName,
            zipCode = zipCode ?: this.zipCode,
            stateOrRegion = stateOrRegion ?: this.stateOrRegion,
            line1 = line1 ?: this.line1,
            line2 = line2,
            phoneNumber = phoneNumber ?: this.phoneNumber,
            createdAt = this.createdAt,
            updatedAt = Instant.now()
        )
            .addEvent(AddressEvent.Type.UPDATED, events.toList())
    }

    /**
     * 住所を削除する。
     *
     * 削除日時([deletedAt])に現在日付が設定されことによって論理削除状態となる。
     * 更新後にイベント([AddressDeleted])を生成する。
     *
     * また、このアカウントが削除済みの場合にはそのままこのアカウントが返却される。
     *
     * @return 削除された住所
     */
    fun delete(): Address = if (isDeleted) this else with(Instant.now()) {
        Address(
            addressId,
            addressOwnerId,
            fullName = fullName,
            zipCode = zipCode,
            stateOrRegion = stateOrRegion,
            line1 = line1,
            line2 = line2,
            phoneNumber = phoneNumber,
            createdAt = createdAt,
            deletedAt = this,
            updatedAt = this
        )
            .addEvent(AddressEvent.Type.DELETED, events.toList())
    }

    /**
     * 発生したイベントを返す。
     *
     * @return 発生したイベントのリスト
     */
    fun occurredEvents(): List<AddressEvent<*>> = events.toList()

    private fun addEvent(type: AddressEvent.Type, events: List<AddressEvent<*>> = emptyList()): Address = this
        .also {
            this.events += events
            this.events += when (type) {
                AddressEvent.Type.CREATED -> AddressCreated(this)
                AddressEvent.Type.UPDATED -> AddressUpdated(this)
                AddressEvent.Type.DELETED -> AddressDeleted(this)
            }
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Address
        return sameIdentityAs(other)
    }

    override fun hashCode(): Int = addressId.hashCode()

    override fun sameIdentityAs(other: Address): Boolean = addressId == other.addressId

    companion object {

        /**
         * 住所を作成する。
         *
         * 住所欄2を除くすべての項目(アカウントのID、氏名または会社名、郵便番号、都道府県、住所欄1、電話番号)が必須指定となる。
         *
         * また、作成後にイベント([AddressCreated])を生成する。
         *
         * @return 作成された住所
         */
        fun create(
            addressId: AddressId,
            addressOwnerId: AddressOwnerId,
            fullName: FullName,
            zipCode: ZipCode,
            stateOrRegion: StateOrRegion,
            line1: Line1,
            line2: Line2?,
            phoneNumber: PhoneNumber
        ): Address = with(Instant.now()) {
            Address(
                addressId,
                addressOwnerId,
                fullName,
                zipCode,
                stateOrRegion,
                line1,
                line2,
                phoneNumber,
                createdAt = this,
                updatedAt = this
            )
        }
            .addEvent(AddressEvent.Type.CREATED)
    }
}
