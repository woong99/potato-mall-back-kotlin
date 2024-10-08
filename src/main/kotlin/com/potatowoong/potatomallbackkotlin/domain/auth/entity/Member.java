package com.potatowoong.potatomallbackkotlin.domain.auth.entity;

import com.potatowoong.potatomallbackkotlin.domain.auth.dto.response.UserSignUpReqDto;
import com.potatowoong.potatomallbackkotlin.domain.auth.enums.SocialType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Comment("사용자 계정 정보")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @Column(name = "user_id", length = 50, nullable = false, updatable = false)
    @Comment("아이디")
    private String userId;

    @Column(name = "password", length = 100)
    @Comment("비밀번호")
    private String password;

    @Column(name = "nickname", length = 20, nullable = false)
    @Comment("닉네임")
    private String nickname;

    @Column(name = "social_type", updatable = false)
    @Comment("SNS 로그인 유형(KAKAO, NAVER, GOOGLE)")
    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    @Builder
    public Member(String userId, String password, String nickname, SocialType socialType) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.socialType = socialType;
    }

    public static Member of(UserSignUpReqDto dto) {
        return Member.builder()
                .userId(dto.getUserId())
                .password(dto.getPassword())
                .nickname(dto.getNickname())
                .build();
    }
}
