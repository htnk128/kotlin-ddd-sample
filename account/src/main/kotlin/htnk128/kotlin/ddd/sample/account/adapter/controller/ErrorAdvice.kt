package htnk128.kotlin.ddd.sample.account.adapter.controller

import htnk128.kotlin.ddd.sample.shared.adapter.controller.resource.ErrorResponse
import htnk128.kotlin.ddd.sample.shared.usecase.ApplicationException
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ErrorAdvice {

    @ExceptionHandler(ApplicationException::class)
    fun handleApplicationException(exception: ApplicationException): ResponseEntity<ErrorResponse> {
        logger.error(exception) { "type=${exception.type}, status=${exception.status}, message=${exception.message}" }

        return ResponseEntity
            .status(exception.status)
            .body(errorResponse(exception.type, exception.status, exception.message))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception): ResponseEntity<ErrorResponse> {
        val type = "server_error"
        val status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        val message = "internal server error."

        logger.error(exception) { "type=$type, status=$status, message=$message" }

        return ResponseEntity
            .status(status)
            .body(errorResponse(type, status, message))
    }

    private fun errorResponse(type: String, code: Int, message: String): ErrorResponse =
        ErrorResponse.from(type, code, message)

    companion object : KLogging()
}
