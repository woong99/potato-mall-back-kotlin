package com.potatowoong.potatomallbackkotlin.global.common.dto

import org.springframework.http.HttpStatus

enum class ResponseText(
    val httpStatus: HttpStatus,
    val message: String
) {
    OK(HttpStatus.OK, "OK"),

    DUPLICATE(HttpStatus.OK, "DUPLICATE"),

    SUCCESS_SIGN_UP(HttpStatus.OK, "회원가입 성공"),

    SUCCESS_LOGOUT(HttpStatus.OK, "로그아웃 성공"),

    SUCCESS_ADD_ADMIN(HttpStatus.OK, "관리자 등록 성공"),

    SUCCESS_MODIFY_ADMIN(HttpStatus.OK, "관리자 수정 성공"),

    SUCCESS_REMOVE_ADMIN(HttpStatus.OK, "관리자 삭제 성공"),

    SUCCESS_ADD_PRODUCT(HttpStatus.OK, "상품 등록 성공"),

    SUCCESS_MODIFY_PRODUCT(HttpStatus.OK, "상품 수정 성공"),

    SUCCESS_REMOVE_PRODUCT(HttpStatus.OK, "상품 삭제 성공"),

    SUCCESS_ADD_PRODUCT_CATEGORY(HttpStatus.OK, "상품 카테고리 등록 성공"),

    SUCCESS_MODIFY_PRODUCT_CATEGORY(HttpStatus.OK, "상품 카테고리 수정 성공"),

    SUCCESS_REMOVE_PRODUCT_CATEGORY(HttpStatus.OK, "상품 카테고리 삭제 성공"),

    SUCCESS_ADD_PRODUCT_LIKE(HttpStatus.OK, "상품 좋아요 추가 성공"),

    SUCCESS_REMOVE_PRODUCT_LIKE(HttpStatus.OK, "상품 좋아요 삭제 성공"),

    SUCCESS_ADD_REVIEW(HttpStatus.OK, "리뷰 등록 성공"),

    SUCCESS_MODIFY_REVIEW(HttpStatus.OK, "리뷰 수정 성공"),

    SUCCESS_REMOVE_REVIEW(HttpStatus.OK, "리뷰 삭제 성공"),

    SUCCESS_ADD_SHOPPING_CART(HttpStatus.OK, "장바구니 상품 등록 성공"),

    SUCCESS_MODIFY_SHOPPING_CART(HttpStatus.OK, "장바구니 상품 수정 성공"),

    SUCCESS_REMOVE_SHOPPING_CART(HttpStatus.OK, "장바구니 상품 삭제 성공"),

    SUCCESS_CHECK_AVAILABLE_PAY(HttpStatus.OK, "결제 가능 여부 확인 성공"),

    SUCCESS_CHECK_AFTER_PAY(HttpStatus.OK, "결제 후 파라미터 검증 성공"),

    SUCCESS_PAY(HttpStatus.OK, "결제 성공");
}