package com.example.todoapp.presentation.form;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Todo の作成・編集画面用のフォームクラス。
 */
@Data
public class TodoForm {
    
    /**
     * 公開ID（編集時のみ使用、画面では hidden field で保持）
     */
    private String publicId;
    
    /**
     * タイトル（必須、最大100文字）
     */
    @NotBlank(message = "タイトルを入力してください")
    @Size(max = 100, message = "タイトルは100文字以内で入力してください")
    private String title;
    
    /**
     * 詳細（任意、最大1000文字）
     */
    @Size(max = 1000, message = "詳細は1000文字以内で入力してください")
    private String detail;
    
    /**
     * 期限日（本日以降）
     */
    @NotNull(message = "期限日を入力してください")
    @FutureOrPresent(message = "期限日は本日以降で入力してください")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
}
