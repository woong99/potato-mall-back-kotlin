package com.potatowoong.potatomallbackkotlin.fixtures

import com.potatowoong.potatomallbackkotlin.domain.auth.dto.response.UserSignUpReqDto

fun createUserSignUpReqDto(
    userId: String = "테스트",
    password: String = "test1234",
    passwordConfirm: String = "test1234",
    nickname: String = "테스트 계정"
): UserSignUpReqDto {
    return UserSignUpReqDto(userId, password, passwordConfirm, nickname)
}