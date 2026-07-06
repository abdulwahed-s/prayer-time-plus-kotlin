# Calculation methods

Every `CalculationMethod` produces a `CalculationParameters` describing four
things:

- **Fajr** — a sun-depression angle below the horizon.
- **Maghrib** — always derived from sunset: at sunset, or a fixed number of
  minutes after (or before) it.
- **Isha** — either a sun-depression angle, or a fixed interval measured from
  Maghrib.
- **Offsets** — whole-minute adjustments applied per prayer.

Angles and offsets are authority-specific. The exact values are returned by
`CalculationMethod.parameters()`; the tables below summarise them. A trailing
"(+N min)" / "(−N min)" on Isha is a minute offset applied after the angle or
interval.

## Standard methods

| Method | Fajr | Maghrib | Isha |
|---|---|---|---|
| `MUSLIM_WORLD_LEAGUE` | 18° | Sunset | 17° |
| `EGYPTIAN` | 19.5° | Sunset | 17.5° |
| `KARACHI` | 18° | Sunset | 18° |
| `UMM_AL_QURA` | 18.5° | Sunset | Maghrib + 90 min¹ |
| `NORTH_AMERICA` | 15° | Sunset | 15° |

¹ In Saudi Arabia during Ramadan (`isRamadan = true`, `countryCode = "SA"`),
Isha becomes Maghrib + 120 min.

## Regional methods

| Method | Fajr | Maghrib | Isha | Other offsets (min) |
|---|---|---|---|---|
| `ALGERIA` | 18° | Sunset + 3 min | 17° | — |
| `MOROCCO` | 19.09° | Sunset + 3 min | 17° | Sunrise −2, Dhuhr +5 |
| `AZROU` | 19.1° | Sunset + 1 min | 17° | Sunrise −1, Dhuhr +5 |
| `SUDAN` | 18.12° | Sunset − 4 min | 17.88° | Sunrise +3, Dhuhr +3 |
| `TUNISIA` | 18° | Sunset + 1 min | 18° (+1 min) | Fajr −1, Dhuhr +7 |
| `LIBYA` | 18.3° | Sunset + 4 min | 18.35° | Dhuhr +4 |
| `TURKEY` | 18° | Sunset + 5 min | 16.93° | Sunrise −6, Dhuhr +6, Asr +4 |
| `MALAYSIA` | 20° | Sunset | 18° | Dhuhr +1 |
| `MALAYSIA2` | 20° | Sunset + 1 min | 18.46° | Dhuhr +3, Asr +2 |
| `INDONESIA` | 20° | Sunset + 2 min | 18° (+2 min) | Fajr +2, Sunrise −2, Dhuhr +2, Asr +2 |
| `PALESTINE` | 20.11° | Sunset + 4 min | 17.9° | Sunrise −5 |
| `OMAN` | 18° | Sunset + 5 min | 18° (+1 min) | Dhuhr +5, Asr +5 |
| `OMAN_MUSCAT` | 17.74° | Sunset + 6 min | 18.229° | Sunrise +1, Dhuhr +6, Asr +6 |
| `EMIRATES` | 18.5° | Sunset + 2 min | 18.5° (−3 min) | Fajr +1, Sunrise −4, Dhuhr +2 |
| `QATAR` | 18° | Sunset + 2 min | Maghrib + 90 min | — |
| `KUWAIT` | 18° | Sunset | 17.5° | — |
| `JORDAN` | 18.12° | Sunset + 1 min | 17.975° | — |
| `SYRIA` | 19.5° | Sunset | 17.5° | — |
| `IRAQ` | 18° | Sunset + 2 min | 17° | Dhuhr +5, Asr +3 |
| `MALDIVES` | 19° | Sunset + 1 min | 19° (+1 min) | Sunrise −1, Dhuhr +4, Asr +1 |
| `KAZAKHSTAN` | 14.97° | Sunset | 14.96° | Dhuhr +5, Asr +5 |
| `TAJIKISTAN` | 18° | Sunset | 18° | — |
| `MOSCOW` | 16° | Sunset + 1 min | 15.1° (+2 min) | Dhuhr +1, Asr +1 |
| `SOUTH_KOREA` | 18° | Sunset | 18° (−6 min) | Fajr +1, Sunrise −1 |
| `UOIF` / `PARIS` / `TOULOUSE` / `LYON` | 12° | Sunset + 4 min | 12° (+5 min) | Fajr −5, Dhuhr +5 |
| `ORLEANS` | 15° | Sunset | 12.34° | Dhuhr +5 |
| `CZECH` | 12.04° | Sunset − 2 min | 12.04° | Dhuhr +5 |
| `SWITZERLAND` | 17.99° | Sunset − 4 min | Maghrib + 100 min | Fajr +1, Sunrise +4 |
| `FRIBOURG` | 18.01° | Sunset − 5 min | Maghrib + 100 min | Fajr +1, Sunrise +5 |
| `BELGIUM` | 18° | Sunset | 18° | applies horizon-dip elevation |

## City methods with standard angles

These carry authority-specific static tables in the original app (out of scope
here); computed from angles they equal a standard method:

- **18° / Sunset / 17°** (Muslim World League angles): `DUBAI`, `LONDON`,
  `BIRMINGHAM`, `BLACKBURN`, `AACHEN`, `MUNCHEN`, `POTSDAM`, `NURNBERG`,
  `AUSTRIA`, `LUXEMBOURG`, `OTHER`, `NONE`.
- **15° / Sunset / 15°** (ISNA angles): `MONTREAL`, `WINDSOR`, `CALGARY`,
  `MISSISSAUGA`, `ROTTERDAM`, `DORDRECHT`, `EINDHOVEN`.

## Notes

- Elevation (`0.833° + 0.0347·√altitude`) is applied to sunrise/sunset only for
  `IRAQ`, `MOROCCO`, `TUNISIA`, `JORDAN`, `ORLEANS`, `SUDAN`, `BELGIUM`,
  `KAZAKHSTAN`, and for locations in `PS`, `IL`, `CZ`, `CH`.
