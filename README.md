# prayer-time-plus

A small, dependency-free **Kotlin** library for computing Islamic prayer times.
It reproduces a classic PrayTimes/USNO solar model to the minute, with a public
API modelled on the [Adhan](https://github.com/batoulapps/adhan-java) library and
extended with sunset-relative Maghrib, per-prayer offsets, an automatic
high-latitude fallback, and country-based "Auto" method resolution.

- **Pure Kotlin, zero runtime dependencies** — the JDK time API only; the caller
  supplies the UTC offset, so there is no time-zone database to ship.
- **Faithful** — a single, no-iteration pass with the documented seeds, angles,
  literals, and rounding.
- **Ergonomic** — immutable value types, preset methods, and `copy`-based
  customization; times are returned as `java.time.OffsetDateTime`.

## Install

Gradle (Kotlin DSL):

```kotlin
dependencies {
    implementation("io.github.abdulwahed-s:prayer-time-plus:0.1.0")
}
```

Maven:

```xml
<dependency>
  <groupId>io.github.abdulwahed-s</groupId>
  <artifactId>prayer-time-plus</artifactId>
  <version>0.1.0</version>
</dependency>
```

> The published coordinate uses the group `io.github.abdulwahed-s`; the Kotlin
> package is `io.github.abdulwaheds.prayertimeplus` (a hyphen is not a legal
> package identifier).

## Quick start

```kotlin
import io.github.abdulwaheds.prayertimeplus.*
import java.time.ZoneOffset

val times = PrayerTimes(
    coordinates = Coordinates(latitude = 24.3486, longitude = 56.6953, altitude = 5.0),
    dateComponents = DateComponents(2026, 6, 28),
    calculationParameters = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters(),
    utcOffset = ZoneOffset.ofHours(4),
    countryCode = "OM",
)

println(times.fajr)     // 2026-06-28T03:59+04:00
println(times.maghrib)  // 2026-06-28T19:05+04:00
```

Each time is an `OffsetDateTime?`; it is `null` when the model cannot define it
(for example Fajr at a high latitude in summer under `HighLatitudeRule.NONE`).

## Initialization

| Input | Type | Notes |
|---|---|---|
| `coordinates` | `Coordinates` | latitude (N+), longitude (E+), altitude in metres |
| `dateComponents` | `DateComponents` | civil year/month/day only |
| `calculationParameters` | `CalculationParameters` | angles, offsets, madhab, rules |
| `utcOffset` | `java.time.ZoneOffset` | includes any DST; the library has no time-zone DB |
| `countryCode` | `String` | ISO-3166 alpha-2; drives the elevation and Ramadan rules |
| `cityName` | `String` | free-form label, not used in the calculation |

Use `PrayerTimes.today(...)` to compute for the current local date at an offset.

## Calculation methods

`CalculationMethod` provides presets; `parameters()` returns a fresh, customizable
`CalculationParameters`.

| Method | Fajr | Isha |
|---|---|---|
| `MUSLIM_WORLD_LEAGUE` | 18° | 17° |
| `EGYPTIAN` | 19.5° | 17.5° |
| `KARACHI` | 18° | 18° |
| `UMM_AL_QURA` | 18.5° | 90 min after Maghrib |
| `NORTH_AMERICA` | 15° | 15° |
| `DUBAI` | 18° | 17° (Muslim World League fallback) |

Around fifty regional and city methods (Turkey, Oman, Emirates, UOIF, Moscow,
Indonesia, …) are also available. See **[METHODS.md](METHODS.md)** for the full
list with angles and per-prayer offsets.

## Madhab

The Asr shadow rule:

| `Madhab` | Shadow factor | Schools |
|---|---|---|
| `SHAFI` | 1 | Shafiʿi / Maliki / Hanbali (standard) |
| `HANAFI` | 2 | Hanafi (later Asr) |

```kotlin
val params = CalculationMethod.KARACHI.parameters().copy(madhab = Madhab.HANAFI)
```

## High-latitude rule

When the sun never reaches the required depression, Fajr and Isha are estimated
from a fraction of the night:

| `HighLatitudeRule` | Behaviour |
|---|---|
| `NONE` | no adjustment (times may be `null`) |
| `MIDDLE_OF_THE_NIGHT` | half of the night |
| `SEVENTH_OF_THE_NIGHT` | one seventh of the night |
| `TWILIGHT_ANGLE` | a fraction proportional to the twilight angle |

Leaving `highLatitudeRule = null` (the default) applies no adjustment, but
retries once with `SEVENTH_OF_THE_NIGHT` if Fajr or Isha come out degenerate.

## Auto method resolution

```kotlin
val method = AutoMethod.forCountry("TR") // CalculationMethod.TURKEY
val params = method.parameters()
```

## Sunnah times

```kotlin
val sunnah = SunnahTimes(times)
println(sunnah.middleOfTheNight)
println(sunnah.lastThirdOfTheNight)
```

## Current and next prayer

```kotlin
import java.time.OffsetDateTime

val now = OffsetDateTime.now(times.utcOffset)
val current = times.currentPrayer(now) // e.g. Prayer.DHUHR
val next = times.nextPrayer(now)       // e.g. Prayer.ASR
```

## Custom parameters and offsets

```kotlin
val params = CalculationMethod.OMAN.parameters().copy(
    madhab = Madhab.HANAFI,
    highLatitudeRule = HighLatitudeRule.SEVENTH_OF_THE_NIGHT,
    adjustments = PrayerAdjustments(fajr = 2, isha = -3),
    isRamadan = true,
)
```

Fully custom parameters can also be built directly with `CalculationParameters`.

## Example CLI

```
./gradlew run
```

Prints today's prayer times for a sample location using the Auto-resolved method.

## Building

```
./gradlew build          # compile, test, ktlint, detekt
./gradlew test
./gradlew dokkaGenerate   # API documentation (HTML)
./gradlew publishToMavenLocal
```

## License

Licensed under the [Apache License, Version 2.0](LICENSE).
