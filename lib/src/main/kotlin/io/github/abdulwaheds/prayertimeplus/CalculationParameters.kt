package io.github.abdulwaheds.prayertimeplus

/**
 * The complete, tunable input to the prayer-time engine.
 *
 * Most callers obtain a ready-made instance from a [CalculationMethod] preset
 * and customise it with [copy]:
 *
 * ```
 * val params = CalculationMethod.OMAN.parameters().copy(
 *     madhab = Madhab.HANAFI,
 *     highLatitudeRule = HighLatitudeRule.SEVENTH_OF_THE_NIGHT,
 *     adjustments = PrayerAdjustments(fajr = 2),
 * )
 * ```
 *
 * Fajr is always an angle. Maghrib is always derived from sunset — either at
 * sunset itself or [maghribValue] minutes after it when [maghribIsInterval] is
 * true. Isha is either an angle ([ishaValue] degrees, when [ishaIsInterval] is
 * false) or a fixed interval of [ishaValue] minutes after Maghrib.
 *
 * @property method the preset key this originated from, or `null` for a fully
 *   custom set. Two behaviours key off it: the horizon-dip elevation methods and
 *   the Umm al-Qura Ramadan rule (see [isRamadan]).
 * @property fajrAngle sun depression below the horizon for Fajr, in degrees.
 * @property maghribIsInterval whether Maghrib is offset from sunset by
 *   [maghribValue] minutes (`true`) or taken at sunset (`false`).
 * @property maghribValue minutes after sunset for Maghrib when
 *   [maghribIsInterval] is true; otherwise unused.
 * @property ishaIsInterval whether Isha is a fixed interval after Maghrib
 *   (`true`) or an angle (`false`).
 * @property ishaValue minutes after Maghrib when [ishaIsInterval] is true,
 *   otherwise the Isha depression angle in degrees.
 * @property methodAdjustments per-prayer minute offsets that define the preset.
 * @property adjustments the caller's own per-prayer minute offsets, added on top
 *   of [methodAdjustments].
 * @property madhab the Asr shadow rule.
 * @property highLatitudeRule the high-latitude fallback.
 *   [HighLatitudeRule.AUTOMATIC] applies no fallback first, then retries once
 *   with [HighLatitudeRule.SEVENTH_OF_THE_NIGHT] if Fajr or Isha come out
 *   degenerate. Use [HighLatitudeRule.NONE] to never apply a fallback.
 * @property isRamadan whether the date falls in Ramadan; combined with the
 *   `makkah` method in Saudi Arabia this adds 30 minutes to Isha.
 */
public data class CalculationParameters(
    val method: String? = null,
    val fajrAngle: Double,
    val maghribIsInterval: Boolean = false,
    val maghribValue: Double = 0.0,
    val ishaIsInterval: Boolean = false,
    val ishaValue: Double,
    val methodAdjustments: PrayerAdjustments = PrayerAdjustments(),
    val adjustments: PrayerAdjustments = PrayerAdjustments(),
    val madhab: Madhab = Madhab.SHAFI,
    val highLatitudeRule: HighLatitudeRule = HighLatitudeRule.AUTOMATIC,
    val isRamadan: Boolean = false,
)
