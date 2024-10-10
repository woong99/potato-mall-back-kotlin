package com.potatowoong.potatomallbackkotlin.fixtures

import com.potatowoong.potatomallbackkotlin.domain.auth.dto.request.UserLoginReqDto
import com.potatowoong.potatomallbackkotlin.domain.auth.entity.Member
import com.potatowoong.potatomallbackkotlin.domain.auth.enums.SocialType

fun createUserLoginReqDto(
    userId: String = "테스트",
    password: String = "test1234",
): UserLoginReqDto {
    return UserLoginReqDto(userId, password)
}

fun createMember(
    userId: String = "테스트",
    password: String = "test1234",
    nickname: String = "테스트 계정",
    socialType: SocialType? = null
): Member {
    return Member("userId", "password", "nickname", socialType)
}