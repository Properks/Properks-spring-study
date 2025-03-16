package com.example.springrestdocs.apiPayload;

import com.example.springrestdocs.apiPayload.code.BaseCode;
import com.example.springrestdocs.apiPayload.code.ReasonDTO;
import com.example.springrestdocs.apiPayload.code.base.SuccessCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String code;
    private final String message;
    private T result;


    public static <T> ApiResponse<T> onSuccess(T result) {
        return onSuccess(SuccessCode._OK, result);
    }

    public static <T> ApiResponse<T> onSuccess(SuccessCode code, T result) {
        ReasonDTO dto = code.getReasonHttpStatus();
        return new ApiResponse<>(true, dto.getCode(), dto.getMessage(), result);
    }

    public static <T> ApiResponse<T> of(BaseCode code, T result) {
        return new ApiResponse<>(true, code.getReasonHttpStatus().getCode(), code.getReasonHttpStatus().getMessage(),
                result);
    }

    public static <T> ApiResponse<T> onFailure(String code, String message, T data) {
        return new ApiResponse<>(false, code, message, data);
    }

    public static <T> ApiResponse<T> onFailure(String code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .build();
    }
}