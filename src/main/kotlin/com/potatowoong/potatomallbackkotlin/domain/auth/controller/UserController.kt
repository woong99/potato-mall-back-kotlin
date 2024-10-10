package com.potatowoong.potatomallbackkotlin.domain.auth.controller

import com.potatowoong.potatomallbackkotlin.domain.auth.dto.request.UserLoginReqDto
import com.potatowoong.potatomallbackkotlin.domain.auth.dto.request.UserSignUpReqDto
import com.potatowoong.potatomallbackkotlin.domain.auth.service.UserLoginService
import com.potatowoong.potatomallbackkotlin.domain.auth.service.UserSignUpService
import com.potatowoong.potatomallbackkotlin.global.auth.jwt.dto.AccessTokenDto
import com.potatowoong.potatomallbackkotlin.global.common.dto.ApiResponse
import com.potatowoong.potatomallbackkotlin.global.common.dto.ResponseText
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val userSignUpService: UserSignUpService,
    private val userLoginService: UserLoginService
) {

    /**
     * 회원가입 API
     */
    @PostMapping("/sign-up")
    fun signUp(
        @Valid @RequestBody dto: UserSignUpReqDto
    ): ResponseEntity<ApiResponse<Unit>> {
        // 회원가입
        userSignUpService.signUp(dto)

        return ApiResponse.success(ResponseText.SUCCESS_SIGN_UP)
    }

    /**
     * 아이디 중복 체크 API
     */
    @GetMapping("/check-duplicate-id")
    fun checkDuplicateId(
        @RequestParam userId: String
    ): ResponseEntity<ApiResponse<Unit>> {
        // 아이디 중복 체크
        val isDuplicate = userSignUpService.checkDuplicateId(userId)

        return ApiResponse.success(if (isDuplicate) ResponseText.DUPLICATE else ResponseText.OK)
    }

    /**
     * 닉네임 중복 체크 API
     */
    @GetMapping("/check-duplicate-nickname")
    fun checkDuplicateNickname(
        @RequestParam nickname: String
    ): ResponseEntity<ApiResponse<Unit>> {
        // 닉네임 중복 체크
        val isDuplicate = userSignUpService.checkDuplicateNickname(nickname)

        return ApiResponse.success(if (isDuplicate) ResponseText.DUPLICATE else ResponseText.OK)
    }

    /**
     * 로그인 API
     */
    @PostMapping("/login")
    fun login(
        @Valid @RequestBody dto: UserLoginReqDto,
        response: HttpServletResponse
    ): ResponseEntity<ApiResponse<AccessTokenDto>> {
        // 로그인
        val accessTokenDto = userLoginService.login(dto, response)

        return ApiResponse.success(accessTokenDto)
    }
}