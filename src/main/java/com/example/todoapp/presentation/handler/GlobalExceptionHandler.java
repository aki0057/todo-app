package com.example.todoapp.presentation.handler;

import com.example.todoapp.domain.exception.TodoNotFoundException;
import com.example.todoapp.presentation.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/** アプリケーション全体の例外をハンドリングするグローバルハンドラ。 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** TodoNotFoundException をハンドリングし、HTTP 404 を返す。 */
    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTodoNotFoundException(
            TodoNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse =
                ErrorResponse.of(
                        HttpStatus.NOT_FOUND.value(),
                        "Not Found",
                        ex.getMessage(),
                        request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /** IllegalArgumentException をハンドリングし、HTTP 400 を返す。 ドメインのバリデーションエラーなどで使用される。 */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {

        ErrorResponse errorResponse =
                ErrorResponse.of(
                        HttpStatus.BAD_REQUEST.value(),
                        "Bad Request",
                        ex.getMessage(),
                        request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /** その他の予期しない例外をハンドリングし、HTTP 500 を返す。 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {

        ErrorResponse errorResponse =
                ErrorResponse.of(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Internal Server Error",
                        "予期しないエラーが発生しました",
                        request.getDescription(false).replace("uri=", ""));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
