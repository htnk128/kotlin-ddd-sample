package htnk128.kotlin.ddd.sample.customer.presentation.controller

import htnk128.kotlin.ddd.sample.customer.application.dto.CustomerDTO
import htnk128.kotlin.ddd.sample.customer.application.service.CustomerService
import htnk128.kotlin.ddd.sample.customer.presentation.resource.CustomerCreateRequest
import htnk128.kotlin.ddd.sample.customer.presentation.resource.CustomerResponse
import htnk128.kotlin.ddd.sample.customer.presentation.resource.CustomerResponses
import htnk128.kotlin.ddd.sample.customer.presentation.resource.CustomerUpdateRequest
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
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@Api("顧客を管理するAPI", tags = ["Customers"])
@RestController
@RequestMapping("/customers")
class CustomerController(private val customerService: CustomerService) {

    @ApiResponses(
        value = [
            (ApiResponse(code = 200, message = "OK", response = CustomerResponse::class)),
            (ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse::class)),
            (ApiResponse(code = 404, message = "Not Found", response = ErrorResponse::class)),
            (ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse::class))
        ]
    )
    @ApiOperation("顧客を取得する")
    @GetMapping("/{customerId}")
    fun find(
        @ApiParam(value = "顧客のID", required = true, example = "CUS_c5fb2cec-a77c-4886-b997-ffc2ef060e78")
        @PathVariable customerId: String
    ): Mono<CustomerResponse> = customerService.find(customerId)
        .toResponse()
        .let { Mono.just(it) }

    @ApiResponses(
        value = [
            (ApiResponse(code = 200, message = "OK", response = CustomerResponses::class)),
            (ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse::class)),
            (ApiResponse(code = 404, message = "Not Found", response = ErrorResponse::class)),
            (ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse::class))
        ]
    )
    @ApiOperation("すべての顧客を取得する")
    @GetMapping("")
    fun findAll(): Mono<CustomerResponses> =
        CustomerResponses(customerService.findAll().map { it.toResponse() })
            .let { Mono.just(it) }

    @ApiResponses(
        value = [
            (ApiResponse(code = 201, message = "Created", response = CustomerResponse::class)),
            (ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse::class)),
            (ApiResponse(code = 404, message = "Not Found", response = ErrorResponse::class)),
            (ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse::class))
        ]
    )
    @ApiOperation("顧客を作成する")
    @PostMapping("", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody request: CustomerCreateRequest
    ): Mono<CustomerResponse> = customerService.create(request.name, request.namePronunciation, request.email)
        .toResponse()
        .let { Mono.just(it) }

    @ApiResponses(
        value = [
            (ApiResponse(code = 200, message = "OK", response = CustomerResponse::class)),
            (ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse::class)),
            (ApiResponse(code = 404, message = "Not Found", response = ErrorResponse::class)),
            (ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse::class))
        ]
    )
    @ApiOperation("顧客を更新する")
    @PutMapping("/{customerId}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun update(
        @ApiParam(value = "顧客のID", required = true, example = "CUS_c5fb2cec-a77c-4886-b997-ffc2ef060e78")
        @PathVariable customerId: String,
        @RequestBody request: CustomerUpdateRequest
    ): Mono<CustomerResponse> =
        customerService.update(customerId, request.name, request.namePronunciation, request.email)
            .toResponse()
            .let { Mono.just(it) }

    @ApiResponses(
        value = [
            (ApiResponse(code = 204, message = "No Content", response = Unit::class)),
            (ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse::class)),
            (ApiResponse(code = 404, message = "Not Found", response = ErrorResponse::class)),
            (ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse::class))
        ]
    )
    @ApiOperation("顧客を削除する")
    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @ApiParam(value = "顧客のID", required = true, example = "CUS_c5fb2cec-a77c-4886-b997-ffc2ef060e78")
        @PathVariable customerId: String
    ): Mono<Void> {
        customerService.delete(customerId)
        return Mono.empty<Void>()
    }

    private fun CustomerDTO.toResponse() =
        CustomerResponse.from(this)
}
