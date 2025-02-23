package com.example.customformlogin.global.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonPropertyOrder({"code", "message", "result"})
public class CustomResponse<T> {

    @JsonProperty("code")
    private HttpStatus code;
    @JsonProperty("message")
    private String message;
    @JsonProperty("result")
    private T result;

    public static <T> CustomResponse<T> onSuccess(T result) {
        return new CustomResponse<>(HttpStatus.OK, "성공", result);
    }

    public static <T> CustomResponse<T> onFailure(HttpStatus status, String message, T result) {
        return new CustomResponse<>(status, message, result);
    }
}
