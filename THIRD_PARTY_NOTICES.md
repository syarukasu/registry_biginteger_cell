# Third-Party Notices

## ExtendedAE Plus

- Project: [ExtendedAE Plus](https://www.curseforge.com/minecraft/mc-mods/extendedae-plus)
- Source: [GaLicn/ExtendedAE_Plus](https://github.com/GaLicn/ExtendedAE_Plus)
- Runtime version range: `1.5.5` through, but not including, `1.6.0`
- License: GNU Lesser General Public License v3.0 (`LGPL-3.0-only`)

Registry BigInteger Cell is a separately distributed runtime-dependent
application built against ExtendedAE Plus. `RegistryBigIntegerTestCellItem`
and `ConfiguredBigIntegerTestCellItem` subclass
`InfinityBigIntegerCellItem` and use the ExtendedAE Plus BigInteger storage
API at runtime. Its two item model JSON files also use
`extendedae_plus:item/infinity_biginteger_cell` as their parent model, so the
matching texture is resolved from the separately installed ExtendedAE Plus JAR.

No ExtendedAE Plus Java classes, source files, model JSON files, or texture
PNG files are copied into this repository or its release JAR. The model-parent
reference does not bundle the upstream asset. Users must obtain ExtendedAE Plus
separately; the mandatory dependency and supported version range are declared
in `META-INF/mods.toml`.

Registry BigInteger Cell's original code and assets remain available under the
MIT License in `LICENSE`. That license does not apply to ExtendedAE Plus. A
verbatim copy of the LGPLv3 license is included in the built JAR at
`META-INF/licenses/ExtendedAE-Plus-LGPL-3.0.txt`.
