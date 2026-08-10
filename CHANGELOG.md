# Changelog

## [Unreleased]

## [1.1.5] - 2026-08-10

### Changed

- Expanded the accepted AE2 range to 15.4.10 through 15.x.
- Added build verification for upstream AE2 15.4.10 and AE2-UELM
  15.5.0-uelm.
- Kept all BigInteger storage and capacity accounting behavior unchanged.

## [1.1.4] - 2026-08-02

### Fixed

- Replaced the invalid ExtendedAE Plus dependency range
  `[1.5.5,1.5.5]` with the valid exact-version range `[1.5.5]`.
- Restored Forge 47.4.20 mod discovery; gameplay and cell storage behavior
  are unchanged.

## [1.1.3] - 2026-07-29

### Fixed

- Centralized exact per-key remaining-capacity arithmetic and rejected negative
  stored amounts before they can become insert capacity.
- Added regression coverage for zero, limit, over-limit, and negative amounts.
- Moved the handler's concrete-cell conversion behind a tested type guard, so
  unrelated items are returned to AE2 without an unchecked cast.
- Added startup diagnostics for the loaded mod version, registered handler,
  and exact per-key limit.
