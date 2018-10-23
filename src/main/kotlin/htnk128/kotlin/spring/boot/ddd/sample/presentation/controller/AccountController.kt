package htnk128.kotlin.spring.boot.ddd.sample.presentation.controller

import htnk128.kotlin.spring.boot.ddd.sample.application.service.AccountService
import htnk128.kotlin.spring.boot.ddd.sample.domain.model.account.Account
import htnk128.kotlin.spring.boot.ddd.sample.domain.model.account.AccountIdentity
import htnk128.kotlin.spring.boot.ddd.sample.domain.model.account.Name
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/accounts")
class AccountController(private val accountService: AccountService) {

    @GetMapping("/{accountId}")
    fun find(@PathVariable accountId: AccountIdentity): Account = accountService.find(accountId)

    @GetMapping("")
    fun findAll(): List<Account> = accountService.findAll()

    @PostMapping("", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun create(@ModelAttribute req: AccountRequest): Account =
        req.toAccount().also { accountService.create(it) }

    @PutMapping("/{accountId}", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun update(
        @PathVariable accountId: AccountIdentity,
        @ModelAttribute req: AccountRequest
    ): Account =
        req.toAccount(accountId).also { accountService.update(it) }
}

data class AccountRequest(val name: String)

private fun AccountRequest.toAccount(
    accountId: AccountIdentity = AccountIdentity.generate()
): Account = Account(accountId, Name(name))

