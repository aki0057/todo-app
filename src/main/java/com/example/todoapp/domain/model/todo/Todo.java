package com.example.todoapp.domain.model.todo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.todoapp.domain.model.todo.value.DueDate;
import com.example.todoapp.domain.model.todo.value.InternalId;
import com.example.todoapp.domain.model.todo.value.PublicId;
import com.example.todoapp.domain.model.todo.value.VersionNumber;

import lombok.Getter;

/**
 * Todo を表すドメインエンティティ。
 * <p>
 * 以下の情報を保持する：
 * <ul>
 *     <li>内部ID（DB 採番）</li>
 *     <li>公開ID（UUID）</li>
 *     <li>タイトル・詳細</li>
 *     <li>期限日</li>
 *     <li>完了状態・削除状態</li>
 *     <li>版数</li>
 *     <li>作成日時・更新日時</li>
 * </ul>
 * </p>
 */
@Getter
public class Todo {

    // ------------------------------------------------------------
    // フィールド
    // ------------------------------------------------------------

    /** DB が採番する内部ID（永続化前は null） */
    private final InternalId internalId;

    /** 外部公開用の UUID */
    private final PublicId publicId;

    /** 版数 */
    private VersionNumber versionNumber;

    /** タイトル（必須） */
    private String title;

    /** 詳細（任意） */
    private String detail;

    /** 完了状態 */
    private boolean completed;

    /** 削除状態（論理削除） */
    private boolean deleted;

    /** 期限日（ValueObject） */
    private DueDate dueDate;

    /** 作成日時 */
    private final LocalDateTime createdAt;

    /** 更新日時 */
    private LocalDateTime updatedAt;

    // ------------------------------------------------------------
    // コンストラクタ
    // ------------------------------------------------------------

    /**
     * 永続化層から復元するためのコンストラクタ。
     */
    public Todo(
            InternalId internalId,
            PublicId publicId,
            VersionNumber versionNumber,
            String title,
            String detail,
            boolean completed,
            boolean deleted,
            DueDate dueDate,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.internalId = internalId;
        this.publicId = publicId;
        this.versionNumber = versionNumber;
        this.title = title;
        this.detail = detail;
        this.completed = completed;
        this.deleted = deleted;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ------------------------------------------------------------
    // ファクトリメソッド
    // ------------------------------------------------------------

    /**
     * Todo を新規作成する。
     * <p>
     * タイトルと期限日は必須。期限日は本日以降である必要がある。
     * タイトルは100文字以内、詳細は1000文字以内である必要がある。
     * </p>
     *
     * @param title   タイトル（必須、100文字以内）
     * @param detail  詳細（任意、1000文字以内）
     * @param dueDate 期限日（必須、本日以降）
     * @return 新しく生成された Todo
     * @throws IllegalArgumentException タイトルまたは詳細が不正な場合
     */
    public static Todo create(String title, String detail, LocalDate dueDate) {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("タイトルは必須です");
        }
        if (title.length() > 100) {
            throw new IllegalArgumentException("タイトルは100文字以内である必要があります");
        }
        if (detail != null && detail.length() > 1000) {
            throw new IllegalArgumentException("詳細は1000文字以内である必要があります");
        }

        LocalDateTime now = LocalDateTime.now();

        return new Todo(
            null,            // internalId（DB が採番）
            PublicId.generate(),        // publicId（UUID）
            VersionNumber.initial(),    // versionNumber = 1
            title,
            detail,
            false,            // completed
            false,              // deleted
            new DueDate(dueDate),
            now,                        // createdAt
            now                         // updatedAt
        );
    }

    // ------------------------------------------------------------
    // 振る舞い
    // ------------------------------------------------------------

    /**
     * Todo の内容を更新する。
     * <p>
     * タイトルと期限日は必須。期限日は本日以降である必要がある。
     * タイトルは100文字以内、詳細は1000文字以内である必要がある。
     * 更新時に版数と更新日時を更新する。
     * </p>
     *
     * @param title   新しいタイトル（必須、100文字以内）
     * @param detail  新しい詳細（任意、1000文字以内）
     * @param dueDate 新しい期限日（必須、本日以降）
     * @throws IllegalArgumentException タイトルまたは詳細が不正な場合
     */
    public void update(String title, String detail, LocalDate dueDate) {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("タイトルは必須です");
        }
        if (title.length() > 100) {
            throw new IllegalArgumentException("タイトルは100文字以内である必要があります");
        }
        if (detail != null && detail.length() > 1000) {
            throw new IllegalArgumentException("詳細は1000文字以内である必要があります");
        }

        this.title = title;
        this.detail = detail;
        this.dueDate = new DueDate(dueDate);

        this.versionNumber = this.versionNumber.next();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Todo を完了状態にする。
     * <p>
     * すでに完了済みの場合は何も変更しない。
     * 完了状態へ遷移した場合のみ、版数と更新日時を更新する。
     * </p>
     */
    public void complete() {

        if (this.completed) {
            return;
        }

        this.completed = true;
        this.versionNumber = this.versionNumber.next();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Todo を削除状態にする（論理削除）。
     * <p>
     * すでに削除済みの場合は何も変更しない。
     * 削除状態へ遷移した場合のみ、版数と更新日時を更新する。
     * </p>
     */
    public void delete() {

        if (this.deleted) {
            return;
        }

        this.deleted = true;
        this.versionNumber = this.versionNumber.next();
        this.updatedAt = LocalDateTime.now();
    }
}
