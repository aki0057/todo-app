package com.example.todoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Todo アプリケーションのメインクラス。
 *
 * <p>Spring Boot アプリケーションのエントリーポイント。
 */
@SpringBootApplication
public class TodoAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoAppApplication.class, args);
    }
}
