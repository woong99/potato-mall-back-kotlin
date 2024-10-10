package com.potatowoong.potatomallbackkotlin.global.auth.jwt.dto

data class TokenDto(
    val accessToken: AccessTokenDto,
    val refreshToken: RefreshTokenDto
)

data class AccessTokenDto(
    val token: String,
    val expiredIn: Long
)

data class RefreshTokenDto(
    val token: String,
    val expiredIn: Long
) {

    /**
     * 만료까지 남은 시간을 초 단위로 반환
     */
    fun getExpiresInSecond(): Long {
        return (expiredIn - System.currentTimeMillis()) / 1000
    }
}
