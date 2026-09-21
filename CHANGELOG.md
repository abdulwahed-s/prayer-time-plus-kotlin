# Changelog

All notable changes to this project are documented here. The format is based on
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project
adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.3.0] - 2026-09-21

### Added

- Working angle-based Maghrib support through the existing
  `maghribIsInterval`/`maghribValue` API, with safe Sunset fallback for
  unavailable or non-chronological events.
- Cross-package Custom preset parity: Fajr 18°, Maghrib at Sunset, Isha 17°,
  with stable key `custom`.

### Fixed

- Interval Isha is calculated from the final Maghrib, including an angle-based
  Maghrib; method and user offsets remain applied exactly once.
- Automatic national method resolution now maps Iraq (`IQ`) to `iraq` instead
  of `egypt`, and Austria (`AT`) to `austria` instead of `tunisia`.
- Removed the unused continent fallback from the generated resolution model;
  country misses now fall back directly to Muslim World League as documented.

## [0.2.0] - 2026-07-09

### Changed

- Added `HighLatitudeRule.AUTOMATIC` as the explicit default high-latitude
  behavior, matching the Swift and Dart packages.
- Changed `CalculationParameters.highLatitudeRule` to default to
  `HighLatitudeRule.AUTOMATIC` instead of relying on `null`.
- Kept `HighLatitudeRule.NONE` as the explicit unadjusted mode where Fajr and
  Isha may remain `null`.

## [0.1.0] - 2026-07-06

### Added

- Pure-Kotlin, dependency-free prayer-time engine (Julian day, sun position,
  solar transit, time-at-depression-angle, Asr shadow rule) matching the
  reference algorithm to the minute.
- `PrayerTimes` entry point returning `OffsetDateTime` values (Fajr, Sunrise,
  Dhuhr, Asr, Sunset, Maghrib, Isha), with `today(...)`, `timeForPrayer`,
  `currentPrayer`, and `nextPrayer`.
- `SunnahTimes` for the middle and last third of the night.
- `CalculationMethod` presets covering the standard international and regional
  methods, plus fully custom `CalculationParameters`.
- `Madhab`, `HighLatitudeRule` (with an automatic seventh-of-the-night
  fallback), per-prayer `PrayerAdjustments`, and the Umm al-Qura Ramadan rule.
- `AutoMethod` country-to-method resolution from bundled data.
- Runnable example CLI (`./gradlew run`).

[Unreleased]: https://github.com/abdulwahed-s/prayer-time-plus-kotlin/compare/v0.3.0...HEAD
[0.3.0]: https://github.com/abdulwahed-s/prayer-time-plus-kotlin/compare/v0.2.0...v0.3.0
[0.2.0]: https://github.com/abdulwahed-s/prayer-time-plus-kotlin/compare/v0.1.0...v0.2.0
[0.1.0]: https://github.com/abdulwahed-s/prayer-time-plus-kotlin/releases/tag/v0.1.0
