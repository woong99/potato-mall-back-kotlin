package com.potatowoong.potatomallbackkotlin.global.common.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.potatowoong.potatomallbackkotlin.global.exception.ErrorCode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

@JsonInclude(Include.NON_NULL)
data class ApiResponse<T>(
    val status: Status,
    val statusCode: Int,
    val message: String? = null,
    val data: T? = null,
    val errorCode: String? = null,
    val errorName: String? = null,
    val timestamp: LocalDateTime = LocalDateTime.now()
) {

    companion object {

        /**
         * 성공 응답
         *
         * @param data 데이터
         * @return ResponseEntity
         */
        fun <T> success(data: T): ResponseEntity<ApiResponse<T>> {
            return ResponseEntity.ok(
                ApiResponse(
                    status = Status.SUCCESS,
                    statusCode = 200,
                    data = data,
                )
            )
        }

        /**
         * 성공 응답
         *
         * @param responseText 응답 텍스트
         * @return ResponseEntity
         */
        fun success(responseText: ResponseText): ResponseEntity<ApiResponse<Unit>> {
            return ResponseEntity.ok(
                ApiResponse(
                    status = Status.SUCCESS,
                    statusCode = responseText.httpStatus.value(),
                    message = responseText.message,
                )
            )
        }

        /**
         * 성공 응답
         *
         * @param responseText 응답 텍스트
         * @param data 데이터
         * @return ResponseEntity
         */
        fun <T> success(responseText: ResponseText, data: T): ResponseEntity<ApiResponse<T>> {
            return ResponseEntity.ok(
                ApiResponse(
                    status = Status.SUCCESS,
                    statusCode = responseText.httpStatus.value(),
                    message = responseText.message,
                    data = data
                )
            )
        }

        /**
         * 실패 응답
         *
         * @param message 메시지
         * @return ResponseEntity
         */
        fun fail(message: String?): ResponseEntity<ApiResponse<Unit>> {
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                    ApiResponse(
                        status = Status.FAIL,
                        statusCode = 400,
                        message = message,
                    )
                )
        }

        /**
         * 에러 응답
         *
         * @param errorCode 에러 코드
         * @return ResponseEntity
         */
        fun error(errorCode: ErrorCode): ResponseEntity<ApiResponse<Unit>> {
            return ResponseEntity
                .status(errorCode.httpStatus)
                .body(
                    ApiResponse(
                        status = Status.ERROR,
                        statusCode = errorCode.httpStatus.value(),
                        message = errorCode.message,
                        errorCode = errorCode.code,
                        errorName = errorCode.name,
                    )
                )
        }
    }

    enum class Status {
        SUCCESS,
        FAIL,
        ERROR
    }
}
