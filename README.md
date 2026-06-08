# 空港スポット管理システム

## 概要
航空機が駐機するスポットの使用状況を管理するWebアプリケーションです。
前職で実際に使用していたシステムを参考に、ポートフォリオとして開発しました。

## 機能一覧
- ログイン・ログアウト（Spring Security）
- スポット管理画面（時間軸で便バーを表示）
- 便の新規登録・編集・削除
- 型式サイズチェック（スポットの最大ウィングスパンを超える場合は赤表示）
- 時間重複チェック（同一スポットで時間が重複する場合はエラー）
- 30分インターバルチェック（便間隔が30分未満の場合は黄色表示）

## 技術スタック
| 区分 | 技術 |
|------|------|
| 言語 | Java 21 |
| フレームワーク | Spring Boot 4.0.5 |
| フロントエンド | Thymeleaf / HTML / CSS / JavaScript |
| DB（開発） | H2 Database |
| DB（本番） | PostgreSQL（AWS RDS） |
| ビルドツール | Gradle |
| 認証 | Spring Security |
| インフラ | AWS EC2 |
| バージョン管理 | Git / GitHub |

## 起動方法（ローカル）
```bash
git clone https://github.com/msuzuki1128business-hash/spot-management.git
cd spot-management
./gradlew bootRun
```
ブラウザで http://localhost:8080 にアクセス

### テストアカウント
| ユーザーID | パスワード |
|------|------|
| admin | admin123 |

## 設計ドキュメント
- [要件定義書](https://app.notion.com/p/359f77abdfad803ab6ece652b199d6a9?source=copy_link)
- [基本設計書](https://app.notion.com/p/359f77abdfad8025b50bf15cd689f516?source=copy_link)
- [詳細設計書](https://app.notion.com/p/359f77abdfad8074a2b0dd6b97a603cf?source=copy_link)

## こだわりポイント
- 前職（航空管制・運航情報官）での実務知識を活かした業務ロジックの実装
- 型式ウィングスパンによるサイズチェックを警告（赤表示）として実装し、特例を考慮した設計
- 30分インターバルチェックを警告（黄色表示）として実装し、実務の運用実態に合わせた設計
- 時間軸ベースの便バー表示をJavaScriptで実装