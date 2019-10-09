package htnk128.kotlin.spring.boot.ddd.sample.contacts.presentation.controller

import htnk128.kotlin.spring.boot.ddd.sample.contacts.application.service.ContactsService
import htnk128.kotlin.spring.boot.ddd.sample.contacts.application.service.dto.ContactsDTO
import io.swagger.annotations.Api
import io.swagger.annotations.ApiModelProperty
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
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

@Api("連作先を管理するAPI", tags = ["Contacts"])
@RestController
@RequestMapping("/contacts")
class ContactsController(private val contactsService: ContactsService) {

    @ApiOperation("指定連絡先IDに該当する連絡先を取得する")
    @GetMapping("/{contactDetailsId}")
    fun find(
        @ApiParam(value = "連絡先ID", required = true, example = "contactDetails01")
        @PathVariable contactDetailsId: String
    ): Flux<ContactsResponse> =
        Flux.just(contactsService.find(contactDetailsId).toResponse())

    @ApiOperation("すべての連絡先情報を取得する")
    @GetMapping("")
    fun findAll(): Flux<ContactsResponses> =
        Flux.just(
            ContactsResponses(contactsService.findAll().map { it.toResponse() })
        )

    @ApiOperation("連絡先を作成する")
    @PostMapping("", consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: ContactsCreateRequest): Flux<ContactsResponse> =
        Flux.just(contactsService.create(request.customerId, request.telephoneNumber).toResponse())

    @ApiOperation("連絡先を更新する")
    @PutMapping("/{contactDetailsId}", consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun update(
        @ApiParam(value = "連絡先ID", required = true, example = "contactDetails01")
        @PathVariable contactDetailsId: String,
        @RequestBody request: ContactsUpdateRequest
    ): Flux<ContactsResponse> =
        Flux.just(contactsService.update(contactDetailsId, request.telephoneNumber).toResponse())
}

data class ContactsCreateRequest(
    @ApiModelProperty(value = "顧客ID", name = "customerId", example = "customer01", required = true, position = 1)
    val customerId: String,
    @ApiModelProperty(
        value = "電話番号", name = "telephoneNumber", example = "00000000000", required = true, position = 2
    )
    val telephoneNumber: String
)

data class ContactsUpdateRequest(
    @ApiModelProperty(
        value = "電話番号", name = "telephoneNumber", example = "00000000000", required = true, position = 2
    )
    val telephoneNumber: String
)

data class ContactsResponse(
    @ApiModelProperty(
        value = "連作先ID", name = "contactDetailsId", example = "contactDetails01", required = true, position = 1
    )
    val contactDetailsId: String,
    @ApiModelProperty(value = "顧客ID", name = "customerId", example = "customer01", required = true, position = 2)
    val customerId: String,
    @ApiModelProperty(
        value = "電話番号", name = "telephoneNumber", example = "00000000000", required = true, position = 3
    )
    val telephoneNumber: String
)

data class ContactsResponses(
    val data: List<ContactsResponse>
)

private fun ContactsDTO.toResponse(): ContactsResponse =
    ContactsResponse(
        contactDetailsId,
        customerId,
        telephoneNumber
    )
