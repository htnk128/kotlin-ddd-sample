package htnk128.kotlin.spring.boot.ddd.sample.customer.presentation.controller

import htnk128.kotlin.spring.boot.ddd.sample.customer.application.dto.CustomerDTO
import htnk128.kotlin.spring.boot.ddd.sample.customer.application.service.CustomerService
import htnk128.kotlin.spring.boot.ddd.sample.customer.presentation.resource.CustomerCreateRequest
import htnk128.kotlin.spring.boot.ddd.sample.customer.presentation.resource.CustomerResponse
import htnk128.kotlin.spring.boot.ddd.sample.customer.presentation.resource.CustomerResponses
import htnk128.kotlin.spring.boot.ddd.sample.customer.presentation.resource.CustomerUpdateRequest
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
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

@Api("顧客を管理するAPI", tags = ["Customers"])
@RestController
@RequestMapping("/customers")
class CustomerController(private val customerService: CustomerService) {

    @ApiOperation("顧客を取得する")
    @GetMapping("/{customerId}")
    fun find(
        @ApiParam(value = "顧客のID", required = true, example = "CUS_c5fb2cec-a77c-4886-b997-ffc2ef060e78")
        @PathVariable customerId: String
    ): CustomerResponse = customerService.find(customerId)
        .toResponse()

    @ApiOperation("すべての顧客を取得する")
    @GetMapping("")
    fun findAll(): CustomerResponses = CustomerResponses(customerService.findAll().map { it.toResponse() })

    @ApiOperation("顧客を作成する")
    @PostMapping("", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody request: CustomerCreateRequest
    ): CustomerResponse = customerService.create(request.name, request.namePronunciation, request.email)
        .toResponse()

    @ApiOperation("顧客を更新する")
    @PutMapping("/{customerId}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun update(
        @ApiParam(value = "顧客のID", required = true, example = "CUS_c5fb2cec-a77c-4886-b997-ffc2ef060e78")
        @PathVariable customerId: String,
        @RequestBody request: CustomerUpdateRequest
    ): CustomerResponse = customerService.update(customerId, request.name, request.namePronunciation, request.email)
        .toResponse()

    @ApiOperation("顧客を削除する")
    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @ApiParam(value = "顧客のID", required = true, example = "CUS_c5fb2cec-a77c-4886-b997-ffc2ef060e78")
        @PathVariable customerId: String
    ) {
        customerService.delete(customerId)
    }

    private fun CustomerDTO.toResponse() =
        CustomerResponse.from(this)
}
