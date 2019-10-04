package htnk128.kotlin.spring.boot.ddd.sample.presentation.controller

import htnk128.kotlin.spring.boot.ddd.sample.application.service.CustomerService
import htnk128.kotlin.spring.boot.ddd.sample.application.service.dto.CustomerDTO
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

@Api("顧客を管理するAPI", tags = ["Customers"])
@RestController
@RequestMapping("/customers")
class CustomerController(private val customerService: CustomerService) {

    @ApiOperation("指定顧客IDに該当する顧客を取得する")
    @GetMapping("/{customerId}")
    fun find(@PathVariable customerId: String): Flux<CustomerResponse> =
        Flux.just(customerService.find(customerId).toResponse())

    @ApiOperation("すべての顧客情報を取得する")
    @GetMapping("")
    fun findAll(): Flux<CustomerResponses> =
        Flux.just(CustomerResponses(customerService.findAll().map { it.toResponse() }))

    @ApiOperation("顧客を作成する")
    @PostMapping("", consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: CustomerRequest): Flux<CustomerResponse> =
        Flux.just(customerService.create(request.name).toResponse())

    @ApiOperation("顧客を更新する")
    @PutMapping("/{customerId}", consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun update(
        @PathVariable customerId: String,
        @RequestBody request: CustomerRequest
    ): Flux<CustomerResponse> =
        Flux.just(customerService.update(customerId, request.name).toResponse())
}

data class CustomerRequest(
    @ApiModelProperty(value = "顧客名", name = "name", example = "sample01", required = true, position = 1)
    val name: String
)

data class CustomerResponse(
    @ApiModelProperty(value = "顧客ID", name = "customerId", example = "customer01", required = true, position = 1)
    val customerId: String,
    @ApiModelProperty(value = "顧客名", name = "name", example = "sample01", required = true, position = 2)
    val name: String
)

data class CustomerResponses(
    val data: List<CustomerResponse>
)

private fun CustomerDTO.toResponse(): CustomerResponse = CustomerResponse(
    customerId,
    name
)
