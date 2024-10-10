package com.potatowoong.potatomallbackkotlin.domain.auth.service

import com.potatowoong.potatomallbackkotlin.domain.auth.dto.request.UserLoginReqDto
import com.potatowoong.potatomallbackkotlin.domain.auth.enums.RefreshTokenName
import com.potatowoong.potatomallbackkotlin.domain.auth.enums.Role
import com.potatowoong.potatomallbackkotlin.domain.auth.repository.MemberRepository
import com.potatowoong.potatomallbackkotlin.global.auth.jwt.component.JwtTokenProvider
import com.potatowoong.potatomallbackkotlin.global.auth.jwt.dto.AccessTokenDto
import com.potatowoong.potatomallbackkotlin.global.common.utils.createCookie
import com.potatowoong.potatomallbackkotlin.global.common.utils.getCookieValue
import com.potatowoong.potatomallbackkotlin.global.common.utils.removeCookie
import com.potatowoong.potatomallbackkotlin.global.exception.CustomException
import com.potatowoong.potatomallbackkotlin.global.exception.ErrorCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit

@Service
class UserLoginService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisTemplate: RedisTemplate<String, String>
) {

    @Transactional(readOnly = true)
    fun login(
        userLoginReqDto: UserLoginReqDto,
        response: HttpServletResponse
    ): AccessTokenDto {
        // ID로 회원 조회
        val savedMember = memberRepository.findById(userLoginReqDto.id)
            .orElseThrow { CustomException(ErrorCode.FAILED_TO_LOGIN) }

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(userLoginReqDto.password, savedMember.password)) {
            throw CustomException(ErrorCode.FAILED_TO_LOGIN)
        }

        // 소셜 로그인 여부 확인
        if (savedMember.socialType != null) {
            throw CustomException(ErrorCode.WRONG_LOGIN_TYPE)
        }

        // Authentication 객체 생성
        val authentication = UsernamePasswordAuthenticationToken(
            savedMember.userId,
            savedMember.password,
            listOf(GrantedAuthority { Role.ROLE_USER.name })
        )

        // 인증 정보를 기반으로 JWT Token 생성
        val tokenDto = jwtTokenProvider.generateToken(authentication)
        val refreshTokenDto = tokenDto.refreshToken

        // Refresh Token을 Redis에 저장
        val valueOperations = redisTemplate.opsForValue()
        valueOperations.set(
            refreshTokenDto.token,
            savedMember.userId,
            refreshTokenDto.getExpiresInSecond(),
            TimeUnit.SECONDS
        )

        // Refresh Token을 쿠키에 담아서 전달
        createCookie(
            RefreshTokenName.USER_REFRESH_TOKEN.name,
            refreshTokenDto.token,
            refreshTokenDto.getExpiresInSecond(),
            response
        )

        return tokenDto.accessToken
    }

    fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        // Refresh Token 쿠키 조회
        val refreshToken = getCookieValue(request.cookies, RefreshTokenName.USER_REFRESH_TOKEN.name) ?: return

        // Redis에 저장된 Refresh Token 삭제
        redisTemplate.delete(refreshToken)

        // Refresh Token 쿠키 삭제
        removeCookie(RefreshTokenName.USER_REFRESH_TOKEN.name, response)
    }
}