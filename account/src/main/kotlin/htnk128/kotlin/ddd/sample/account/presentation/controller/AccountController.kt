package htnk128.kotlin.ddd.sample.account.presentation.controller

import htnk128.kotlin.ddd.sample.account.application.command.CreateAccountCommand
import htnk128.kotlin.ddd.sample.account.application.command.DeleteAccountCommand
import htnk128.kotlin.ddd.sample.account.application.command.FindAccountCommand
import htnk128.kotlin.ddd.sample.account.application.command.FindAllAccountCommand
import htnk128.kotlin.ddd.sample.account.application.command.UpdateAccountCommand
import htnk128.kotlin.ddd.sample.account.application.dto.AccountDTO
import htnk128.kotlin.ddd.sample.account.application.service.AccountService
import htnk128.kotlin.ddd.sample.account.presentation.resource.AccountCreateRequest
import htnk128.kotlin.ddd.sample.account.presentation.resource.AccountFindAllRequest
import htnk128.kotlin.ddd.sample.account.presentation.resource.AccountResponse
import htnk128.kotlin.ddd.sample.account.presentation.resource.AccountResponses
import htnk128.kotlin.ddd.sample.account.presentation.resource.AccountUpdateRequest
import htnk128.kotlin.ddd.sample.shared.presentation.resource.ErrorResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@Api("アカウントを管理するAPI", tags = ["Accounts"])
@RestController
@RequestMapping("/accounts")
class AccountController(private val accountService: AccountService) {

    @ApiResponses(
        value = [
            (ApiResponse(code = 200, message = "OK", response = AccountResponse::class)),
            (ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse::class)),
            (ApiResponse(code = 404, message = "Not Found", response = ErrorResponse::class)),
            (ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse::class))
        ]
    )
    @ApiOperation("アカウントを取得する")
    @GetMapping("/{accountId}")
    fun find(
        @ApiParam(value = "アカウントのID", required = true, example = "AC_c5fb2cec-a77c-4886-b997-ffc2ef060e78")
        @PathVariable accountId: String
    ): Mono<AccountResponse> =
        accountService.find(
            FindAccountCommand(accountId)
        )
            .map { it.toResponse() }

    @ApiResponses(
        value = [
            (ApiResponse(code = 200, message = "OK", response = AccountResponses::class)),
            (ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse::class)),
            (ApiResponse(code = 404, message = "Not Found", response = ErrorResponse::class)),
            (ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse::class))
        ]
    )
    @ApiOperation("すべてのアカウントを取得する")
    @GetMapping("")
    fun findAll(
        @ModelAttribute request: AccountFindAllRequest
    ): Mono<AccountResponses> =
        accountService.findAll(
            FindAllAccountCommand(
                request.limit,
                request.offset
            )
        )
            .map { dto ->
                AccountResponses(
                    dto.count,
                    dto.hasMore,
                    dto.data.map { it.toResponse() }
                )
            }

    @ApiResponses(
        value = [
            (ApiResponse(code = 201, message = "Created", response = AccountResponse::class)),
            (ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse::class)),
            (ApiResponse(code = 404, message = "Not Found", response = ErrorResponse::class)),
            (ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse::class))
        ]
    )
    @ApiOperation("アカウントを作成する")
    @PostMapping("", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody request: AccountCreateRequest
    ): Mono<AccountResponse> =
        accountService.create(
            CreateAccountCommand(
                request.name,
                request.namePronunciation,
                request.email,
                request.password
            )
        )
            .map { it.toResponse() }

    @ApiResponses(
        value = [
            (ApiResponse(code = 200, message = "OK", response = AccountResponse::class)),
            (ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse::class)),
            (ApiResponse(code = 404, message = "Not Found", response = ErrorResponse::class)),
            (ApiResponse(code = 409, message = "Conflict", response = ErrorResponse::class)),
            (ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse::class))
        ]
    )
    @ApiOperation("アカウントを更新する")
    @PutMapping("/{accountId}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun update(
        @ApiParam(value = "アカウントのID", required = true, example = "AC_c5fb2cec-a77c-4886-b997-ffc2ef060e78")
        @PathVariable accountId: String,
        @RequestBody request: AccountUpdateRequest
    ): Mono<AccountResponse> =
        accountService.update(
            UpdateAccountCommand(
                accountId,
                request.name,
                request.namePronunciation,
                request.email,
                request.password
            )
        )
            .map { it.toResponse() }

    @ApiResponses(
        value = [
            (ApiResponse(code = 204, message = "No Content", response = Unit::class)),
            (ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse::class)),
            (ApiResponse(code = 404, message = "Not Found", response = ErrorResponse::class)),
            (ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse::class))
        ]
    )
    @ApiOperation("アカウントを削除する")
    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @ApiParam(value = "アカウントのID", required = true, example = "AC_c5fb2cec-a77c-4886-b997-ffc2ef060e78")
        @PathVariable accountId: String
    ): Mono<AccountResponse> =
        accountService.delete(
            DeleteAccountCommand(accountId)
        )
            .map { it.toResponse() }

    private fun AccountDTO.toResponse() =
        AccountResponse.from(this)
}
