package com.potatowoong.potatomallbackkotlin.global.auth.jwt.component

import com.potatowoong.potatomallbackkotlin.global.auth.jwt.dto.AccessTokenDto
import com.potatowoong.potatomallbackkotlin.global.auth.jwt.dto.RefreshTokenDto
import com.potatowoong.potatomallbackkotlin.global.auth.jwt.dto.TokenDto
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.Jwts.SIG
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
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
     *
     * @param authentication
     * @return
     */
    fun generateToken(authentication: Authentication): TokenDto {
        val accessTokenDto = generateAccessToken(authentication)
        val refreshTokenDto = generateRefreshToken()

        return TokenDto(accessTokenDto, refreshTokenDto)
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
}