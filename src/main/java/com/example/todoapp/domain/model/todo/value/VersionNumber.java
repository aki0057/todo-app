package com.example.todoapp.domain.model.todo.value;

import jakarta.persistence.Embeddable;

/**
 * Todo の版数を表す ValueObject。
 * <p>
 * 更新の度に インクリメントされ、楽観的ロックの制御に使用される。
 * 最小値は 1。
 * </p>
 */
@Embeddable
public record VersionNumber(Integer value) {

    private static final int INITIAL = 1;

    public VersionNumber {
        if (value == null || value < 1) {
            throw new IllegalArgumentException("版数は1以上の整数でなければなりません");
        }
    }

    /**
     * 初期版数を生成する。
     * <p>
     * 新規 Todo 作成時に使用される。初期値は 1。
     * </p>
     *
     * @return 初期版数（1）
     */
    public static VersionNumber initial() {
        return new VersionNumber(INITIAL);
    }

    /**
     * 次の版数を取得する。
     * <p>
     * 現在の版数に 1 を加えた新しい版数を返す。
     * </p>
     *
     * @return インクリメント後の新しい版数
     */
    public VersionNumber next() {
        return new VersionNumber(value + 1);
    }
}
