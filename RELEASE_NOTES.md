# Registry BigInteger Cell 1.1.0

## English

- Adds an all-registry AE2 BigInteger cell for registered items, fluids, and
  Mekanism chemicals.
- Adds a config-driven AE2 BigInteger cell for selected item and fluid IDs.
- Stores and exposes at most exactly `10^64` units per AE key.
- Uses `minecraft:cobblestone` as the public configured-cell default.
- Rejects unconfigured keys in the configured cell.
- Remaps legacy `registry_long_test_cell` item IDs.
- Includes an independent Gradle Wrapper and MIT License.

## 日本語

- 登録済みアイテム、液体、Mekanism化学物質を収録する全レジストリセルを追加。
- 設定したアイテム・液体IDだけを収録する設定対象セルを追加。
- 各AEキーの在庫と再投入上限を厳密に`10^64`へ制限。
- 公開版の設定対象セルは`minecraft:cobblestone`を初期値として使用。
- 設定対象セルは未設定キーの投入を拒否。
- 旧`registry_long_test_cell`のアイテムIDを自動移行。
- 独立Gradle WrapperとMITライセンスを同梱。
