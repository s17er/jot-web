# README.md

このファイルは、このリポジトリでコードを扱う際の指針を提供します。

## プロジェクト概要

このプロジェクトは、求人サイトの管理機能を提供するWebアプリケーションです。
Webサイトはおおまかに以下の2種類に分かれています。

- 運営用: 求人サイトの運営会社が使用するためのWebアプリケーションです
- 顧客用: 求人を提供する顧客が管理するためのWebアプリケーションです

このプログラムはJava Strutsベースで構築されています

## ビルドとテストコマンド

### 利用可能なプロファイル

- **local**: ローカル開発環境用（デフォルト）
- **staging**: ステージング環境用
- **integration**: 統合環境用
- **production**: 本番環境用

各プロファイルは、それぞれの環境に適した設定ファイルを使用します。

## プロジェクトアーキテクチャ

これはStruts/Seasar2のJavaベースの求人サイト管理システムです。

### 技術スタック
- Java Struts、Seasar2、JSP、JDBCManager
- **Javaバージョン**: 1.8

### モジュール構造
```
app/
├── common/                     # 共通ライブラリ
├── g-manage-admin-logic/       # 管理者ビジネスロジック
├── g-manage-admin-pc/          # 管理者Webアプリケーション
├── g-shop-admin-logic/         # 顧客ビジネスロジック  
└── g-shop-admin-pc/            # 顧客Webアプリケーション
```

#### モジュール説明

- common: モデルやビジネスロジック、ユーリティリティ、カスタムタグなどの他モジュールで使用する共通部分を実装する
- g-manage-admin-logic: 管理者Webアプリケーションで使用する共通ロジックなどを実装する
- g-manage-admin-pc: 管理者Webアプリケーション (warファイルを生成)
- g-manage-shop-logic: 顧客用Webアプリケーションで使用する共通ロジックなどを実装する
- g-manage-shop-pc: 管理用Webアプリケーション (warファイルを生成)

#### モジュールの特徴

- **パッケージ構造**: `com.gourmetcaree` パッケージを使用
- **技術スタック**: Seasar2、Struts、JSP、S2JDBC
- **依存関係**: 
  - *-logicモジュールは common に依存
  - *-pcモジュールは common と対応する *-logic に依存

##### 依存関係の構造

```
g-manage-admin-pc → g-manage-admin-logic → common
g-manage-shop-pc → g-manage-shop-logic → common
```

