# Todo App (DDD + Spring Boot)

## 1. プロジェクト概要

このアプリケーションは、DDD（ドメイン駆動設計）のレイヤードアーキテクチャを採用した Todo 管理アプリです。  
Todo の更新履歴を保持するため、`todo` と `todo_history` の 2 つのテーブルを持ち、生成および更新のたびに履歴が自動生成されます。

学習目的として DDD の実践的な構造を体験することを目的としています。

## 2. 技術スタック

- Java 21
- Spring Boot 3.5.9
- Spring Data JPA
- PostgreSQL
- Lombok
- Thymeleaf

## 3. ディレクトリ構造（DDD 準拠）

src/main/java/com/example/todoapp/
├── application/ # ユースケース層
├── domain/ # ドメイン層（Entity, ValueObject, Repository Interface）
├── infrastructure/ # 永続化層（JPA Entity, Repository実装）
└── presentation/ # Web層（Controller）

## 4. テーブル構造

### todo テーブル（集約ルート）

| カラム名       | 型        | 説明                         |
| -------------- | --------- | ---------------------------- |
| internal_id    | int       | 主キー（DB による自動採番）  |
| version_number | int       | 最新の履歴番号               |
| public_id      | varchar   | 外部公開ID（アプリ側で生成） |
| title          | varchar   | タイトル                     |
| detail         | text      | 詳細                         |
| due_date       | date      | 期限日                       |
| completed_flag | boolean   | 完了フラグ                   |
| deleted_flag   | boolean   | 削除フラグ                   |
| created_at     | timestamp | 作成日時                     |
| updated_at     | timestamp | 更新日時                     |

### todo_history テーブル（履歴テーブル）

| カラム名       | 型        | 説明                   |
| -------------- | --------- | ---------------------- |
| internal_id    | int       | Todo の ID（外部キー） |
| version_number | int       | 履歴番号（複合主キー） |
| public_id      | varchar   | 外部公開ID             |
| title          | varchar   | タイトル               |
| detail         | text      | 詳細                   |
| due_date       | date      | 期限日                 |
| completed_flag | boolean   | 完了フラグ             |
| deleted_flag   | boolean   | 削除フラグ             |
| created_at     | timestamp | 作成日時               |
| updated_at     | timestamp | 更新日時               |

## 5. データの流れ

1. Todo を新規作成すると internal_id が DB により採番される
2. 更新時、version_number が +1 される
3. 生成および更新時、最新の状態が todo_history に保存される
4. todo テーブルには常に「最新の状態」だけが残る

## 6. 起動方法

./mvnw spring-boot:run
