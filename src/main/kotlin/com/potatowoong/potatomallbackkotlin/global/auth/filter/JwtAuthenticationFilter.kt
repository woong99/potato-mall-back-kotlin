package com.potatowoong.potatomallbackkotlin.global.auth.filter

import com.potatowoong.potatomallbackkotlin.global.auth.jwt.component.JwtTokenProvider
import com.potatowoong.potatomallbackkotlin.global.exception.CustomException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : OncePerRequestFilter() {

    companion object {
        private const val BEARER_PREFIX = "Bearer "
        private const val AUTHORIZATION_HEADER = "Authorization"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = resolveToken(request)

        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                val authentication = jwtTokenProvider.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: CustomException) {
            SecurityContextHolder.clearContext()
            request.setAttribute("exception", e)
        }

        filterChain.doFilter(request, response)
    }

    /**
     * Request Header에서 Token을 추출하는 메소드
     *
     * @param request
     * @return
     */
    private fun resolveToken(
        request: HttpServletRequest
    ): String? {
        return request.getHeader(AUTHORIZATION_HEADER)
            ?.takeIf { it.startsWith(BEARER_PREFIX) && it.length > 7 }
            ?.substring(7)
    }
}