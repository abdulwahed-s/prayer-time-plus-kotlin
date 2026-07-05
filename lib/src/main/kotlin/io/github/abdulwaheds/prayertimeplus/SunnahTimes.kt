package io.github.abdulwaheds.prayertimeplus

import java.time.Duration
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

/**
 * The recommended night times derived from a day's [PrayerTimes]: the middle
 * and the last third of the night.
 *
 * The night runs from this day's Maghrib to the following day's Fajr, so this
 * computes the next day's times internally. Both values are rounded to the
 * nearest minute, and are `null` when either bounding time is undefined.
 *
 * ```
 * val sunnah = SunnahTimes(PrayerTimes.today(coordinates, params, ZoneOffset.ofHours(4)))
 * println(sunnah.lastThirdOfTheNight)
 * ```
 */
public class SunnahTimes(prayerTimes: PrayerTimes) {
    /** The midpoint between Maghrib and the next day's Fajr, or `null` if undefined. */
    public val middleOfTheNight: OffsetDateTime?

    /** The start of the final third of the night, or `null` if undefined. */
    public val lastThirdOfTheNight: OffsetDateTime?

    init {
        val maghrib = prayerTimes.maghrib
        val nextFajr = nextDayFajr(prayerTimes)
        if (maghrib == null || nextFajr == null) {
            middleOfTheNight = null
            lastThirdOfTheNight = null
        } else {
            val nightSeconds = Duration.between(maghrib, nextFajr).seconds
            middleOfTheNight = roundToMinute(maghrib.plusSeconds(nightSeconds / HALVES))
            lastThirdOfTheNight =
                roundToMinute(maghrib.plusSeconds(nightSeconds * LAST_THIRD_NUMERATOR / THIRDS))
        }
    }

    private fun nextDayFajr(prayerTimes: PrayerTimes): OffsetDateTime? {
        val today = prayerTimes.dateComponents
        val tomorrow = DateComponents.from(
            LocalDate.of(today.year, today.month, today.day).plusDays(1),
        )
        return PrayerTimes(
            coordinates = prayerTimes.coordinates,
            dateComponents = tomorrow,
            calculationParameters = prayerTimes.calculationParameters,
            utcOffset = prayerTimes.utcOffset,
            countryCode = prayerTimes.countryCode,
            cityName = prayerTimes.cityName,
        ).fajr
    }

    private fun roundToMinute(time: OffsetDateTime): OffsetDateTime =
        time.plusSeconds(SECONDS_PER_HALF_MINUTE).truncatedTo(ChronoUnit.MINUTES)

    private companion object {
        const val HALVES = 2L
        const val THIRDS = 3L
        const val LAST_THIRD_NUMERATOR = 2L
        const val SECONDS_PER_HALF_MINUTE = 30L
    }
}
