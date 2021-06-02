package htnk128.kotlin.ddd.sample.address.adapter.controller

import htnk128.kotlin.ddd.sample.address.adapter.controller.resource.AddressCreateRequest
import htnk128.kotlin.ddd.sample.address.adapter.controller.resource.AddressFindAllRequest
import htnk128.kotlin.ddd.sample.address.adapter.controller.resource.AddressResponse
import htnk128.kotlin.ddd.sample.address.adapter.controller.resource.AddressResponses
import htnk128.kotlin.ddd.sample.address.adapter.controller.resource.AddressUpdateRequest
import htnk128.kotlin.ddd.sample.address.usecase.inputport.AddressUseCase
import htnk128.kotlin.ddd.sample.address.usecase.inputport.command.CreateAddressCommand
import htnk128.kotlin.ddd.sample.address.usecase.inputport.command.DeleteAddressCommand
import htnk128.kotlin.ddd.sample.address.usecase.inputport.command.FindAddressCommand
import htnk128.kotlin.ddd.sample.address.usecase.inputport.command.FindAllAddressCommand
import htnk128.kotlin.ddd.sample.address.usecase.inputport.command.UpdateAddressCommand
import htnk128.kotlin.ddd.sample.address.usecase.outputport.AddressPresenter
import htnk128.kotlin.ddd.sample.address.usecase.outputport.dto.AddressDTO
import htnk128.kotlin.ddd.sample.shared.adapter.controller.resource.ErrorResponse
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
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@Api("住所を管理するAPI", tags = ["Addresses"])
@RestController
@RequestMapping("/addresses")
class AddressController(
    private val addressUseCase: AddressUseCase,
    private val addressPresenter: AddressPresenter
) {

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
    ): Mono<AddressResponse> {
        val command = FindAddressCommand(addressId)

        return addressUseCase.find(command)
            .map { addressPresenter.toDTO(it) }
            .map { it.toResponse() }
    }

    @ApiResponses(
        value = [
            (ApiResponse(code = 200, message = "OK", response = AddressResponses::class)),
            (ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse::class)),
            (ApiResponse(code = 404, message = "Not Found", response = ErrorResponse::class)),
            (ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse::class))
        ]
    )
    @ApiOperation("アカウントのすべての住所を取得する")
    @GetMapping("")
    fun findAll(
        @ModelAttribute request: AddressFindAllRequest
    ): Mono<AddressResponses> {
        val command = FindAllAddressCommand(request.ownerId)

        return addressUseCase.findAll(command)
            .map { addressPresenter.toDTO(it) }
            .map { it.toResponse() }
            .collect(Collectors.toList())
            .map { AddressResponses(it) }
    }

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
    ): Mono<AddressResponse> {
        val command = CreateAddressCommand(
            request.ownerId,
            request.fullName,
            request.zipCode,
            request.stateOrRegion,
            request.line1,
            request.line2,
            request.phoneNumber
        )

        return addressUseCase.create(command)
            .map { addressPresenter.toDTO(it) }
            .map { it.toResponse() }
    }

    @ApiResponses(
        value = [
            (ApiResponse(code = 200, message = "OK", response = AddressResponse::class)),
            (ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse::class)),
            (ApiResponse(code = 404, message = "Not Found", response = ErrorResponse::class)),
            (ApiResponse(code = 409, message = "Conflict", response = ErrorResponse::class)),
            (ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse::class))
        ]
    )
    @ApiOperation("住所を更新する")
    @PutMapping("/{addressId}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun update(
        @ApiParam(value = "住所のID", required = true, example = "ADDR_c5fb2cec-a77c-4886-b997-ffc2ef060e78")
        @PathVariable addressId: String,
        @RequestBody request: AddressUpdateRequest
    ): Mono<AddressResponse> {
        val command = UpdateAddressCommand(
            addressId,
            request.fullName,
            request.zipCode,
            request.stateOrRegion,
            request.line1,
            request.line2,
            request.phoneNumber
        )

        return addressUseCase.update(command)
            .map { addressPresenter.toDTO(it) }
            .map { it.toResponse() }
    }

    @ApiResponses(
        value = [
            (ApiResponse(code = 200, message = "OK", response = AddressResponse::class)),
            (ApiResponse(code = 400, message = "Bad Request", response = ErrorResponse::class)),
            (ApiResponse(code = 404, message = "Not Found", response = ErrorResponse::class)),
            (ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponse::class))
        ]
    )
    @ApiOperation("住所を削除する")
    @DeleteMapping("/{addressId}")
    fun delete(
        @ApiParam(value = "住所のID", required = true, example = "ADDR_c5fb2cec-a77c-4886-b997-ffc2ef060e78")
        @PathVariable addressId: String
    ): Mono<AddressResponse> {
        val command = DeleteAddressCommand(addressId)

        return addressUseCase.delete(command)
            .map { addressPresenter.toDTO(it) }
            .map { it.toResponse() }
    }

    private fun AddressDTO.toResponse() =
        AddressResponse.from(this)
}
