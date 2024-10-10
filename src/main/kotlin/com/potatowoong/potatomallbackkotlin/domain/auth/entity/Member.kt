package com.potatowoong.potatomallbackkotlin.domain.auth.entity

import com.potatowoong.potatomallbackkotlin.domain.auth.dto.request.UserSignUpReqDto
import com.potatowoong.potatomallbackkotlin.domain.auth.enums.SocialType
import jakarta.persistence.*
import org.hibernate.annotations.Comment

@Entity
class Member(

    @Id
    @Column(name = "user_id", length = 50, nullable = false, updatable = false)
    @Comment("아이디")
    val userId: String,

    @Column(name = "password", length = 100)
    @Comment("비밀번호")
    val password: String?,

    @Column(name = "nickname", length = 20, nullable = false)
    @Comment("닉네임")
    var nickname: String,

    @Column(name = "social_type", updatable = false)
    @Comment("SNS 로그인 유형(KAKAO, NAVER, GOOGLE)")
    @Enumerated(value = EnumType.STRING)
    var socialType: SocialType? = null
) {

    companion object {
        fun of(userSignUpReqDto: UserSignUpReqDto): Member {
            return Member(
                userSignUpReqDto.userId,
                userSignUpReqDto.password,
                userSignUpReqDto.nickname
            )
        }
    }
}