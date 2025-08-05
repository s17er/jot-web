# -----------------------------------------------------------------------
# g-manage-admin-pc グルメキャリー 運営側管理画面(PC) 用のEclipseプロジェクトです。
# 2009/10/13 新規作成 ：安東（ウィズテクノロジー）
# -----------------------------------------------------------------------

◆ ディレクトリ構造

g-manage-admin-pc
  |
  +--src
      |
      +--integration (本番サーバ テスト環境用の設定ファイルなど)
      |
      +--main
      |   |
      |   +--java (Javaのソースコード)
      |   |
      |   +--resources (設定ファイルなど)
      |   |
      |   +--webapp (Web公開用のCSS、画像、JavaScript、JSPなど)
      |
      +--production (本番サーバ 本番環境の設定ファイルなど)
      |
      +--staging (社内ステージング環境用の設定ファイルなど)
      |
      +--test (テスト用ファイルなど)

◆ 画面とJSPの構造

[注意点]
・画面はヘッダ、ボディ、フッタの3つに分割
・レイアウト情報は各サブディレクトリ直下に配置、ボディはbodyディレクトリに配置
・JSPのエンコーディングはUTF-8
・common.jspにtaglibや共通に使用する変数などを宣言

[画面とJSPの対応]
※ JSPファイル g-mamage-admin-pc/src/main/webapp/WEB-INF/view

ここにJSPの一覧を記述します。


◆ その他ファイル
[メールテンプレート]
テンプレートエンジンはFreeMarker
/g-manage-admin-pc/src/main/resources/com/gourmetcaree/admin/pc/sys/mai 以下に配置



