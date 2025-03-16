package com.example.springrestdocs.exception;

import com.example.springrestdocs.apiPayload.code.BaseErrorCode;
import com.example.springrestdocs.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ArticleErrorCode implements BaseErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "ARTICLE400", "게시글을 찾지 못했습니다.");
    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .httpStatus(status)
                .code(code)
                .message(message)
                .isSuccess(false)
                .build();
    }
}
