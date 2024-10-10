package com.potatowoong.potatomallbackkotlin.domain.auth.dto.request

import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank

data class UserLoginReqDto(

    @field:NotBlank(message = "아이디를 입력해주세요.")
    val id: String,

    @field:NotBlank(message = "비밀번호를 입력해주세요.")
    val password: String
)
