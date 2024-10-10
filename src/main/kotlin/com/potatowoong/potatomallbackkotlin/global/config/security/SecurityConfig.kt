package com.potatowoong.potatomallbackkotlin.global.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig {

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

        // URL 권한 설정
        http
            .authorizeHttpRequests {
                it
                    .requestMatchers(*permitAll).permitAll()
                    .anyRequest().authenticated()
            }

        return http.build()
    }
}