# Changelog

All notable changes to this project are documented here. The format is based on
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/), and this project
adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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

[Unreleased]: https://github.com/abdulwahed-s/prayer-time-plus-kotlin/compare/v0.1.0...HEAD
[0.1.0]: https://github.com/abdulwahed-s/prayer-time-plus-kotlin/releases/tag/v0.1.0
