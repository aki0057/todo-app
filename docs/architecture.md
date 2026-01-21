# Architecture Overview

## 全体構造

このアプリは DDD（ドメイン駆動設計）をベースにした4層構造で作られている。

- Presentation（Controller，Form）
  → HTTP リクエストを受け取り、DTO を扱う。
  ビジネスロジックは書かず、Application に渡すだけとする。

- Application（Service）
  → Domain で定義したメソッドを用いて、ユースケースを作成し、配置する。

- Domain（Todo，ValueObject）
  → ビジネスルールを表現する。
  他の層に依存しないようにする。

- Infrastructure（Entity，Mapper，RepositoryImpl）
  → Mapperにより、Domain と Entity の変換を行う。
  DBアクセスを行い、CRUD操作を実行する。

依存関係は以下の通り

```
Presentation → Application → Domain
Infrastructure → Domain
```

## DDD の採用範囲

本アプリケーションでは、軽量DDDの考え方をベースに、以下のような基本的なDDD要素を採用している。

- Entity（Todo など）
- Value Object（InternalId, PublicId）
- Repository（Interface とその実装）
- Domain Model（ビジネスルールの中心）
- Application Service（ユースケースの実行）
- Infrastructure（Entity, Mapper による永続化）

一方で、以下のような高度なDDD要素は採用していない。

- Bounded Context
- Domain Event
- Aggregate の厳密な定義
- CQRS
- Event Sourcing
- Anti-corruption Layer（ACL）

## 技術選定の理由

実務で使用経験があるため。
