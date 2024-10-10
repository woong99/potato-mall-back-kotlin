package com.potatowoong.potatomallbackkotlin.global.common.utils

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse

/**
 * 쿠키 생성
 *
 * @param cookieName 쿠키 이름
 * @param value 쿠키 값
 * @param maxAge 쿠키 만료 시간
 * @param response HttpServletResponse
 */
fun createCookie(
    cookieName: String,
    value: String,
    maxAge: Long,
    response: HttpServletResponse
) {
    val cookie = Cookie(cookieName, value)
    cookie.isHttpOnly = true
    cookie.secure = true
    cookie.maxAge = maxAge.toInt()
    cookie.path = "/"

    response.addCookie(cookie)
}

/**
 * 쿠키의 값 조회
 *
 * @param cookies 쿠키 배열
 * @param name 쿠키 이름
 * @return 쿠키 값
 */
fun getCookieValue(
    cookies: Array<Cookie>?,
    name: String
): String? {
    if (cookies == null) {
        return null
    }

    for (cookie in cookies) {
        if (cookie.name == name) {
            return cookie.value
        }
    }

    return null
}

/**
 * 쿠키 삭제
 *
 * @param cookieName 쿠키 이름
 * @param response HttpServletResponse
 */
fun removeCookie(
    cookieName: String,
    response: HttpServletResponse
) {
    val cookie = Cookie(cookieName, null)
    cookie.isHttpOnly = true
    cookie.secure = true
    cookie.maxAge = 0
    cookie.path = "/"

    response.addCookie(cookie)
}