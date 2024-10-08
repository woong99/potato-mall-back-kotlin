package com.potatowoong.potatomallbackkotlin.domain.auth.controller

import com.potatowoong.potatomallbackkotlin.domain.auth.dto.response.UserSignUpReqDto
import com.potatowoong.potatomallbackkotlin.domain.auth.service.UserSignUpService
import com.potatowoong.potatomallbackkotlin.global.common.dto.ApiResponse
import com.potatowoong.potatomallbackkotlin.global.common.dto.ResponseText
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    val userSignUpService: UserSignUpService
) {

    /**
     * 회원가입 API
     */
    @PostMapping("/sign-up")
    fun signUp(@Valid @RequestBody dto: UserSignUpReqDto): ResponseEntity<ApiResponse<Unit>> {
        userSignUpService.signUp(dto)

        return ApiResponse.success(ResponseText.SUCCESS_SIGN_UP)
    }

    /**
     * 아이디 중복 체크 API
     */
    @GetMapping("/check-duplicate-id")
    fun checkDuplicateId(@RequestParam userId: String): ResponseEntity<ApiResponse<Unit>> {
        // 아이디 중복 체크
        val isDuplicate = userSignUpService.checkDuplicateId(userId)

        return ApiResponse.success(if (isDuplicate) ResponseText.DUPLICATE else ResponseText.OK)
    }

    /**
     * 닉네임 중복 체크 API
     */
    @GetMapping("/check-duplicate-nickname")
    fun checkDuplicateNickname(@RequestParam nickname: String): ResponseEntity<ApiResponse<Unit>> {
        // 닉네임 중복 체크
        val isDuplicate = userSignUpService.checkDuplicateNickname(nickname)

        return ApiResponse.success(if (isDuplicate) ResponseText.DUPLICATE else ResponseText.OK)
    }
}