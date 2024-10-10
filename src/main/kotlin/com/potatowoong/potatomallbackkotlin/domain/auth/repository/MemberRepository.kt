package com.potatowoong.potatomallbackkotlin.domain.auth.repository

import com.potatowoong.potatomallbackkotlin.domain.auth.entity.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, String> {

    fun existsByUserId(userId: String): Boolean

    fun existsByNickname(nickname: String): Boolean
}