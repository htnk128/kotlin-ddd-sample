package htnk128.kotlin.ddd.sample.account.usecase.outputport.dto

import htnk128.kotlin.ddd.sample.account.domain.model.account.Account

/**
 * アカウント([Account])のDTO。
 */
data class AccountDTO(
    val accountId: String,
    val name: String,
    val namePronunciation: String,
    val email: String,
    val password: String,
    val createdAt: Long,
    val deletedAt: Long?,
    val updatedAt: Long
)
