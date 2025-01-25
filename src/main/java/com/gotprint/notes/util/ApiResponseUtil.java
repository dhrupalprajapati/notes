package com.gotprint.notes.util;

import com.gotprint.notes.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.validation.Errors;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class ApiResponseUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Mono<ApiResponse> successResponse(String message, HttpStatusCode... httpStatusCodes) {
        return Mono.just(new ApiResponse(httpStatusCodes.length > 0 ? httpStatusCodes[0].value() : HttpStatus.OK.value(), message, getCurrentTimestamp()));
    }

    public static Mono<ApiResponse> errorResponse(String message, HttpStatusCode... httpStatusCodes) {
        return Mono.just(new ApiResponse(httpStatusCodes.length > 0 ? httpStatusCodes[0].value() : HttpStatus.BAD_REQUEST.value(), message, getCurrentTimestamp()));
    }

    public static Mono<ApiResponse> errorResponse(Errors errors, HttpStatusCode... httpStatusCodes) {
        return Mono.defer(() -> {
            String errorMessage = errors.getFieldErrors().stream()
                    .map(error -> String.format("%s : %s", error.getField(), error.getDefaultMessage()))
                    .collect(Collectors.joining(", "));
            return Mono.just(new ApiResponse(httpStatusCodes.length > 0 ? httpStatusCodes[0].value() : HttpStatus.BAD_REQUEST.value(), errorMessage, getCurrentTimestamp()));
        });
    }

    private static String getCurrentTimestamp() {
        return LocalDateTime.now().format(FORMATTER);
    }
}
