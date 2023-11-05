package com.capstone.backend.exception;

import com.capstone.backend.model.CustomError;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiException extends RuntimeException{
    HttpStatus status;
    CustomError error;

    public static ApiException notFoundException(String message) {
        return ApiException.builder().status(HttpStatus.NOT_FOUND)
                .error(CustomError.builder()
                        .code("404")
                        .message(message)
                        .build())
                .build();
    }

    public static ApiException badRequestException(String message) {
        return ApiException.builder().status(HttpStatus.BAD_REQUEST)
                .error(CustomError.builder()
                        .code("400")
                        .message(message)
                        .build())
                .build();
    }

    public static ApiException internalServerException(String message) {
        return ApiException.builder().status(HttpStatus.INTERNAL_SERVER_ERROR)
                .error(CustomError.builder()
                        .code("500")
                        .message(message)
                        .build())
                .build();
    }

    public static ApiException forBiddenException(String message) {
        return ApiException.builder().status(HttpStatus.FORBIDDEN)
                .error(CustomError.builder()
                        .code("403")
                        .message(message)
                        .build())
                .build();
    }

    public static ApiException unAuthorizedException(String message) {
        return ApiException.builder().status(HttpStatus.UNAUTHORIZED)
                .error(CustomError.builder()
                        .code("401")
                        .message(message)
                        .build())
                .build();
    }

    public static ApiException maxSizeException(String message) {
        return ApiException.builder().status(HttpStatus.EXPECTATION_FAILED)
                .error(CustomError.builder()
                        .code("417")
                        .message(message)
                        .build())
                .build();
    }

    public static ApiException conflictResourceException(String message) {
        return ApiException.builder().status(HttpStatus.BAD_REQUEST)
                .error(CustomError.builder()
                        .code("409")
                        .message(message)
                        .build())
                .build();
    }
}