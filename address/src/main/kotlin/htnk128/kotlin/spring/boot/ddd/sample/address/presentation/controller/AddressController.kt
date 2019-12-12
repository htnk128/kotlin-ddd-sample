package htnk128.kotlin.spring.boot.ddd.sample.address.presentation.controller

import htnk128.kotlin.spring.boot.ddd.sample.address.application.dto.AddressDTO
import htnk128.kotlin.spring.boot.ddd.sample.address.application.service.AddressService
import htnk128.kotlin.spring.boot.ddd.sample.address.presentation.resource.AddressCreateRequest
import htnk128.kotlin.spring.boot.ddd.sample.address.presentation.resource.AddressResponse
import htnk128.kotlin.spring.boot.ddd.sample.address.presentation.resource.AddressResponses
import htnk128.kotlin.spring.boot.ddd.sample.address.presentation.resource.AddressUpdateRequest
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

@Api("住所を管理するAPI", tags = ["Addresss"])
@RestController
@RequestMapping("/addresses")
class AddressController(private val addressService: AddressService) {

    @ApiOperation("住所を取得する")
    @GetMapping("/{addressId}")
    fun find(
        @ApiParam(value = "住所のID", required = true, example = "CUS_c5fb2cec-a77c-4886-b997-ffc2ef060e78")
        @PathVariable addressId: String
    ): AddressResponse = addressService.find(addressId)
        .toResponse()

    @ApiOperation("すべての住所を取得する")
    @GetMapping("")
    fun findAll(): AddressResponses = AddressResponses(addressService.findAll().map { it.toResponse() })

    @ApiOperation("住所を作成する")
    @PostMapping("", consumes = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody request: AddressCreateRequest
    ): AddressResponse = addressService.create(request.name, request.namePronunciation, request.email)
        .toResponse()

    @ApiOperation("住所を更新する")
    @PutMapping("/{addressId}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun update(
        @ApiParam(value = "住所のID", required = true, example = "CUS_c5fb2cec-a77c-4886-b997-ffc2ef060e78")
        @PathVariable addressId: String,
        @RequestBody request: AddressUpdateRequest
    ): AddressResponse = addressService.update(addressId, request.name, request.namePronunciation, request.email)
        .toResponse()

    @ApiOperation("住所を削除する")
    @DeleteMapping("/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @ApiParam(value = "住所のID", required = true, example = "CUS_c5fb2cec-a77c-4886-b997-ffc2ef060e78")
        @PathVariable addressId: String
    ) = addressService.delete(addressId)

    private fun AddressDTO.toResponse() =
        AddressResponse.from(this)
}
