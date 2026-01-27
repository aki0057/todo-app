package com.example.todoapp.domain.exception;

/**
 * Todo が見つからない場合にスローされる例外。
 */
public class TodoNotFoundException extends RuntimeException {
    
    public TodoNotFoundException(String message) {
        super(message);
    }
    
    public TodoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
