package io.github.abdulwaheds.prayertimeplus

/**
 * Per-prayer time shifts in **whole minutes**, added to the computed times.
 *
 * The same shape is used for two distinct roles on [CalculationParameters]: the
 * fixed offsets baked into a calculation method, and the caller's own manual
 * tuning. The two are summed before being applied, so they compose with [plus].
 *
 * @property fajr minutes added to Fajr.
 * @property sunrise minutes added to Sunrise.
 * @property dhuhr minutes added to Dhuhr.
 * @property asr minutes added to Asr.
 * @property maghrib minutes added to Maghrib.
 * @property isha minutes added to Isha.
 */
public data class PrayerAdjustments(
    val fajr: Int = 0,
    val sunrise: Int = 0,
    val dhuhr: Int = 0,
    val asr: Int = 0,
    val maghrib: Int = 0,
    val isha: Int = 0,
) {
    /** Returns the element-wise sum of this and [other]. */
    public operator fun plus(other: PrayerAdjustments): PrayerAdjustments =
        PrayerAdjustments(
            fajr = fajr + other.fajr,
            sunrise = sunrise + other.sunrise,
            dhuhr = dhuhr + other.dhuhr,
            asr = asr + other.asr,
            maghrib = maghrib + other.maghrib,
            isha = isha + other.isha,
        )
}
