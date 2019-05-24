package htnk128.kotlin.spring.boot.ddd.sample.presentation.controller

import htnk128.kotlin.spring.boot.ddd.sample.application.service.AccountResponse
import htnk128.kotlin.spring.boot.ddd.sample.application.service.AccountService
import htnk128.kotlin.spring.boot.ddd.sample.application.service.AccountsResponse
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
import reactor.core.publisher.Flux

@Api("アカウントを管理するAPI", tags = ["Accounts"])
@RestController
@RequestMapping("/accounts")
class AccountController(private val accountService: AccountService) {

    @ApiOperation("指定アカウントIDに該当するアカウントを取得する")
    @GetMapping("/{accountId}")
    fun find(@PathVariable accountId: String): Flux<AccountResponse> =
        Flux.just(accountService.find(accountId))

    @ApiOperation("すべてのアカウント情報を取得する")
    @GetMapping("")
    fun findAll(): Flux<AccountsResponse> =
        Flux.just(accountService.findAll())

    @ApiOperation("アカウントを作成する")
    @PostMapping("", consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: AccountRequest): Flux<AccountResponse> =
        Flux.just(accountService.create(request.name))

    @ApiOperation("アカウントを更新する")
    @PutMapping("/{accountId}", consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun update(
        @PathVariable accountId: String,
        @RequestBody request: AccountRequest
    ): Flux<AccountResponse> =
        Flux.just(accountService.update(accountId, request.name))
}

data class AccountRequest(
    @ApiModelProperty(value = "アカウント名", name = "name", example = "sample01", required = true, position = 1)
    val name: String
)
