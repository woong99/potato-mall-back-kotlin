package com.potatowoong.potatomallbackkotlin.domain.auth.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


data class UserSignUpReqDto(

    @field:NotBlank(message = "아이디는 필수 입력 값입니다.")
    @field:Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하로 입력해주세요.")
    val userId: String,

    @field:NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @field:Size(min = 4, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    var password: String,

    @field:NotBlank(message = "비밀번호 확인은 필수 입력 값입니다.")
    val passwordConfirm: String,

    @field:NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @field:Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하로 입력해주세요.")
    val nickname: String
) {
    // 비밀번호 변경
    fun modifyPassword(password: String) {
        this.password = password
    }
}
