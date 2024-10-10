package com.potatowoong.potatomallbackkotlin.domain.auth.service

import com.potatowoong.potatomallbackkotlin.domain.auth.enums.SocialType
import com.potatowoong.potatomallbackkotlin.domain.auth.repository.MemberRepository
import com.potatowoong.potatomallbackkotlin.fixtures.createMember
import com.potatowoong.potatomallbackkotlin.fixtures.createUserLoginReqDto
import com.potatowoong.potatomallbackkotlin.global.auth.jwt.component.JwtTokenProvider
import com.potatowoong.potatomallbackkotlin.global.auth.jwt.dto.AccessTokenDto
import com.potatowoong.potatomallbackkotlin.global.auth.jwt.dto.RefreshTokenDto
import com.potatowoong.potatomallbackkotlin.global.auth.jwt.dto.TokenDto
import com.potatowoong.potatomallbackkotlin.global.exception.CustomException
import com.potatowoong.potatomallbackkotlin.global.exception.ErrorCode
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.*
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class UserLoginServiceTest : BehaviorSpec({

    val memberRepository: MemberRepository = mockk()
    val passwordEncoder: PasswordEncoder = mockk()
    val jwtTokenProvider: JwtTokenProvider = mockk()
    val redisTemplate: RedisTemplate<String, String> = mockk()
    val valueOperations: ValueOperations<String, String> = mockk()

    val userLoginService = UserLoginService(
        memberRepository,
        passwordEncoder,
        jwtTokenProvider,
        redisTemplate
    )

    afterTest {
        clearAllMocks()
    }

    val httpServletResponse = MockHttpServletResponse()

    Given("모든 케이스를 통과했을 때") {
        val tokenExpiresIn = LocalDateTime.now().plusDays(7).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        val accessTokenDto = AccessTokenDto("accessToken", tokenExpiresIn)
        val refreshTokenDto = RefreshTokenDto("refreshToken", tokenExpiresIn)
        val tokenDto = TokenDto(accessTokenDto, refreshTokenDto)

        every { memberRepository.findById(any()) } returns Optional.of(createMember())
        every { passwordEncoder.matches(any(), any()) } returns true
        every { jwtTokenProvider.generateToken(any()) } returns tokenDto
        every { redisTemplate.opsForValue() } returns valueOperations
        every { valueOperations.set(any(), any(), any(), any()) } just runs

        When("로그인하면") {
            val result = userLoginService.login(createUserLoginReqDto(), httpServletResponse)

            Then("성공") {
                result shouldNotBe null

                verify { memberRepository.findById(any()) }
                verify { passwordEncoder.matches(any(), any()) }
                verify { jwtTokenProvider.generateToken(any()) }
                verify { redisTemplate.opsForValue() }
                verify { valueOperations.set(any(), any(), any(), any()) }
            }
        }
    }

    Given("아이디가 존재하지 않을 때") {
        every { memberRepository.findById(any()) } returns Optional.empty()

        When("로그인하면") {
            val exception = shouldThrow<CustomException> {
                userLoginService.login(createUserLoginReqDto(), httpServletResponse)
            }

            Then("예외 발생") {
                exception.errorCode shouldBe ErrorCode.FAILED_TO_LOGIN

                verify { memberRepository.findById(any()) }
                verify(exactly = 0) { passwordEncoder.matches(any(), any()) }
                verify(exactly = 0) { jwtTokenProvider.generateToken(any()) }
                verify(exactly = 0) { redisTemplate.opsForValue() }
                verify(exactly = 0) { valueOperations.set(any(), any(), any(), any()) }
            }
        }
    }

    Given("비밀번호가 일치하지 않을 때") {
        every { memberRepository.findById(any()) } returns Optional.of(createMember())
        every { passwordEncoder.matches(any(), any()) } returns false

        When("로그인하면") {
            val exception = shouldThrow<CustomException> {
                userLoginService.login(createUserLoginReqDto(), httpServletResponse)
            }

            Then("예외 발생") {
                exception.errorCode shouldBe ErrorCode.FAILED_TO_LOGIN

                verify { memberRepository.findById(any()) }
                verify { passwordEncoder.matches(any(), any()) }
                verify(exactly = 0) { jwtTokenProvider.generateToken(any()) }
                verify(exactly = 0) { redisTemplate.opsForValue() }
                verify(exactly = 0) { valueOperations.set(any(), any(), any(), any()) }
            }
        }
    }

    Given("소셜 로그인 사용자인 경우") {
        every { memberRepository.findById(any()) } returns Optional.of(createMember(socialType = SocialType.GOOGLE))
        every { passwordEncoder.matches(any(), any()) } returns true

        When("로그인하면") {
            val exception = shouldThrow<CustomException> {
                userLoginService.login(createUserLoginReqDto(), httpServletResponse)
            }

            Then("예외 발생") {
                exception.errorCode shouldBe ErrorCode.WRONG_LOGIN_TYPE

                verify { memberRepository.findById(any()) }
                verify { passwordEncoder.matches(any(), any()) }
                verify(exactly = 0) { jwtTokenProvider.generateToken(any()) }
                verify(exactly = 0) { redisTemplate.opsForValue() }
                verify(exactly = 0) { valueOperations.set(any(), any(), any(), any()) }
            }
        }
    }
})