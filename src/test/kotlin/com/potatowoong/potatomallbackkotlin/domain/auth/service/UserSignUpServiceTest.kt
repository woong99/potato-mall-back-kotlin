package com.potatowoong.potatomallbackkotlin.domain.auth.service

import com.potatowoong.potatomallbackkotlin.domain.auth.repository.MemberRepository
import com.potatowoong.potatomallbackkotlin.fixtures.createUserSignUpReqDto
import com.potatowoong.potatomallbackkotlin.global.exception.CustomException
import com.potatowoong.potatomallbackkotlin.global.exception.ErrorCode
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.crypto.password.PasswordEncoder

class UserSignUpServiceTest : BehaviorSpec({

    val memberRepository = mockk<MemberRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()

    val userSignUpService = UserSignUpService(memberRepository, passwordEncoder)

    afterTest {
        clearAllMocks()
    }

    Given("모든 케이스를 통과했을 때") {
        every { memberRepository.existsByUserId(any()) } returns false
        every { memberRepository.existsByNickname(any()) } returns false
        every { passwordEncoder.encode(any()) } returns "encodedPassword"
        every { memberRepository.save(any()) } returns mockk()

        When("회원가입하면") {
            userSignUpService.signUp(createUserSignUpReqDto())

            Then("성공") {
                verify { memberRepository.existsByUserId(any()) }
                verify { memberRepository.existsByNickname(any()) }
                verify { passwordEncoder.encode(any()) }
                verify { memberRepository.save(any()) }
            }
        }
    }

    Given("아이디가 중복일 때") {
        every { memberRepository.existsByUserId(any()) } returns true

        When("회원가입하면") {
            val exception = shouldThrow<CustomException> {
                userSignUpService.signUp(createUserSignUpReqDto())
            }

            Then("예외 발생") {
                exception.errorCode shouldBe ErrorCode.DUPLICATE_USER_ID

                verify { memberRepository.existsByUserId(any()) }
                verify(exactly = 0) { memberRepository.existsByNickname(any()) }
                verify(exactly = 0) { passwordEncoder.encode(any()) }
                verify(exactly = 0) { memberRepository.save(any()) }
            }
        }
    }

    Given("닉네임이 중복일 때") {
        every { memberRepository.existsByUserId(any()) } returns false
        every { memberRepository.existsByNickname(any()) } returns true

        When("회원가입하면") {
            val exception = shouldThrow<CustomException> {
                userSignUpService.signUp(createUserSignUpReqDto())
            }

            Then("예외 발생") {
                exception.errorCode shouldBe ErrorCode.DUPLICATE_NICKNAME

                verify { memberRepository.existsByUserId(any()) }
                verify { memberRepository.existsByNickname(any()) }
                verify(exactly = 0) { passwordEncoder.encode(any()) }
                verify(exactly = 0) { memberRepository.save(any()) }
            }
        }
    }

    Given("비밀번호와 비밀번호 확인이 일치하지 않을 때") {
        every { memberRepository.existsByUserId(any()) } returns false
        every { memberRepository.existsByNickname(any()) } returns false

        When("회원가입하면") {
            val exception = shouldThrow<CustomException> {
                userSignUpService.signUp(createUserSignUpReqDto(password = "wrongPassword"))
            }

            Then("예외 발생") {
                exception.errorCode shouldBe ErrorCode.PASSWORD_NOT_MATCHED

                verify { memberRepository.existsByUserId(any()) }
                verify { memberRepository.existsByNickname(any()) }
                verify(exactly = 0) { passwordEncoder.encode(any()) }
                verify(exactly = 0) { memberRepository.save(any()) }
            }
        }
    }

    Given("아이디가 이미 존재하는 경우") {
        every { memberRepository.existsByUserId(any()) } returns true

        When("아이디 중복 검사 시") {
            val result = userSignUpService.checkDuplicateId("test")

            Then("true 반환") {
                result shouldBe true
            }
        }
    }

    Given("아이디가 존재하지 않는 경우") {
        every { memberRepository.existsByUserId(any()) } returns false

        When("아이디 중복 검사 시") {
            val result = userSignUpService.checkDuplicateId("test")

            Then("false 반환") {
                result shouldBe false
            }
        }
    }

    Given("닉네임이 이미 존재하는 경우") {
        every { memberRepository.existsByNickname(any()) } returns true

        When("닉네임 중복 검사 시") {
            val result = userSignUpService.checkDuplicateNickname("test")

            Then("true 반환") {
                result shouldBe true
            }
        }
    }

    Given("닉네임이 존재하지 않는 경우") {
        every { memberRepository.existsByNickname(any()) } returns false

        When("닉네임 중복 검사 시") {
            val result = userSignUpService.checkDuplicateNickname("test")

            Then("false 반환") {
                result shouldBe false
            }
        }
    }
})