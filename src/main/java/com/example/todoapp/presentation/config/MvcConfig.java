package com.example.todoapp.presentation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC の設定クラス。
 * ビューコントローラによるリダイレクトなど、プレゼンテーション層の構成を定義する。
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * ルートパス (/) を /todos へリダイレクトする。
     * ロジックを持たない固定リダイレクトは ViewController で実装するのが DDD のベストプラクティス。
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/todos");
    }
}
