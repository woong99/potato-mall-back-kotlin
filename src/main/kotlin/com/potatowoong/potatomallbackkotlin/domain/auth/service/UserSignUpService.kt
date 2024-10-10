package com.potatowoong.potatomallbackkotlin.domain.auth.service

import com.potatowoong.potatomallbackkotlin.domain.auth.dto.request.UserSignUpReqDto
import com.potatowoong.potatomallbackkotlin.domain.auth.entity.Member
import com.potatowoong.potatomallbackkotlin.domain.auth.repository.MemberRepository
import com.potatowoong.potatomallbackkotlin.global.exception.CustomException
import com.potatowoong.potatomallbackkotlin.global.exception.ErrorCode
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserSignUpService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) {

    /**
     * 회원가입
     */
    @Transactional
    fun signUp(dto: UserSignUpReqDto) {
        // 아이디 중복 체크
        if (memberRepository.existsByUserId(dto.userId)) {
            throw CustomException(ErrorCode.DUPLICATE_USER_ID)
        }

        // 닉네임 중복 체크
        if (memberRepository.existsByNickname(dto.nickname)) {
            throw CustomException(ErrorCode.DUPLICATE_NICKNAME)
        }

        // 비밀번호 일치 여부 체크
        if (dto.password != dto.passwordConfirm) {
            throw CustomException(ErrorCode.PASSWORD_NOT_MATCHED)
        }

        // 비밀번호 암호화
        dto.modifyPassword(passwordEncoder.encode(dto.password))

        // 회원가입
        val member = Member.of(dto)
        memberRepository.save(member)
    }

    /**
     * 아이디 중복 검사
     */
    @Transactional(readOnly = true)
    fun checkDuplicateId(userId: String): Boolean {
        return memberRepository.existsByUserId(userId)
    }

    /**
     * 닉네임 중복 검사
     */
    @Transactional(readOnly = true)
    fun checkDuplicateNickname(nickname: String): Boolean {
        return memberRepository.existsByNickname(nickname)
    }
}