package htnk128.kotlin.ddd.sample.address.presentation.controller

import htnk128.kotlin.ddd.sample.address.application.dto.AddressDTO
import htnk128.kotlin.ddd.sample.address.application.service.AddressService
import htnk128.kotlin.ddd.sample.address.presentation.resource.AddressCreateRequest
import htnk128.kotlin.ddd.sample.address.presentation.resource.AddressResponse
import htnk128.kotlin.ddd.sample.address.presentation.resource.AddressResponses
import htnk128.kotlin.ddd.sample.address.presentation.resource.AddressUpdateRequest
import htnk128.kotlin.ddd.sample.shared.presentation.resource.ErrorResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import java.util.stream.Collectors
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@Api("住所を管理するAPI", tags = ["Addresss"])
@RestController
@RequestMapping("/addresses")
class AddressController(private val addressService: AddressService) {

    @ApiResponses(
        value = [
            (ApiResponse(code = 200, message = "OK", response = AddressResponse::class)),
            (ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse::class)),
            (ApiResponse(code = 404, message = "Not Found", response = ErrorResponse::class)),
            (ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse::class))
        ]
    )
    @ApiOperation("住所を取得する")
    @GetMapping("/{addressId}")
    fun find(
        @ApiParam(value = "住所のID", required = true, example = "ADDR_c5fb2cec-a77c-4886-b997-ffc2ef060e78")
        @PathVariable addressId: String
    ): Mono<AddressResponse> = addressService.find(addressId)
        .map { it.toResponse() }

    @ApiResponses(
        value = [
            (ApiResponse(code = 200, message = "OK", response = AddressResponses::class)),
            (ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse::class)),
            (ApiResponse(code = 404, message = "Not Found", response = ErrorResponse::class)),
            (ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse::class))
        ]
    )
    @ApiOperation("顧客のIDに紐付いているすべての住所を取得する")
    @GetMapping("")
    fun findAll(
        @ApiParam(value = "顧客のID", required = true, example = "CUST_c5fb2cec-a77c-4886-b997-ffc2ef060e78")
        @RequestParam("customerId", required = true) customerId: String
    ): Mono<AddressResponses> =
        addressService.findAll(customerId)
            .map { it.toResponse() }
            .collect(Collectors.toList())
            .map { AddressResponses(it) }

    @ApiResponses(
        value = [
            (ApiResponse(code = 201, message = "Created", response = AddressResponse::class)),
            (ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse::class)),
            (ApiResponse(code = 404, message = "Not Found", response = ErrorResponse::class)),
            (ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse::class))
        ]
    )
    @ApiOperation("住所を作成する")
    @PostMapping("", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody request: AddressCreateRequest
    ): Mono<AddressResponse> = addressService.create(
        request.customerId,
        request.fullName,
        request.zipCode,
        request.stateOrRegion,
        request.line1,
        request.line2,
        request.phoneNumber
    )
        .map { it.toResponse() }

    @ApiResponses(
        value = [
            (ApiResponse(code = 200, message = "OK", response = AddressResponse::class)),
            (ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse::class)),
            (ApiResponse(code = 404, message = "Not Found", response = ErrorResponse::class)),
            (ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse::class))
        ]
    )
    @ApiOperation("住所を更新する")
    @PutMapping("/{addressId}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun update(
        @ApiParam(value = "住所のID", required = true, example = "ADDR_c5fb2cec-a77c-4886-b997-ffc2ef060e78")
        @PathVariable addressId: String,
        @RequestBody request: AddressUpdateRequest
    ): Mono<AddressResponse> = addressService.update(
        addressId,
        request.fullName,
        request.zipCode,
        request.stateOrRegion,
        request.line1,
        request.line2,
        request.phoneNumber
    )
        .map { it.toResponse() }

    @ApiResponses(
        value = [
            (ApiResponse(code = 204, message = "No Content", response = Unit::class)), // TODO 暫定対応でUnitとする
            (ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse::class)),
            (ApiResponse(code = 404, message = "Not Found", response = ErrorResponse::class)),
            (ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse::class))
        ]
    )
    @ApiOperation("住所を削除する")
    @DeleteMapping("/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @ApiParam(value = "住所のID", required = true, example = "ADDR_c5fb2cec-a77c-4886-b997-ffc2ef060e78")
        @PathVariable addressId: String
    ): Mono<AddressResponse> =
        addressService.delete(addressId)
            .map { it.toResponse() }

    private fun AddressDTO.toResponse() =
        AddressResponse.from(this)
}
