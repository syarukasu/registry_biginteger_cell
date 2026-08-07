# Registry BigInteger Cell codebase map

> **Navigation only.** このMapはCodex・LLM・reviewerの探索量を減らすためのindexです。仕様判断はREADME、source、tests、third-party noticesを使用します。

## 使い方

1. [`../AGENTS.md`](../AGENTS.md)を読む。
2. 下のTask routeを1つ選ぶ。
3. 対象classと対応testだけを開く。
4. compile/test結果が依存API差分を示した場合だけ外部JARへscopeを広げる。

## 固定座標

```text
Minecraft        1.20.1
Forge            47.x line
Java             17
ExtendedAE Plus  1.5.5 runtime
ACO              1.5.4+ / tested 1.5.5
Exact amount     10^64 per key
```

## Task router

| Route | Task | Read first | Source scope | Verification scope |
| --- | --- | --- | --- | --- |
| `B1` | BigInteger inventory、per-key/shared total、insert/extract limit | READMEのCells、対応tests | `LimitedBigIntegerCellInventory`, limits/contents classes | limits/arithmetic tests、実cell test |
| `R1` | Registry-wide key collection、Mekanism/Applied Mekanistics types | READMEのregistry cell説明 | `RegistryTestCellContents`, item/handler/guard | type guard test、実modpack load |
| `C1` | Configured cell IDs、config parse、default cobblestone | READMEのconfigured cell説明 | config、configured item/contents | config/invalid ID test |
| `U1` | Tooltip/display scientific notation | READMEのdisplay説明 | formatter classes、item tooltip caller | formatter test、client tooltip |
| `H1` | Cell handler/AE2/ExtendedAE Plus integration | READMEのdependency/license boundary | handler、cell items、mods.toml | build with exact dependencies、game load |
| `L1` | License、notice、external model/texture boundary | README License、`THIRD_PARTY_NOTICES.md` | notices/resources metadataだけ | JAR contents review |
| `V1` | Build、local mods dir、release | README Build、`build.gradle` | build files、workflow、tests | `clean test build` |

## Class map

| Responsibility | Path |
| --- | --- |
| Mod entrypoint/registration | `src/main/java/com/syaru/registrybigintegercell/RegistryBigIntegerCell.java` |
| Common config | `src/main/java/com/syaru/registrybigintegercell/RegistryBigIntegerCellConfig.java` |
| Registry cell item | `src/main/java/com/syaru/registrybigintegercell/RegistryBigIntegerTestCellItem.java` |
| Configured cell item | `src/main/java/com/syaru/registrybigintegercell/ConfiguredBigIntegerTestCellItem.java` |
| Registry contents | `src/main/java/com/syaru/registrybigintegercell/RegistryTestCellContents.java` |
| Configured contents | `src/main/java/com/syaru/registrybigintegercell/ConfiguredTestCellContents.java` |
| BigInteger inventory facade | `src/main/java/com/syaru/registrybigintegercell/LimitedBigIntegerCellInventory.java` |
| Cell handler | `src/main/java/com/syaru/registrybigintegercell/RegistryBigIntegerCellHandler.java` |
| Handler type guard | `src/main/java/com/syaru/registrybigintegercell/CellHandlerTypeGuard.java` |
| Limits | `src/main/java/com/syaru/registrybigintegercell/RegistryBigIntegerCellLimits.java` |
| Display formatter | `src/main/java/com/syaru/registrybigintegercell/BigIntegerDisplayFormatter.java` |
| Tooltip formatter | `src/main/java/com/syaru/registrybigintegercell/BigIntegerTooltipFormatter.java` |
| Forge metadata | `src/main/resources/META-INF/mods.toml` |

## Test map

| Concern | Test |
| --- | --- |
| Scientific notation/display | `src/test/java/com/syaru/registrybigintegercell/BigIntegerDisplayFormatterTest.java` |
| Handler type safety | `src/test/java/com/syaru/registrybigintegercell/CellHandlerTypeGuardTest.java` |
| Limits/overflow boundaries | `src/test/java/com/syaru/registrybigintegercell/RegistryBigIntegerCellLimitsTest.java` |

## Dependency/build rule

```text
build.gradle / mods.toml
-> exact local dependency JARs
-> source compile
-> unit tests
-> built JAR notice/license contents
-> actual Forge + AE2 + ExtendedAE Plus + ACO smoke
```

外部dependencyのsource全体を読む前に、compile errorのowner/member/descriptorを特定する。

## 最小検証コマンド

```text
./gradlew test --no-daemon
./gradlew clean test build --no-daemon -PlocalModsDir=<mods-directory>
```

## 省トークン用prompt

```text
AGENTS.mdとdocs/CODEBASE_MAP.mdの<Route ID>だけを基準に作業する。
Task: <作業内容>
対象classと対応test以外を最初に読まない。
依存JARへ広げる場合はcompile/runtime errorのsymbolを根拠として示す。
```
