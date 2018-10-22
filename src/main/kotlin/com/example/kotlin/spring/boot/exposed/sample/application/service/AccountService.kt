package com.example.kotlin.spring.boot.exposed.sample.application.service

import com.example.kotlin.spring.boot.exposed.sample.domain.model.account.Account
import com.example.kotlin.spring.boot.exposed.sample.domain.model.account.AccountIdentity
import com.example.kotlin.spring.boot.exposed.sample.domain.model.account.AccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(private val accountRepository: AccountRepository) {

    @Transactional(readOnly = true)
    fun find(accountId: AccountIdentity): Account = accountRepository.find(accountId)
        ?: throw RuntimeException("account not found.")

    @Transactional(readOnly = true)
    fun findAll(): List<Account> = accountRepository.findAll()

    @Transactional(timeout = 10, rollbackFor = [Exception::class])
    fun create(account: Account) {
        accountRepository.create(account)
    }

    @Transactional(timeout = 10, rollbackFor = [Exception::class])
    fun update(account: Account) {
        accountRepository.update(account)
            .takeIf { it > 0 }
            ?: throw RuntimeException("account not found.")
    }
}
