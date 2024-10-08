package com.potatowoong.potatomallbackkotlin.global.exception

import com.potatowoong.potatomallbackkotlin.global.common.dto.ApiResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class PotatoMallAdvice {

    private val log = KotlinLogging.logger {}

    /**
     * Handle custom exception
     */
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ResponseEntity<ApiResponse<Unit>> {
        return ApiResponse.error(e.errorCode)
    }

    /**
     * Handle method argument not valid exception
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ApiResponse<Unit>> {
        return ApiResponse.fail(e.bindingResult.allErrors[0].defaultMessage)
    }

    /**
     * Handle exception
     */
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ApiResponse<Unit>> {
        log.error { "[Exception] :: $e" }
        return ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR)
    }
}