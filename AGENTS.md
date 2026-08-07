# Registry BigInteger Cell agent entrypoint

このファイルはCodex・LLM・自動レビューが、小規模リポジトリでも毎回全source・全resource・全依存MODを読み込まずに作業範囲を決めるための入口です。

## 最小読込手順

1. 最初に本書と [`docs/CODEBASE_MAP.md`](docs/CODEBASE_MAP.md) だけを読む。
2. MapのTask routeを1つ選び、対象classと対応testだけを開く。
3. local mods directoryや依存JARの全内容を初手で解析しない。
4. compile error、test failure、実API差分が示した場合だけ依存側へ範囲を広げる。
5. 全resources、release notes、third-party license全文を機能修正の開始条件にしない。

## 固定契約

```text
Minecraft                 1.20.1
Loader                    Forge
Runtime Java              17
Primary dependency        ExtendedAE Plus 1.5.5
AE2 Crafting Optimizer    1.5.4+ in 1.5.x; tested with 1.5.5
Optional key integrations Applied Mekanistics / Mekanism
Exact initial amount      10^64 per selected AE key
License                   MIT for original code/assets
```

Cellのexact BigInteger amount、per-key limit、shared-total limitは切り捨てません。AE2-compatible viewがboundedであっても、内部exact amountを`long`へ暗黙変換しません。

Registry cellは登録済みitem/fluid/Mekanism chemical keysを列挙し、configured cellはconfigに指定されたitem/fluid IDsだけを使用します。invalid ID、unsupported key type、wrong cell handlerを安全に拒否します。

ExtendedAE PlusのJava code、model、textureをこのrepository/JARへコピーしません。LGPL noticeとexternal asset/model dependency boundaryを維持します。

## 編集規則

- arithmetic/limit変更は対応unit testを先に確認する。
- cell handler/item/config変更はruntime dependencyとmods.tomlも確認する。
- license/distribution変更はREADME、THIRD_PARTY_NOTICES、embedded noticeを同じ変更で更新する。
- entrypoint、主要class、test位置が変わる場合は `docs/CODEBASE_MAP.md` を更新する。

## 検証順

```text
対象test class
-> ./gradlew test --no-daemon
-> ./gradlew clean test build --no-daemon -PlocalModsDir=<mods>
-> 必要な場合だけForge実環境でcell insert/extract/tooltip/save確認
```

unit testやbuildだけの結果を実AE2/ExtendedAE Plus環境の動作確認済みとして扱いません。
