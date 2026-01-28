package com.example.todoapp.infrastructure.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Todo の永続化エンティティ。
 * <p>
 * データベース の todos テーブルにマップされ、
 * JPA を通じて永続化される。
 * </p>
 */
@Getter
@Setter
@Entity
@Table(name = "todos")
public class TodoEntity {

    /** DB が採番する内部ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "internal_id")
    private Integer internalId;

    /** 版数 */
    @Column(name = "version_number")
    private Integer versionNumber;

    /** 公開ID（UUID形式） */
    @Column(name = "public_id")
    private String publicId;

    /** タイトル（必須） */
    @Column(name = "title", nullable = false, length = 255)
    private String title;

    /** 詳細（任意） */
    @Column(name = "detail")
    private String detail;

    /** 期限日（必須） */
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    /** 完了状態フラグ */
    @Column(name = "completed_flag", nullable = false)
    private boolean completedFlag;

    /** 削除状態フラグ（論理削除） */
    @Column(name = "deleted_flag", nullable = false)
    private boolean deletedFlag;

    /** 作成日時（自動記録） */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 更新日時（自動更新） */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
