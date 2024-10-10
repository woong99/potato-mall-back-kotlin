package com.potatowoong.potatomallbackkotlin.global.auth.jwt.component

import com.potatowoong.potatomallbackkotlin.global.auth.jwt.dto.AccessTokenDto
import com.potatowoong.potatomallbackkotlin.global.auth.jwt.dto.RefreshTokenDto
import com.potatowoong.potatomallbackkotlin.global.auth.jwt.dto.TokenDto
import com.potatowoong.potatomallbackkotlin.global.exception.CustomException
import com.potatowoong.potatomallbackkotlin.global.exception.ErrorCode
import io.jsonwebtoken.*
import io.jsonwebtoken.Jwts.SIG
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecurityException
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Collectors
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}") private val secretKey: String,
    @Value("\${jwt.access-token-expiration}") private val accessTokenExpiredTime: Long,
    @Value("\${jwt.refresh-token-expiration}") private val refreshTokenExpiredTime: Long
) {

    /**
     * Access Token, Refresh Token 생성
     */
    fun generateToken(authentication: Authentication): TokenDto {
        val accessTokenDto = generateAccessToken(authentication)
        val refreshTokenDto = generateRefreshToken()

        return TokenDto(accessTokenDto, refreshTokenDto)
    }

    /**
     * Access Token을 파싱하여 Authentication 객체를 반환하는 메소드
     */
    fun getAuthentication(
        accessToken: String
    ): Authentication {
        // 토큰 복호화
        val claims = parseClaims(accessToken)
        if (claims["auth"] == null) {
            throw CustomException(ErrorCode.INVALID_ACCESS_TOKEN)
        }

        // 권한정보 획득
        val authorities = claims["auth"].toString()
            .split(",")
            .map { SimpleGrantedAuthority(it) }

        // Principal 객체 생성
        val principal = User(claims.subject, "", authorities)
        return UsernamePasswordAuthenticationToken(principal, "", authorities)
    }

    /**
     * Access Token의 유효성을 검증하는 메소드
     */
    fun validateToken(
        accessToken: String
    ): Boolean {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(accessToken)
            return true
        } catch (e: Exception) {
            when (e) {
                is SecurityException, is MalformedJwtException, is UnsupportedJwtException -> {
                    throw CustomException(ErrorCode.INVALID_ACCESS_TOKEN)
                }

                is ExpiredJwtException -> {
                    throw CustomException(ErrorCode.EXPIRED_ACCESS_TOKEN)
                }

                else -> {
                    throw e
                }
            }
        }
    }

    /**
     * Access Token 생성
     */
    private fun generateAccessToken(authentication: Authentication): AccessTokenDto {
        val authorities = authentication.authorities.stream()
            .map { it.authority }
            .collect(Collectors.joining(","))
        val now = Date().time

        // Access Token 만료 시간 설정
        val accessTokenExpiresIn = Date(now + 1000 * accessTokenExpiredTime)

        // Access Token 생성
        val accessToken = Jwts.builder()
            .subject(authentication.name)
            .claim("auth", authorities)
            .expiration(accessTokenExpiresIn)
            .signWith(getSigningKey(), SIG.HS256)
            .compact()

        return AccessTokenDto(accessToken, accessTokenExpiresIn.time)
    }

    /**
     * Refresh Token 생성
     */
    private fun generateRefreshToken(): RefreshTokenDto {
        val now = Date().time

        // Refresh Token 만료 시간 설정
        val refreshTokenExpiresIn = Date(now + 1000 * refreshTokenExpiredTime)

        // Refresh Token 생성
        val refreshToken = Jwts.builder()
            .expiration(refreshTokenExpiresIn)
            .signWith(getSigningKey(), SIG.HS256)
            .compact()

        return RefreshTokenDto(refreshToken, refreshTokenExpiresIn.time)
    }

    /**
     * Get signing key
     */
    private fun getSigningKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(secretKey)

        return Keys.hmacShaKeyFor(keyBytes)
    }

    /**
     * Access Token을 파싱하여 Claims를 반환하는 메소드
     */
    private fun parseClaims(
        accessToken: String
    ): Claims {
        try {
            return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(accessToken)
                .payload
        } catch (e: ExpiredJwtException) {
            throw CustomException(ErrorCode.EXPIRED_ACCESS_TOKEN)
        }
    }
}