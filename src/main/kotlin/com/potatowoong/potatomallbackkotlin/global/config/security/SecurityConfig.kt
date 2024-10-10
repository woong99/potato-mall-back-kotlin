package com.potatowoong.potatomallbackkotlin.global.config.security

import com.potatowoong.potatomallbackkotlin.global.auth.filter.JwtAuthenticationFilter
import com.potatowoong.potatomallbackkotlin.global.auth.jwt.component.JwtTokenProvider
import com.potatowoong.potatomallbackkotlin.global.auth.jwt.handler.JwtAccessDeniedHandler
import com.potatowoong.potatomallbackkotlin.global.auth.jwt.handler.JwtAuthenticationEntryPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler
) {

    private val permitAll = arrayOf(
        "/api/v1/user/login",
        "/api/v1/user/sign-up",
        "/api/v1/user/check-duplicate-id",
        "/api/v1/user/check-duplicate-nickname"
    )

    /**
     * PasswordEncoder Bean 등록
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    /**
     * SecurityFilterChain Bean 등록
     */
    @Bean
    fun filterChain(
        http: HttpSecurity
    ): SecurityFilterChain {
        // Basic Auth 비활성화
        http
            .httpBasic { it.disable() }

        // CSRF 비활성화
        http
            .csrf { it.disable() }

        // Form Login 비활성화
        http
            .formLogin { it.disable() }

        // Session 비활성화
        http
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }

        // Exception Handler 등록
        http
            .exceptionHandling {
                it.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                it.accessDeniedHandler(jwtAccessDeniedHandler)
            }

        // URL 권한 설정
        http
            .authorizeHttpRequests {
                it
                    .requestMatchers(*permitAll).permitAll()
                    .anyRequest().authenticated()
            }

        // JWT Filter 등록
        http
            .addFilterBefore(
                JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )

        return http.build()
    }
}