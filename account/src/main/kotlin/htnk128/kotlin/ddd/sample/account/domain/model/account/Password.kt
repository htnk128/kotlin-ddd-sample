package htnk128.kotlin.ddd.sample.account.domain.model.account

import htnk128.kotlin.ddd.sample.dddcore.domain.SomeValueObject
import java.security.MessageDigest
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * アカウントのパスワードを表現する。
 *
 * 必ず64文字の文字列をもつ。
 */
class Password private constructor(value: String) : SomeValueObject<Password, String>(value) {

    /**
     * アカウントを外部向けのフォーマットに変換する。
     * 必ず"*****"を返す。
     */
    fun format(): String = "*****"

    companion object {

        private const val ITERATION_COUNT = 100
        private const val KEY_LENGTH = 256

        private val LENGTH_RANGE = (6..100)

        /**
         * [value]に指定された値をアカウントのパスワードに変換する。
         *
         * 値には、6〜100桁までの文字列を指定することが可能である。
         *
         * アカウントのID([AccountId])をソルトとして用いてハッシュ化する。
         *
         * SHA-256を用いているため、文字列変換後は64文字となる。
         *
         * @throws AccountInvalidRequestException 条件に違反した値を指定した場合
         * @return 指定された値を持つアカウントのパスワード
         */
        fun valueOf(value: String, accountId: AccountId): Password {
            return value
                .takeIf { LENGTH_RANGE.contains(it.length) }
                ?.let {
                    val secret = it.toCharArray()
                    val salt = MessageDigest.getInstance("SHA-256")
                        .apply { update(accountId.id().toByteArray()) }
                        .digest()

                    SecretKeyFactory
                        .getInstance("PBKDF2WithHmacSHA256")
                        .generateSecret(PBEKeySpec(secret, salt, ITERATION_COUNT, KEY_LENGTH))
                        .encoded
                        .joinToString("") { b -> String.format("%02x", b.toInt() and 255) }
                }
                ?.let { Password(it) }
                ?: throw AccountInvalidRequestException("Password must be between 6 and 100 characters.")
        }

        /**
         * 指定された値をアカウントのパスワードに変換する。
         */
        fun from(value: String): Password = Password(value)
    }
}
