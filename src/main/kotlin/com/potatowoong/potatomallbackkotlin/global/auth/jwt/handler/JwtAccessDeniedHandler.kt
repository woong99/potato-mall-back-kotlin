package com.potatowoong.potatomallbackkotlin.global.auth.jwt.handler

import com.potatowoong.potatomallbackkotlin.global.exception.CustomException
import com.potatowoong.potatomallbackkotlin.global.exception.ErrorCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerExceptionResolver

@Component
class JwtAccessDeniedHandler(
    @Qualifier("handlerExceptionResolver")
    private val resolver: HandlerExceptionResolver
) : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {
        resolver.resolveException(request!!, response!!, null, CustomException(ErrorCode.FORBIDDEN))
    }
}