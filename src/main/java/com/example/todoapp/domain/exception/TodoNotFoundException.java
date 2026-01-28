package com.example.todoapp.domain.exception;

/** Todo が見つからない場合にスローされる例外。 */
public class TodoNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Todoが見つかりません";

    /** デフォルトメッセージで例外を生成する。 */
    public TodoNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * 指定されたメッセージで例外を生成する。
     *
     * @param message エラーメッセージ
     */
    public TodoNotFoundException(String message) {
        super(message != null ? message : DEFAULT_MESSAGE);
    }

    /**
     * 指定されたメッセージと原因で例外を生成する。
     *
     * @param message エラーメッセージ
     * @param cause 原因となった例外
     */
    public TodoNotFoundException(String message, Throwable cause) {
        super(message != null ? message : DEFAULT_MESSAGE, cause);
    }
}
