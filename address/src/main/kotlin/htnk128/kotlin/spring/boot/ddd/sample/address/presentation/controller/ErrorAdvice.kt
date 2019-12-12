package htnk128.kotlin.spring.boot.ddd.sample.address.presentation.controller

import htnk128.kotlin.spring.boot.ddd.sample.shared.UnexpectedException
import htnk128.kotlin.spring.boot.ddd.sample.shared.application.exception.NotFoundException
import htnk128.kotlin.spring.boot.ddd.sample.shared.domain.exception.InvalidRequestException
import htnk128.kotlin.spring.boot.ddd.sample.shared.presentation.resource.ErrorResponse
import mu.KLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class ErrorAdvice {

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    fun handleNotFoundException(exception: NotFoundException): ErrorResponse {
        logger.warn(exception) { "type=${exception.type}, status=${exception.status}, message=${exception.message}" }
        return errorResponse(exception.type, exception.status, exception.message)
    }

    @ExceptionHandler(InvalidRequestException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    fun handleInvalidRequestException(exception: InvalidRequestException): ErrorResponse {
        logger.warn(exception) { "type=${exception.type}, status=${exception.status}, message=${exception.message}" }
        return errorResponse(exception.type, exception.status, exception.message)
    }

    @ExceptionHandler(UnexpectedException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleUnexpectedException(exception: UnexpectedException): ErrorResponse {
        logger.error(exception) { "type=${exception.type}, status=${exception.status}, message=${exception.message}" }
        return errorResponse(exception.type, exception.status, exception.message)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleException(exception: Exception): ErrorResponse {
        val type = "server_error"
        val status = HttpStatus.INTERNAL_SERVER_ERROR.value()
        val message = "internal server error."

        logger.error(exception) { "type=$type, status=$status, message=$message" }
        return errorResponse(type, status, message)
    }

    private fun errorResponse(type: String, code: Int, message: String): ErrorResponse =
        ErrorResponse(ErrorResponse.Error(type, code, message))

    companion object : KLogging()
}
