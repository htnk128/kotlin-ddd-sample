package htnk128.kotlin.ddd.sample.customer.domain.model.customer

import htnk128.kotlin.ddd.sample.customer.domain.exception.CustomerInvalidRequestException
import htnk128.kotlin.ddd.sample.dddcore.domain.SingleValueObject
import java.security.MessageDigest
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * 顧客のパスワードを表現する。
 *
 * 必ず64文字の文字列をもつ。
 */
class Password private constructor(private val value: String) : SingleValueObject<Password, String>() {

    /**
     * 顧客を外部向けのフォーマットに変換する。
     * 必ず"*****"を返す。
     */
    fun format(): String = "*****"

    override fun toValue(): String = value

    companion object {
        private const val ITERATION_COUNT = 100
        private const val KEY_LENGTH = 256

        private val LENGTH_RANGE = (6..100)

        /**
         * [value]に指定された値を顧客のパスワードに変換する。
         *
         * 値には、6〜100桁までの文字列を指定することが可能である。
         *
         * 顧客のID([CustomerId])をソルトとして用いてハッシュ化する。
         *
         * SHA-256を用いているため、文字列変換後は64文字となる。
         *
         * @throws CustomerInvalidRequestException 条件に違反した値を指定した場合
         * @return 指定された値を持つ顧客のパスワード
         */
        fun valueOf(value: String, customerId: CustomerId): Password {
            return value
                .takeIf { LENGTH_RANGE.contains(it.length) }
                ?.let {
                    val secret = it.toCharArray()
                    val salt = MessageDigest.getInstance("SHA-256")
                        .apply { update(customerId.id().toByteArray()) }
                        .digest()

                    Password(
                        SecretKeyFactory
                            .getInstance("PBKDF2WithHmacSHA256")
                            .generateSecret(PBEKeySpec(secret, salt, ITERATION_COUNT, KEY_LENGTH))
                            .encoded
                            .joinToString("") { b -> String.format("%02x", b.toInt() and 255) }
                    )
                }
                ?: throw CustomerInvalidRequestException("Name must be 100 characters or less.")
        }

        /**
         * 指定された値を顧客のパスワードに変換する。
         */
        fun from(value: String): Password = Password(value)
    }
}
