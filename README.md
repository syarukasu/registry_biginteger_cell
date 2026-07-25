# Registry BigInteger Cell

Operator-only AE2 integration cells for Minecraft 1.20.1 Forge.

## Cells

- `registry_biginteger_cell:registry_biginteger_test_cell`
  contains every registered item, fluid, Mekanism gas, infusion type,
  pigment, and slurry.
- `registry_biginteger_cell:configured_biginteger_test_cell`
  contains only the item and fluid IDs listed in
  `config/registry_biginteger_cell-common.toml`.

Every selected AE key is initialized with the exact amount `10^64`.
The cells use ExtendedAE Plus BigInteger storage, while AE2-compatible views
remain bounded by the integrations installed in the pack.

The configured cell defaults to `minecraft:cobblestone`.

Pack or server operators can replace that default in the common config. For
example, the syarukasutime integration server uses:

- `kubejs:aco_crafting_island_seed_a`
- `kubejs:aco_crafting_island_seed_b`

Both cells have no survival recipe. Use `/give` for integration testing.
Sneak-right-click a cell to rebuild it from the current registries or config.

Legacy item IDs from `registry_long_test_cell` are remapped automatically.

## Build

Java 17 is required. The build expects the matching AE2, ExtendedAE Plus,
Applied Mekanistics, and Mekanism JARs in a local mods directory.

PowerShell:

```powershell
.\gradlew.bat build -PlocalModsDir="C:\path\to\minecraft\mods"
```

Alternatively, set the `LOCAL_MODS_DIR` environment variable and run
`./gradlew build`.

The release JAR is written to `build/libs/`.

## License

Registry BigInteger Cell is available under the MIT License. See `LICENSE`.
