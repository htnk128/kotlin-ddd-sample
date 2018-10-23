package htnk128.kotlin.spring.boot.ddd.sample.presentation.controller

import htnk128.kotlin.spring.boot.ddd.sample.application.service.AccountService
import htnk128.kotlin.spring.boot.ddd.sample.domain.model.account.Account
import htnk128.kotlin.spring.boot.ddd.sample.domain.model.account.AccountIdentity
import htnk128.kotlin.spring.boot.ddd.sample.domain.model.account.Name
import io.swagger.annotations.Api
import io.swagger.annotations.ApiModelProperty
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Api("アカウントを管理するAPI")
@RestController
@RequestMapping("/accounts")
class AccountController(private val accountService: AccountService) {

    @ApiOperation("指定アカウントIDに該当するアカウントを取得する")
    @GetMapping("/{accountId}")
    fun find(@PathVariable accountId: AccountIdentity): AccountResponse =
        accountService.find(accountId).toAccountResponse()

    @ApiOperation("すべてのアカウント情報を取得する")
    @GetMapping("")
    fun findAll(): List<AccountResponse> = accountService.findAll().map { it.toAccountResponse() }

    @ApiOperation("アカウントを作成する")
    @PostMapping("", consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: AccountRequest): AccountResponse =
        request.toAccount().also { accountService.create(it) }.toAccountResponse()

    @ApiOperation("アカウントを更新する")
    @PutMapping("/{accountId}", consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun update(
        @PathVariable accountId: AccountIdentity,
        @RequestBody request: AccountRequest
    ): AccountResponse =
        request.toAccount(accountId).also { accountService.update(it) }.toAccountResponse()
}

data class AccountRequest(
    @ApiModelProperty(
        value = "アカウント名", name = "name", example = "sample01", required = true, position = 1,
        dataType = "java.lang.String"
    )
    val name: Name
)

private fun AccountRequest.toAccount(accountId: AccountIdentity = AccountIdentity.generate()): Account =
    Account(accountId, name)

data class AccountResponse(
    @ApiModelProperty(
        value = "アカウントID", name = "accountId", example = "accountId01", position = 1, dataType = "java.lang.String"
    )
    val accountId: AccountIdentity,
    @ApiModelProperty(
        value = "アカウント名", name = "name", example = "sample01", position = 2, dataType = "java.lang.String"
    )
    val name: Name
)

private fun Account.toAccountResponse() = AccountResponse(accountId, name)
