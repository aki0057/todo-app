package com.example.todoapp.presentation.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * エラーレスポンスを表すDTO。
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;
    
    private final int status;
    
    private final String error;
    
    private final String message;
    
    private final String path;
    
    /**
     * パスなしでエラーレスポンスを作成するファクトリメソッド。
     */
    public static ErrorResponse of(int status, String error, String message) {
        return new ErrorResponse(
            LocalDateTime.now(),
            status,
            error,
            message,
            null
        );
    }
    
    /**
     * パス情報を含めてエラーレスポンスを作成するファクトリメソッド。
     */
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(
            LocalDateTime.now(),
            status,
            error,
            message,
            path
        );
    }
}
