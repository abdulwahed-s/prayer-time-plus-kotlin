package io.github.abdulwaheds.prayertimeplus

import io.github.abdulwaheds.prayertimeplus.engine.PrayerTimeCalculator
import io.github.abdulwaheds.prayertimeplus.engine.SolarTimes
import io.github.abdulwaheds.prayertimeplus.engine.roundedMinuteOfDay
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset

/**
 * The six daily prayer times (plus Sunrise and the derived Sunset) for one
 * location, date and parameter set.
 *
 * Times are computed eagerly on construction and exposed as
 * [java.time.OffsetDateTime] carrying the supplied [utcOffset], so a value reads
 * correctly as both a local wall-clock time and an absolute instant. A time
 * that the model cannot define (for example Fajr at a high latitude in summer
 * under [HighLatitudeRule.NONE]) is `null`.
 *
 * The caller supplies the UTC offset directly, including any daylight-saving
 * adjustment — this library carries no time-zone database.
 *
 * ```
 * val times = PrayerTimes(
 *     coordinates = Coordinates(24.3486, 56.6953, altitude = 5.0),
 *     dateComponents = DateComponents(2026, 6, 28),
 *     calculationParameters = CalculationMethod.UMM_AL_QURA.parameters(),
 *     utcOffset = ZoneOffset.ofHours(4),
 *     countryCode = "OM",
 * )
 * println(times.fajr) // 2026-06-28T04:1x+04:00
 * ```
 *
 * @property coordinates the observer's location.
 * @property dateComponents the civil date the times are computed for.
 * @property calculationParameters the angles, offsets and rules in effect.
 * @property utcOffset the offset from UTC applied to every returned time.
 * @property countryCode ISO-3166 alpha-2 code; influences the elevation and
 *   Ramadan rules. Optional.
 * @property cityName free-form location label; carried for the caller's
 *   convenience and not used in the calculation. Optional.
 */
public class PrayerTimes(
    public val coordinates: Coordinates,
    public val dateComponents: DateComponents,
    public val calculationParameters: CalculationParameters,
    public val utcOffset: ZoneOffset,
    public val countryCode: String = "",
    public val cityName: String = "",
) {
    /** Dawn prayer, or `null` if undefined. */
    public val fajr: OffsetDateTime?

    /** Sunrise (end of the Fajr window), or `null` if undefined. */
    public val sunrise: OffsetDateTime?

    /** Midday prayer, or `null` if undefined. */
    public val dhuhr: OffsetDateTime?

    /** Afternoon prayer, or `null` if undefined. */
    public val asr: OffsetDateTime?

    /** Sunset (from which Maghrib is derived), or `null` if undefined. */
    public val sunset: OffsetDateTime?

    /** Sunset prayer, or `null` if undefined. */
    public val maghrib: OffsetDateTime?

    /** Night prayer, or `null` if undefined. */
    public val isha: OffsetDateTime?

    init {
        val calculator = PrayerTimeCalculator(
            coordinates = coordinates,
            date = dateComponents,
            parameters = calculationParameters,
            countryCode = countryCode,
            timeZoneHours = utcOffset.totalSeconds / SECONDS_PER_HOUR,
        )

        val explicitRule = calculationParameters.highLatitudeRule
        val firstPass = calculator.compute(explicitRule ?: HighLatitudeRule.NONE)
        val resolved = if (explicitRule == null && firstPass.looksDegenerate()) {
            // Auto mode retries once with a night-fraction fallback.
            calculator.compute(HighLatitudeRule.SEVENTH_OF_THE_NIGHT)
        } else {
            firstPass
        }

        fajr = buildTime(roundedMinuteOfDay(resolved.fajr))
        sunrise = buildTime(roundedMinuteOfDay(resolved.sunrise))
        dhuhr = buildTime(roundedMinuteOfDay(resolved.dhuhr))
        asr = buildTime(roundedMinuteOfDay(resolved.asr))
        sunset = buildTime(roundedMinuteOfDay(resolved.sunset))
        maghrib = buildTime(roundedMinuteOfDay(resolved.maghrib))
        isha = buildTime(roundedMinuteOfDay(resolved.isha))
    }

    /** The [OffsetDateTime] for [prayer], or `null` for [Prayer.NONE] or an undefined time. */
    public fun timeForPrayer(prayer: Prayer): OffsetDateTime? = when (prayer) {
        Prayer.FAJR -> fajr
        Prayer.SUNRISE -> sunrise
        Prayer.DHUHR -> dhuhr
        Prayer.ASR -> asr
        Prayer.MAGHRIB -> maghrib
        Prayer.ISHA -> isha
        Prayer.NONE -> null
    }

    /**
     * The prayer whose window contains [at]: the latest prayer whose time is at
     * or before [at], or [Prayer.NONE] before the day's Fajr.
     */
    public fun currentPrayer(at: OffsetDateTime = OffsetDateTime.now()): Prayer =
        orderedTimes().lastOrNull { (_, time) -> time != null && !at.isBefore(time) }?.first
            ?: Prayer.NONE

    /**
     * The next prayer after [at]: the earliest prayer whose time is strictly
     * after [at], or [Prayer.NONE] once Isha has passed.
     */
    public fun nextPrayer(at: OffsetDateTime = OffsetDateTime.now()): Prayer =
        orderedTimes().firstOrNull { (_, time) -> time != null && at.isBefore(time) }?.first
            ?: Prayer.NONE

    private fun orderedTimes(): List<Pair<Prayer, OffsetDateTime?>> = listOf(
        Prayer.FAJR to fajr,
        Prayer.SUNRISE to sunrise,
        Prayer.DHUHR to dhuhr,
        Prayer.ASR to asr,
        Prayer.MAGHRIB to maghrib,
        Prayer.ISHA to isha,
    )

    private fun buildTime(minuteOfDay: Int?): OffsetDateTime? {
        if (minuteOfDay == null) return null
        return OffsetDateTime.of(
            dateComponents.year,
            dateComponents.month,
            dateComponents.day,
            minuteOfDay / MINUTES_PER_HOUR,
            minuteOfDay % MINUTES_PER_HOUR,
            0,
            0,
            utcOffset,
        )
    }

    private fun SolarTimes.looksDegenerate(): Boolean {
        val fajrMinute = roundedMinuteOfDay(fajr)
        val ishaMinute = roundedMinuteOfDay(isha)
        val fajrBroken = fajrMinute == null || fajrMinute == 0
        val ishaHour = ishaMinute?.div(MINUTES_PER_HOUR)
        val ishaBroken = ishaHour == null || ishaHour == 0 || ishaHour == NOON_HOUR
        return fajrBroken || ishaBroken
    }

    public companion object {
        private const val SECONDS_PER_HOUR = 3600.0
        private const val MINUTES_PER_HOUR = 60
        private const val NOON_HOUR = 12

        /**
         * Times for today's date at [utcOffset], resolving "today" as the local
         * date at that offset.
         */
        public fun today(
            coordinates: Coordinates,
            parameters: CalculationParameters,
            utcOffset: ZoneOffset,
            countryCode: String = "",
            cityName: String = "",
        ): PrayerTimes = PrayerTimes(
            coordinates = coordinates,
            dateComponents = DateComponents.from(LocalDate.now(utcOffset)),
            calculationParameters = parameters,
            utcOffset = utcOffset,
            countryCode = countryCode,
            cityName = cityName,
        )
    }
}
