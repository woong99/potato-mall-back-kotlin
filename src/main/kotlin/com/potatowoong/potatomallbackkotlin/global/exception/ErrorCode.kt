package com.potatowoong.potatomallbackkotlin.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
    val httpStatus: HttpStatus,
    val code: String,
    val message: String
) {
    // 공통 계정 관련 에러코드
    EXIST_USER_ID(HttpStatus.BAD_REQUEST, "AU001", "이미 존재하는 아이디입니다."),
    MISMATCH_PASSWORD_AND_PASSWORD_CONFIRM(HttpStatus.BAD_REQUEST, "AU002", "비밀번호가 일치하지 않습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AU003", "만료된 엑세스 토큰입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "AU004", "유효하지 않은 엑세스 토큰입니다."),
    FAILED_TO_LOGIN(HttpStatus.BAD_REQUEST, "AU005", "로그인에 실패하였습니다. 아이디 또는 비밀번호를 확인해주세요."),
    ADMIN_NOT_FOUND(HttpStatus.BAD_REQUEST, "AU006", "존재하지 않는 관리자입니다."),
    INCORRECT_PASSWORD_LENGTH(HttpStatus.BAD_REQUEST, "AU007", "비밀번호는 8자 이상 20자 이하로 입력해주세요."),
    SELF_DELETION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "AU008", "자신의 계정은 삭제할 수 없습니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AU009", "접근 권한이 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AU010", "로그인이 필요합니다."),

    // 사용자 계정 관련 에러코드
    DUPLICATE_USER_ID(HttpStatus.BAD_REQUEST, "UA001", "이미 존재하는 아이디입니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "UA002", "이미 존재하는 닉네임입니다."),
    PASSWORD_NOT_MATCHED(HttpStatus.BAD_REQUEST, "UA003", "비밀번호가 일치하지 않습니다."),
    WRONG_LOGIN_TYPE(HttpStatus.BAD_REQUEST, "UA004", "잘못된 로그인 타입입니다."),

    // 파일 관련 에러코드
    FAILED_TO_UPLOAD_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "PF001", "파일 업로드에 실패하였습니다."),
    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "PF002", "지원하지 않는 파일 확장자입니다."),
    EXCEEDED_FILE_SIZE(HttpStatus.BAD_REQUEST, "PF003", "파일 크기가 초과되었습니다."),
    FAILED_TO_DELETE_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "PF004", "파일 삭제에 실패하였습니다."),
    ATCH_FILE_NOT_FOUND(HttpStatus.BAD_REQUEST, "PF005", "존재하지 않는 첨부파일입니다."),

    // 상품 관련 에러코드
    PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "PD001", "존재하지 않는 상품입니다."),
    NOT_FOUND_THUMBNAIL(HttpStatus.BAD_REQUEST, "PD002", "썸네일 이미지를 등록해주세요."),
    DUPLICATED_PRODUCT_NAME(HttpStatus.BAD_REQUEST, "PD003", "이미 존재하는 상품명입니다."),
    PRODUCT_STOCK_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "PD004", "상품 재고량이 부족합니다."),

    // 상품 카테고리 관련 에러코드
    DUPLICATED_CATEGORY_NAME(HttpStatus.BAD_REQUEST, "PC001", "이미 존재하는 카테고리명입니다."),
    NOT_FOUND_CATEGORY(HttpStatus.BAD_REQUEST, "PC002", "존재하지 않는 카테고리입니다."),
    EXIST_PRODUCT_IN_CATEGORY(HttpStatus.BAD_REQUEST, "PC003", "해당 카테고리에 상품이 존재합니다."),

    // 상품 좋아요 관련 에러코드
    ALREADY_LIKED_PRODUCT(HttpStatus.BAD_REQUEST, "PL001", "이미 좋아요한 상품입니다."),
    NOT_LIKED_PRODUCT(HttpStatus.BAD_REQUEST, "PL002", "좋아요하지 않은 상품입니다."),

    // 상품 리뷰 관련 에러코드
    REVIEW_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "RV001", "이미 리뷰를 작성한 상품입니다."),
    REVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST, "RV002", "존재하지 않는 리뷰입니다."),

    // 장바구니 관련 에러코드
    PRODUCT_QUANTITY_EXCEEDED(HttpStatus.BAD_REQUEST, "SC001", "상품 재고량을 초과하였습니다."),
    SHOPPING_CART_NOT_FOUND(HttpStatus.BAD_REQUEST, "SC002", "존재하지 않는 장바구니 상품입니다."),

    // 결제 관련 에러코드
    PAY_TRANSACTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "PT001", "존재하지 않는 결제 트랜잭션입니다."),
    PAY_AMOUNT_NOT_MATCH(HttpStatus.BAD_REQUEST, "PT002", "결제 금액이 일치하지 않습니다."),
    PAYMENT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "PT003", "결제에 실패하였습니다."),

    // 공통 에러코드
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ISE001", "서버 내부 오류입니다.");
}