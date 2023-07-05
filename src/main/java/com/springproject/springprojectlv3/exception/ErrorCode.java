package com.springproject.springprojectlv3.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

// 예외 케이스 관리
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 토큰 전달 하지 않은 경우, 정상 토큰이 아닌 경우
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다."),

    // 토큰이 있으며 유효한 토큰이나, 해당 사용자의 게시글/댓글이 아닌 경우 (즉, 해당 사용자의 토큰이 아닌 경우)
    AUTHORIZATION(HttpStatus.BAD_REQUEST, "작성자만 삭제/수정할 수 있습니다."),

    // DB 에 이미 존재하는 username 으로 회원가입 요청한 경우
    DUPLICATED_USERNAME(HttpStatus.BAD_REQUEST, "중복된 username 입니다"),

    // 로그인 시, username 과 password 중 일치하지 않는 정보가 있을 경우
    NOT_MATCH_INFORMATION(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),

    // DB 에 해당 게시글이 존재하지 않는 경우
    NOT_FOUND_BOARD(HttpStatus.BAD_REQUEST, "게시글을 찾을 수 없습니다."),

    // DB 에 해당 댓글이 존재하지 않는 경우
    NOT_FOUND_COMMENT(HttpStatus.BAD_REQUEST, "댓글을 찾을 수 없습니다."),

    // admin 계정으로 회원가입 시, ADMIN_TOKEN 과 일치하지 않을 경우
    NOT_MATCH_ADMIN_TOKEN(HttpStatus.BAD_REQUEST, "관리자 암호가 일치하지 않습니다.");
    
    private final HttpStatus httpStatus;
    private final String message;
}
