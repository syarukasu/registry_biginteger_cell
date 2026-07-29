# Changelog

## [Unreleased]

## [1.1.3] - 2026-07-29

### Fixed

- Centralized exact per-key remaining-capacity arithmetic and rejected negative
  stored amounts before they can become insert capacity.
- Added regression coverage for zero, limit, over-limit, and negative amounts.
- Moved the handler's concrete-cell conversion behind a tested type guard, so
  unrelated items are returned to AE2 without an unchecked cast.
- Added startup diagnostics for the loaded mod version, registered handler,
  and exact per-key limit.
