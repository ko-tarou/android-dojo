# 概要

「Google Gemini」のようなAIチャットアプリの開発に取り組みましょう。

https://play.google.com/store/apps/details?id=com.google.android.apps.bard&hl=ja

## 機能要件

各画面の機能要件は、下記の通りです。

### メッセージ入力画面

必須要件
- メッセージを入力して、サーバーに送信ができる
- AIのメッセージを出力できる
- 画像の送信ができる

任意要件
- 新規の会話を作成できる
- メッセージ履歴をローカルに保存できる (オフライン対応)
- 提案されたソースコードをフォーマットして表示できる
- メッセージを長押しでコピーできる
- AIのメッセージを外部アプリに共有する
- 音声入力できる
- リッチなアニメーション
- など

### メッセージ一覧画面 (任意)


任意要件
- 過去の会話一覧が表示できる
- 過去の会話履歴が表示できる

### 設定画面

必須要件
- Gemini API Key の保存

任意要件
- ダークテーマ / ライトテーマの切り替え
- メッセージ履歴の削除

- など

## 非機能要件

各画面の非機能要件は、下記の通りです。
できるだけ意識して開発してみましょう。

- アプリがフリーズ・クラッシュしない etc (可用性)
- 画面サイズの異なるデバイスでも動作する etc (互換性)
- DI、マルチモジュール化、UnitTest、UI Test etc (保守性)
- アニメーション、ダークモード、文字/表示文字サイズ変更、タップ範囲 etc (アクセシビリティ)

## その他
- Jetpack Compose を使った UI を作成してください。
- デザインの指定はありません。
- クライアントのモニタリング環境 (Firebase Crashlytics / Performance など) は課題の対象外とします。
- 難読化の対応は必須ではありません。
- パフォーマンスやクラッシュのモニタリング環境の構築は必須ではありません。
- APIは、Google 社が提供する gemini API を利用してください。API リファレンスは[こちら](https://ai.google.dev/api?hl=ja&lang=android)。
- インターンの期間中、API 総利用回数に制限があります。短時間での大量リクエストは避けてください。
- 不正利用防止の為、token は外部には公開しないように注意してください。
- また、インターンや社内で知り得た情報を gemini に投げることは無いように注意してください。

# 開発環境
https://developer.android.com/studio?hl=ja

- 安定版のAndroid Studio
    - Preview版も利用可だが、利用の目的を明確にすること
- これらのバージョン指定は特にないが、古すぎるバージョンは特別な理由がない限り使用は控えること
    - AndroidGradlePlugin, Kotlin
- サポートデバイス
  - 指定はありません。私物のデバイスやエミュレータを推奨します。

### 【開発する上での留意事項】

- Themeを適用すること
    - AppTheme, MaterialTheme, DayNigihtTheme など指定はないが、PrimaryColor や SurcfaceColor などを意識して使うこと
- 難読化の対応は必須ではない
- パフォーマンスやクラッシュのモニタリング環境の構築は必須ではない

# API キー

下記ページを参考に、自身の Google アカウントでログインして、無料で取得することが出来ます。

https://aistudio.google.com/app/u/3/apikey?hl=ja&pli=1

## API キーのテスト

実際にgemini APIを実行してみましょう。

YOUR_GEMINI_API_KEYは取得したAPIキーに置き換えてください。

```text
$ curl "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=YOUR_GEMINI_API_KEY" \
-H 'Content-Type: application/json' \
-X POST \
-d '{
  "contents": [{
    "parts":[{"text": "あなたのことを教えて"}]
    }]
   }'
{
  "candidates": [
    {
      "content": {
        "parts": [
          {
            "text": "私は、Google によってトレーニングされた、巨大言語モデルです。\n\nより具体的に言うと、私は大規模なテキストデータセットでトレーニングされた、ニューラルネットワークに基づいた機械学習モデルです。\n\n私の目的は、与えられたテキストを理解し、それに基づいてさまざまなタスクを実行することです。例えば、質問に答えたり、テキストを生成したり、言語を翻訳したりすることができます。\n\nまだ学習中であり、完璧ではありませんが、日々改善を続けています。 どんなお手伝いができるか、お気軽にお尋ねください。\n"
          }
        ],
        "role": "model"
      },
      "finishReason": "STOP",
      "avgLogprobs": -0.32266526473195928
    }
  ],
  "usageMetadata": {
    "promptTokenCount": 3,
    "candidatesTokenCount": 114,
    "totalTokenCount": 117,
    "promptTokensDetails": [
      {
        "modality": "TEXT",
        "tokenCount": 3
      }
    ],
    "candidatesTokensDetails": [
      {
        "modality": "TEXT",
        "tokenCount": 114
      }
    ]
  },
  "modelVersion": "gemini-2.0-flash"
}

```

